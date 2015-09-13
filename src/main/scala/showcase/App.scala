package showcase

import scalajs.js.annotation.JSExport

import lib.html.DomUtil.getContainer


@JSExport
//object App extends lib.html.DomUtil {
object App {

  @JSExport
  def main(): Unit = {
    println("Hello world!")

    val container = getContainer()
    container.innerHTML = ""
//    Showcase1TextBoxes(container)
//    Showcase2DragAndDrop(container) // TODO use a proper data model
    Showcase3TextBoxDecorators(container)
  }
}
