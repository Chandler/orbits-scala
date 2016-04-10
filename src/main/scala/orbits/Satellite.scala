package orbits

import org.denigma.threejs._
import scala.scalajs.js
import js.JSConverters._

case class Satellite(
  var orbit: Line,
  var body: Mesh,
  val satId: Int,
  val constellationName: String,
  val tleLine1: String,
  val tleLine2: String,
  val scale: Double
) {
  def show() {
    body.visible = true
    orbit.visible = true
  }

  def hide() {
    body.visible = false
    orbit.visible = false
  }

  def positionOrbitAtDate(
    date: js.Date,
    orbitLengthHours: Int,
    color: Color
  ) = {
    if (orbitLengthHours > 0) {
      val resolution = 3

      val numPoints = math.floor(((orbitLengthHours*60) * 0.95)/resolution).toInt
      
      var previousDate = date

      val points = 
        (1 to numPoints).map { _ =>
          val date = new js.Date(previousDate.getTime() + -resolution*60000)
          previousDate = date
          val point = Coordinates.tleToEcf(tleLine1, tleLine2, date)
          new Vector3(
            point.x/scale,
            point.y/scale,
            point.z/scale
          )
        }

      val newOrbit = ThreeHelper.line(color = color.getHex())
      newOrbit.geometry.vertices = points.toJSArray
      orbit = newOrbit
    } else {
      orbit = ThreeHelper.line()
    }
  }

  def positionBodyAtDate(date: js.Date) {
    val point = Coordinates.tleToEcf(tleLine1, tleLine2, date)
    body.position.x = point.x/scale
    body.position.y = point.y/scale
    body.position.z = point.z/scale  
  }
}
