package orbits

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ global => g }
import org.scalajs.dom.raw.HTMLElement
import scala.scalajs.js.JSConverters._
import org.scalajs.jquery._
import org.scalajs.dom
import org.denigma.threejs._

/**
 * helper methods for setting up click handlers and performing 
 * other dom modifications
 */
object DomActions {
  def updateLegend(
    constellations: Seq[String],
    colorMap: Map[String, Color]
  ) {
    jQuery("#legend").text("")
    constellations.foreach { name =>
      val color = "#" + colorMap(name).getHexString()
      val snippet = 
        s"""
           <div>
             <div class="circle" style="background-color: ${color}"></div>
             <div class="label-text"> ${name} </div>
           </div>
        """
      jQuery("#legend").append(snippet)      
    }
  }

  def updateDateField(date: js.Date) {
    g.jQuery(".time").text(
      g.dateFormat(
        date,
        "mmmm dS, yyyy, h:MM TT", true
      ) + " UTC"
    )
  }

  def setupRadioButtons(config: Config) = {
    jQuery("#realtime").click(
      (e: HTMLElement) => {
        config.speedupMultiplier = 0
        config.currentDate = config.originalDate
      }
    )

    jQuery("#100x").click(
      (e: HTMLElement) => {
        config.speedupMultiplier = 100
        config.currentDate = config.originalDate
      }
    )

    jQuery("#1000x").click(
      (e: HTMLElement) => {
        config.speedupMultiplier = 1000
        config.currentDate = config.originalDate
      }
    )

    jQuery("#orbits_off").click(
      (e: HTMLElement) => {
        config.orbitDisplay = "off"
      }
    ) 

    jQuery("#orbits_one_orbit").click(
      (e: HTMLElement) => {
        config.orbitDisplay = "one_orbit"
      }
    ) 

    jQuery("#orbits_one_day").click(
      (e: HTMLElement) => {
        config.orbitDisplay = "one_day" 
      }
    )
  }


  def setupTagSelector(config: Config) = {
    // an example of dropping into pure javascript without types
    // since select2 is not part of the scalajs-jquery API
    g.jQuery("#tag-selector").select2(
      js.Dynamic.literal(
        data = config.tags.keySet.toSeq.toJSArray,
        multiple = true
      )
    )
    
    // some crazy bug where this only works on the second try
    g.jQuery("#tag-selector").select2().`val`(config.selectedTags.toJSArray)
    g.jQuery("#tag-selector").select2().`val`(config.selectedTags.toJSArray)

    // TODO fix `BezierClass is not defined` error
    // g.jQuery("#tag-selector").on(
    //   "select2:open",
    //   () =>
    //     g.jQuery(".select2-results__options")
    //       .niceScroll(js.Dynamic.literal(cursorborder = "none"))
    // )

    g.jQuery("#tag-selector").on(
      "select2:select select2:unselect",
      (
        (e: HTMLElement) => { //e is `this` in javascript
          var selected = g.jQuery(e).`val`().asInstanceOf[js.Array[String]]
          if (selected != null) {
            config.selectedTags = selected.toArray.toSeq
          } else {
            config.selectedTags = Nil
          }
        }
      ): js.ThisFunction
    )
  }
}