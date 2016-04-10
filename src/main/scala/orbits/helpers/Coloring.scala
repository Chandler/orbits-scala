package orbits

import org.denigma.threejs._

object ColorMap {
  def forConstellations(
    constellations: Seq[String]
  ): Map[String, Color] = {
    val colors = new ColorRotation()
    constellations
      .map { name: String => (name, colors.next()) }
      .toMap
  }
}

class ColorRotation() {
  var purple        = new Color(0xB54BC1)
  var cyan          = new Color(0x13E2EC)
  var red           = new Color(0xFF0000)
  var blue          = new Color(0x0089FF)
  var light_green   = new Color(0x96D811)
  var orange        = new Color(0xFF9800)
  var teal          = new Color(0x009688)
  var yellow        = new Color(0xFFEB3B)
  var grey          = new Color(0x9E9E9E)
  var darker_purple = new Color(0x42178E)
  var puke_green    = new Color(0xCDDC39)
  var brown         = new Color(0x795548)
  var white         = new Color(0xFFFFFF)
  var hot_pink      = new Color(0xF900FD)
  
  val colors = 
    Seq(
      light_green,  
      cyan,         
      red,          
      orange,       
      hot_pink,     
      blue,         
      purple,       
      teal,         
      yellow,      
      puke_green,   
      brown
    )

  private var colorIndex = 0

  def next(): Color = {
    val color = colors(colorIndex)
    if (this.colorIndex >= this.colors.length - 1) {
      this.colorIndex = 0
    } else {
      this.colorIndex = this.colorIndex + 1
    }
    return color
  }
}