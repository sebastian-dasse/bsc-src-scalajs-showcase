package showcase

import org.scalajs.dom.{document, html, raw}
import scala.util.Random
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object App {

  @JSExport
  def main(): Unit = {
    println("Hello world!")

    val container = getContainerWScalaTagsJsDom
    setupUiWScalaTagsJsDom(container)
  }

  def getContainerWScalaTagsJsDom: html.Div = {
    def grabContainer = Option(document.getElementById("container"))
    def createContainer = document.body.appendChild(div(id:="container").render)
    grabContainer.getOrElse(createContainer)
  }.asInstanceOf[html.Div]

  def setupUiWScalaTagsJsDom(container: html.Element): Unit = {
    import scalatags.JsDom.all._

    val (foo, bar) = ("simple", Random.nextInt(100))

    container.appendChild(
      div(                            // TypedTag[html.Div]
        h1("Hello scalatags.JsDom!"), // TypedTag[html.Heading]
        p(
          "This is yet a ", i(foo), " html snippet showing a random number: ", b(bar), "."
        ),                            // TypedTag[html.Paragraph]
        p("But soon this will be a more complex website to showcase Scala.js.")
      ).render                        // html.Div
    )
  }
}
