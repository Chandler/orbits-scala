package orbits

import OrbitImplicits._
import org.denigma.threejs._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.jquery._
import prickle._
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ global => g }
import scala.scalajs.js.JSConverters._
import scala.util.{Try, Success, Failure}
import scalajs.js.annotation.{JSExport, JSName}
import upickle.default._

/** 
 * The json satellite data returned from the server is deserialized into these typed objects
 */
case class SatDescription(sat_id: Int, name: String, line1: String, line2: String)
case class ServerResponse(
  tles: Seq[SatDescription],
  tags: Map[String, Seq[Int]],
  constellationUrls: Map[String, String]
)

case class Point3D(x: Double, y: Double, z: Double)

/**
 * Several of the values in this config are globally mutated by this application
 * As far as I can tell, this is just how 3D animation programming works.
 *
 * write: The `onClick` handlers respond to user input and update the config.
 * read: The main animation loop reads the config for each frame and applies the user
 * input.
 *
 * All of the shared state in this app is one instance of this config object
 */
class Config(
  val tles: Seq[SatDescription],
  val tags: Map[String, Seq[Int]]
) {
  val satelliteWidthKm          = 150.0
  
  // a scale factor to bring the earth's widths and satellite
  // coordinates down to values around 1
  val scale                     = 10000.0
  
  // how far our fake sun lightsource is from the center of the earth, in km
  // works just fine if it's actually inside the earth.
  val distanceToSun             = 10.0
  
  // assuming 60 fps for now, you could calculate the actual fps
  // http://stackoverflow.com/a/5111475/67166 like this
  val fps                       = 60

  val originalDate              = new js.Date()

  // mutable
  var speedupMultiplier         = 1000
  var selectedTags              = Seq("Cubesats")
  var orbitDisplay              = "one_orbit"
  var currentDate               = new js.Date()

  def incrementCurrentDateSeconds(amount: Int) = 
    currentDate.setSeconds(currentDate.getSeconds() + amount)
}

@JSExport
object App extends {
  /**
   *  1) attach the generated HTML to the page
   *  2) fetch data from the server
   *  3) kick off the animation
   */
  @JSExport
  def main(target: html.Div): Unit = {
    target.innerHTML = OrbitsHtml()

    Ajax.get("generated/tles_and_tags.json").onSuccess { case xhr =>
      val response = read[ServerResponse](xhr.responseText)

      // this is mutable, the onclick handlers updates the values and the render()
      // function reads them before rendering each frame.
      var config = new Config(response.tles, response.tags)

      initializeAnimation(config)
    }
  }

  /**
   *  Setup the initial WebGL scene and dom click handlers.
   */
  def initializeAnimation(config: Config) = {
    val webglEl: HTMLElement = dom.document.getElementById("scene").asInstanceOf[HTMLElement]
    val width    = dom.window.innerWidth.asInstanceOf[Double]
    val height   = dom.window.innerHeight.asInstanceOf[Double]
    var scene    = new Scene()
    val renderer = new WebGLRenderer()
    renderer.setSize(width, height)

    var earthRadiusKm = 6378.135 // width of the earth in kilometers
    val camera            = new PerspectiveCamera(45, width / height, 0.1, 1000)
    camera.position.z     = 3 //position camera above north pole
    camera.position.y     = 0
    camera.position.x     = 0
    camera.rotation.order = "YXZ"

    scene.add(new AmbientLight(0x333333))
    var light = new DirectionalLight(0xffffff, 1)
    light.position.set(10000/config.scale,10000/config.scale,10000/config.scale)
    scene.add(light)

    var radius = earthRadiusKm/config.scale
    var segments = 32 // how many polygons to use when rendering the earth sphere
    
    var materialParams = 
      js.Dynamic.literal(
        map = ImageUtils.loadTexture("images/4kearth.jpg"),
        specular = new Color(0xffffff)  
      ).asInstanceOf[MeshPhongMaterialParameters]
   
    var sphere = 
      new Mesh(
        new SphereGeometry(radius, segments, segments),
        new MeshPhongMaterial(materialParams)
      )

    // rotate along the X axis so that Z lines up with the north pole
    // this makes our earth fit with the ECEF coordinates which assume a Z
    // north pole. 
    // not sure what the units are but 1.6 is the rotation that gets closest to Z = north
    sphere.rotation.x = 1.6
    scene.add(sphere)

    // add colored axi extending twice the width of earth
    scene.add(new AxisHelper((earthRadiusKm*2)/config.scale))

    // controls update camera position based on mouse dragging
    var controls = new TrackballControls(camera)

    webglEl.appendChild(renderer.domElement)

    DomActions.setupRadioButtons(config)
    DomActions.setupTagSelector(config)

    val satellites = Satellites(config)

    satellites.all.foreach(sat => scene.add(sat.body))
    satellites.all.foreach(sat => scene.add(sat.orbit))

    render(
      scene,
      satellites,
      light,
      config,
      renderer,
      camera,
      controls
    )
  }

  /**
   *  The main animation loop that is called for every frame.
   */
  def render(
    scene: Scene,
    satellites: Satellites,
    light: Light,
    config: Config,
    renderer: Renderer,
    camera: Camera,
    controls: TrackballControls
  ): Unit = {
    controls.update()
    
    config.incrementCurrentDateSeconds(
      Try(config.speedupMultiplier/config.fps).getOrElse(0)
    )

    DomActions.updateDateField(config.currentDate)

    val displayOptions = SatDisplayOptions(config.selectedTags, config.orbitDisplay)

    val selectedConstellations = satellites.selectedConstellations(displayOptions)

    val constellationsColors = ColorMap.forConstellations(selectedConstellations)

    // BUG 1: for some reason I can't modify existing orbit objects so I have to remove
    // them completely on each frame and add new ones
    satellites.all.foreach(sat => scene.remove(sat.orbit))
    satellites.positionAtDate(config.currentDate, displayOptions, constellationsColors)
    satellites.all.foreach(sat => scene.add(sat.orbit)) // BUG 1

    satellites.recolor(displayOptions, constellationsColors)
    
    DomActions.updateLegend(selectedConstellations, constellationsColors) 
    
    val sunCoordinates = SunPosition(config.currentDate, config.distanceToSun)
    light.position.set(sunCoordinates.x, sunCoordinates.y, sunCoordinates.z)

    dom.window.requestAnimationFrame(
      (_: Double) => 
        render(
          scene,
          satellites,
          light,
          config,
          renderer,
          camera,
          controls
        )
    )
    renderer.render(scene, camera)
  }
}