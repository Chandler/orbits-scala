package orbits
import scala.math._
import scala.scalajs.js

object SunPosition {
  def apply(date: js.Date, distance: Double) = solarPosition(date, distance)

  def solarDeclination(dayOfYear: Int, hourOfDay: Int): Double = {
    val radians_progress_around_sun = ((2*Pi)/365.25)*(dayOfYear + hourOfDay/24)
    val r = radians_progress_around_sun
    val declination = 0.396372-22.91327*cos(r)+4.02543*sin(r)-0.387205*cos(2*r) + 0.051967*sin(2*r)-0.154527*cos(3*r) + 0.084798*sin(3*r)
    // somehow this magic function operates on radians and returns degress, 
    // convert back to rads
    return declination * (Pi/180)
  }

  def dayOfYear(date: js.Date): Int = {
    val start  = new js.Date(date.getFullYear(), 0, 0)
    val diff   = date.getMilliseconds() - start.getMilliseconds()
    val oneDay = 1000 * 60 * 60 * 24
    val day    = Math.floor(diff / oneDay)
    return day.toInt
  }
  
  def solarPosition(date: js.Date, distance: Double): Point3D = {
    // what % of the day are we through
    // in terms of hours 1-24
    val dayProgress = (date.getUTCHours())/24.0;

    // what percent of the hour are we through
    val hourProgress = (date.getUTCMinutes()/60.0);

    // what percent of the day are we through, at minutly granularity
    val preciseProgres = dayProgress + hourProgress*(1/24.0);

    // the sun rotates east to west but our progress around the equator
    // goes west to east so we need to go backwards
    val descendingProgress = 1-preciseProgres;

    //convert progress % to radians, 100% = 2pi
    val progressToRadians = descendingProgress*2*Pi;

    // add one pi radians to convert from midnight to noon
    val noonRadiansFromLongZero = progressToRadians + Pi;

    val declinationOfSun = solarDeclination(dayOfYear(date), date.getUTCHours())
    
    // inclination (or polar angle)
    val azimuthAngle = noonRadiansFromLongZero

    // inclination (or polar angle)
    //
    // "The elevation angle is 90 degrees (π/2 radians) minus the inclination angle"
    //
    // in our case elevation is the declination of the sun, and we need to convert it into 
    // an inclination
    val polarAngle = (Pi/2) - declinationOfSun 

    val r = distance

    val x = r * sin(polarAngle) * cos(azimuthAngle)
    val y = r * sin(polarAngle) * sin(azimuthAngle)
    val z = r * cos(polarAngle)

    return Point3D(x, y, z)
  }
}