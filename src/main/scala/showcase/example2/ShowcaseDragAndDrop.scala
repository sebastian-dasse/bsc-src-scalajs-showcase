package showcase.example2

import scala.util.Random
import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scalatags.JsDom.all._

import htmlib.ConvenientDiv._
import htmlib.DomUtil._
import showcase.Showcase


/*
info on drag and drop with JavaScript:
  - http://www.w3schools.com/html/html5_draganddrop.asp
  - https://developer.mozilla.org/en-US/docs/Web/Guide/HTML/Drag_operations
  - http://www.html5rocks.com/de/tutorials/dnd/basics/
 */

object ShowcaseDragAndDrop extends Showcase {
  def setupUi(container: html.Element): Unit = {
    container.appendChild(
      div(
        maxWidth:=1000.px,
        margin:="auto",
        h1("Scala.js Showcase 2"),
        div(
          invisibleBox(
            draggableBox("d1")(
              h3("first"),
              p("foo " , em("bar"), " baz"),
              p("ping pang")
            )
          ),
          invisibleBox(
            draggableBox("xd2")(
              h3("second")
            )
          ),
          invisibleBox(
            draggableBox("d3")(
              h3("third")
            )
          )
        )
      ).render
    )
  }

  def draggableBox(theId: String = Random.nextString(5))(xs: Modifier*) = box(
    id:=theId,
    border:="solid 1px blue",
    margin:="0 0",   // top/bottom right/left
    fontSize:=1.25.em,
    fontFamily:="Arial, Helvetica, sans-serif",
    draggable:="true",
    ondragstart:=drag,
    onclick:=clickDebug
  )(xs)

  def invisibleBox(xs: Modifier*) = box(
    background:="#FFFF99",
    border:="",
    minHeight:=2.em,
    padding:="0 0",   // top/bottom right/left
    ondrop:=drop,
    ondragover:=allowDrop
  )(xs)

  def allowDrop: (dom.DragEvent => Unit) = evt => evt.preventDefault()

  def drag: (dom.DragEvent => Unit) = evt => evt.target match {
    case targetDiv: html.Div =>
      evt.dataTransfer.setData("text", targetDiv.id)
  }

  def drop: (dom.DragEvent => Unit) = evt => evt.currentTarget match {
    case tgtContainer: html.Div =>
      evt.preventDefault()
      val srcElmId = evt.dataTransfer.getData("text")
      replaceChild(tgtContainer, srcElmId)
  }

  def clickDebug = (ev: dom.MouseEvent) => ev.currentTarget match {
    case elm: html.Element => console.log(elm.id)
  }
}
