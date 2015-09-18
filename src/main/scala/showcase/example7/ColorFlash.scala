package showcase.example7

import org.scalajs.dom
import org.scalajs.dom.ext.Color
import org.scalajs.dom.html

class ColorFlash(elm: html.Element) {
  val originalColor = elm.style.background

  def flash(color: Color = Color("Yellow"), timeout: Double = 250): Unit = {
    elm.style.background = color
    dom.setTimeout({ () =>
      elm.style.background = originalColor
    }, timeout)
  }
}
