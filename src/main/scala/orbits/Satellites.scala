package orbits

import org.denigma.threejs._
import scala.scalajs.js

/** 
 * tells us what satellites are currently in the animation
 * and how the orbit should be displayed
 */
case class SatDisplayOptions(
  val selectedTags: Seq[String],
  val orbitDisplay: String
)

case class Satellites(
  val satellites: Seq[Satellite],
  val tagToIds: Map[String, Seq[Int]]
) {
  
  def all = satellites
 
  /** 
   * apply a new color to all satellites that are currently
   * in the animation
   */
  def recolor(
    options: SatDisplayOptions,
    colorMap: Map[String, Color]
  ) {
    selected(options).foreach { sat =>
      sat.body.material.setValues(
        js.Dynamic.literal(color = colorMap(sat.constellationName))
      )
    }
  }

  /** 
   * return all satellites that are tagged with a selected tag
   */
  def selected(options: SatDisplayOptions) =
    all.filter { sat =>
      options.selectedTags.flatMap(tagToIds).contains(sat.satId)
    }.sortBy(_.constellationName) 

  def selectedConstellations(options: SatDisplayOptions) =
    selected(options)
      .map(_.constellationName)
      .toSet
      .toSeq

  /** 
   * update the body and orbit positions based on a date.
   */
  def positionAtDate(
    date: js.Date,
    options: SatDisplayOptions,
    colorMap: Map[String, Color] // BUG 1, color info only needed here because I am rebuilding the orbit object
  ) {
    all.foreach(_.hide)
    
    // super hacky TODO fix this, (it's getting late)
    var orbitLengthHours = 0
    
    if (options.orbitDisplay == "one_orbit") {
      orbitLengthHours = 3 //TODO calculate this for real
    } else if (options.orbitDisplay == "one_day") {
      orbitLengthHours = 24
    }

    selected(options).foreach { sat =>
      sat.show()
      sat.positionOrbitAtDate(
        date,
        orbitLengthHours,
        colorMap(sat.constellationName)
      )
      sat.positionBodyAtDate(date)
    }
  }
}

object Satellites {
  def apply(config: Config): Satellites = {
    val satellites = 
      config.tles.map { tle =>
        Satellite(
          ThreeHelper.line(),
          ThreeHelper.sphere(config.satelliteWidthKm/config.scale),
          tle.sat_id,
          tle.name,
          tle.line1,
          tle.line2,
          config.scale
        )
      }
    Satellites(satellites, config.tags)
  }
}