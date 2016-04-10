package orbits

import org.scalajs.jquery._
import scala.scalajs.js
import org.scalajs.dom.raw.HTMLElement
import scalajs.js.annotation.JSName
import org.denigma.threejs._
import scala.collection.mutable

// The best way to call out to pure javascript is to make a typed Scala facade
// for the JS API you are calling. Many JS APIs like JQuery and Three.js have complete
// facades, while the ones in this file are incomplete. They only define the methods
// and members that I'm using in Orbits.scala

object OrbitImplicits {
  implicit def jq2Select2(jq: JQuery): Select2JQuery = jq.asInstanceOf[Select2JQuery]
}

@js.native
@JSName("THREE.TrackballControls")
class TrackballControls extends js.Object {
  def this(
    camera: Camera = js.native,
    domElement: HTMLElement = js.native
  ) = this()
  def update(): Unit = js.native
}

@js.native
trait Select2Options extends js.Object {
  var data: Seq[String] = js.native
  var multiple: Boolean = js.native
}

@js.native
@JSName("JQuery")
class Select2JQuery extends js.Object {
  def select2(options: Select2Options): JQuery = js.native;
  def select2(): JQuery = js.native;
  def `val`(items: Seq[String]): JQuery = js.native;
}

