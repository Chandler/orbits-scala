package orbits

import scalatags.Text.all._

/**
 * Generates the primary HTML for this application in a #type #safe way. A small amount of initial
 * structural HTML is fixed in index.html.
 */
object OrbitsHtml {
  def apply() =
    html(
      div(`class`:="animation_options_box") (
        div(`class`:="time")(
          raw("&zwnj") //invisible char for space holder until real time is added on the client.
        ),

        div(`class`:="controls")(
          speedSelectorRadio(),
          div(id:="orbit_paths", `class`:="hidden_on_mobile")(
            "Show Orbit Paths",
            orbitsPathRadio()
          )
        ),

        div(id:="more_options", `class`:="hidden_on_desktop")(
          "more options avaliable on displays wider than 600px"
        ),

        div(id:="tag-selector-wrapper", `class`:="hidden_on_mobile")(
          select(id:="tag-selector")
        ),

        div(id:="legend", `class`:="hidden_on_mobile"),

        a(href:="https://github.com/Chandler/orbits-scala")("View on Github")
      ),
      div(id:="scene")
    ).render

  def speedSelectorRadio =
    div(`class`:="btn-group btn-group-justified", data.toggle:="buttons")(
      label(`class`:="btn btn-primary", id:="realtime")(
        input(`type`:="radio")("real time")
       ),
      label(`class`:="btn btn-primary", id:="100x")(
        input(`type`:="radio")("100x")
       ),
      label(`class`:="btn btn-primary active", id:="1000x")(
        input(`type`:="radio")("1000x")
       )
    )

  def orbitsPathRadio =
    div(`class`:="btn-group btn-group-justified", data.toggle:="buttons")(
      label(`class`:="btn btn-primary", id:="orbits_off")(
        input(`type`:="radio")("off")
       ),
      label(`class`:="btn btn-primary active", id:="orbits_one_orbit")(
        input(`type`:="radio")("1 orbit")
       ),
      label(`class`:="btn btn-primary", id:="orbits_one_day")(
        input(`type`:="radio")("1 day")
       )
    )
}