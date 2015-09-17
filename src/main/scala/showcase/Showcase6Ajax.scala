package showcase

import org.scalajs.dom.html
import scalatags.JsDom.all._

object Showcase6Ajax extends Showcase {
  def setupUi(container: html.Element): Unit = {
    container.appendChild(
      div(
        h1("Scala.js Showcase 6: AJAX!")
      ).render
    )
  }
}
