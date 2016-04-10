package orbits

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ global => g }

/*
 * TLE: Two Line Element Set - a standard format for representing the position and velocity of a satellite
 * at a given point in time. Using ~physics~ this timestamp can be used to predict a satellites
 * future or past positions. https://en.wikipedia.org/wiki/Two-line_element_set
 * 
 * ECEF: Earth Centered Earth Fixed - is a 3D cartesian coordinate system where (0,0,0)
 * is the center of an earth that does not move. In our simulation, the earth never moves, the sun
 * and satellites move around it. So we need ECEF. https://en.wikipedia.org/wiki/ECEF
 * If the earth rotated around it's own axis or the sun we might use ECI https://en.wikipedia.org/wiki/Earth-centered_inertial
 *
 * This library uses javascript directly via js.Dynamic.global because there is no typed facade for satellites.js
 * What this means is calls like `g.satellite.eciToEcf` are not type safe and will explode at runtime if
 * satellite.eciToEcf don't exist.
 */
object Coordinates {
  /** calculate a satellite's position for a given time */
  def tleToEcef(
    line1: String,
    line2: String,
    date: js.Date
  ): Point3D = {
    var gmst = g.satellite.gstimeFromDate(
      date.getUTCFullYear(),
      date.getUTCMonth() + 1, // Note, this function requires months in range 1-12.
      date.getUTCDate(),
      date.getUTCHours(),
      date.getUTCMinutes(),
      date.getUTCSeconds()
    )

    var positionAndVelocity = tleToPositionAndVelocity(line1, line2, date)
        
    // https://en.wikipedia.org/wiki/Earth-centered_inertial
    var positionEci = positionAndVelocity.position
    
    //https://en.wikipedia.org/wiki/ECEF
    var positionEcef = g.satellite.eciToEcf(positionEci, gmst)

    return Point3D(
      positionEcef.x.asInstanceOf[Double],
      positionEcef.y.asInstanceOf[Double],
      positionEcef.z.asInstanceOf[Double]
    )
  }

  private def tleToPositionAndVelocity(
    line1: String,
    line2: String,
    date: js.Date
  ): js.Dynamic =
    g.satellite.propagate(
      g.satellite.twoline2satrec(line1, line2),
      date.getUTCFullYear(),
      date.getUTCMonth() + 1,
      date.getUTCDate(),
      date.getUTCHours(),
      date.getUTCMinutes(),
      date.getUTCSeconds()
    )
}