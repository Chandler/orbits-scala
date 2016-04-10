package orbits

import org.denigma.threejs._
import scala.scalajs.js

object ThreeHelper {
  def sphere(
    width: Double,
    segments: Int = 32,
    color: Int = 0xffffff
  ): Mesh =
    new Mesh(
      new SphereGeometry(width, segments, segments),
      new MeshBasicMaterial(
        js.Dynamic.literal(color = color).asInstanceOf[MeshBasicMaterialParameters]
      )
    )

  def line(
    color: Double = 0xffffff,
    linewidth: Int = 2
  ): Line =
    new Line(
      new Geometry(),
      new LineBasicMaterial(
        js.Dynamic.literal(
          color = color,
          linewidth = linewidth
        ).asInstanceOf[LineBasicMaterialParameters]
      )
    )
}
