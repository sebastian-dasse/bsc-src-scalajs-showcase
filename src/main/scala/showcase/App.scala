package showcase

import scalajs.js.annotation.JSExport

import showcase.Util._


@JSExport
object App /*extends MyBox*/ {

  @JSExport
  def main(): Unit = {
    println("Hello world!")

    val container = getContainer()
    container.innerHTML = ""
//    Showcase1TextBoxes(container)
    Showcase2DragAndDrop(container)
  }
}
