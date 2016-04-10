package orbits

import scala.scalajs.js
import js.Dynamic.{ global => g }

object Coordinates {
  def tleToEcf(
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

    // https://en.wikipedia.org/wiki/ECEF
    // ecef is the fixed earth coordinate system which is what we want
    // in this example since our earth doesn't rotate.
    // if earth was rotating instead of the sun we would need the ECI
    // coordinates I think
    var positionEcf = g.satellite.eciToEcf(positionEci, gmst)

    return Point3D(
      positionEcf.x.asInstanceOf[Double],
      positionEcf.y.asInstanceOf[Double],
      positionEcf.z.asInstanceOf[Double]
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