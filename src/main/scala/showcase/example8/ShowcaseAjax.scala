package showcase.example8

import org.scalajs.dom.html
import scalatags.JsDom.all._

import showcase.Showcase

object ShowcaseAjax extends Showcase {
  def setupUi(container: html.Element): Unit = {
    container.appendChild(
      div(
        h1("Scala.js Showcase 8: AJAX!")
      ).render
    )
  }
}
