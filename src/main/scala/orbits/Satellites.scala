package orbits

import org.denigma.threejs._
import scala.scalajs.js

case class SatDisplayOptions(
  val selectedTags: Seq[String],
  val orbitDisplay: String
)

case class Satellites(
  val satellites: Seq[Satellite],
  val tagToIds: Map[String, Seq[Int]]
) {
  
  def all = satellites

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

  def selected(options: SatDisplayOptions) =
    all.filter { sat =>
      options.selectedTags.flatMap(tagToIds).contains(sat.satId)
    }.sortBy(_.constellationName) 

  def selectedConstellations(options: SatDisplayOptions) =
    selected(options)
      .map(_.constellationName)
      .toSet
      .toSeq

  def positionAtDate(
    date: js.Date,
    options: SatDisplayOptions,
    colorMap: Map[String, Color]
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