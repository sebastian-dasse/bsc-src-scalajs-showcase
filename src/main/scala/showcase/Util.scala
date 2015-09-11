package showcase

import org.scalajs.dom.{document, html}
import scalatags.JsDom.all._


object Util extends Util

trait Util {
  def getContainer(theId: String = "container"): html.Div = {
    def grabContainer = Option(document.getElementById("container"))
    def createContainer = document.body.appendChild(div(id:=theId).render)
    grabContainer.getOrElse(createContainer)
  }.asInstanceOf[html.Div]
}
