package showcase

import scala.util.Random
import org.scalajs.dom
import org.scalajs.dom.{console, document, html}
import scalatags.JsDom.all._

import showcase.TextBox._


object Showcase2DragAndDrop {
  def apply(container: html.Element): Unit = setupUi(container)

  private def allowDrop: (dom.DragEvent => Unit) = evt =>
    evt.preventDefault()

  private def drag: (dom.DragEvent => Unit) = evt => evt.target match {
    case targetDiv: html.Div =>
      evt.dataTransfer.setData("text", targetDiv.id)
  }

  /*/// 1. imperative version - the opposite of elegant
  private def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit = {
    val droppedElm = document.getElementById(srcElmId)
    if (droppedElm != null) {
      val srcContainer = droppedElm.parentNode
      if (tgtContainer.hasChildNodes) {
        val droppedOnElm = tgtContainer.firstElementChild
        srcContainer.appendChild(droppedOnElm)
      }
      tgtContainer.appendChild(droppedElm)
    }
  }*/

  /*/// 2. naive version using Options - quite similar
  private def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit = {
    val droppedElm = Option(document.getElementById(srcElmId))
    if (droppedElm.isDefined) {
      val srcContainer = droppedElm.get.parentNode
      val droppedOnElm = Option(tgtContainer.firstElementChild)
      if (droppedOnElm.isDefined) {
        srcContainer.appendChild(droppedOnElm.get)
      }
      tgtContainer.appendChild(droppedElm.get)
    }
  }*/

  /// 3. functional using for comprehension - very readable
  private def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit =
    for {
      droppedElm <- Option(document.getElementById(srcElmId))
      srcContainer = droppedElm.parentNode
      droppedOnElm = Option(tgtContainer.firstElementChild)
    } yield {
      droppedOnElm.foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    }

  /*/// 4. strictly functional version - very concise but hard to read
  private def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit =
    Option(document.getElementById(srcElmId)).foreach( droppedElm => {
      val srcContainer = droppedElm.parentNode
      Option(tgtContainer.firstElementChild).foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    })*/


  private def drop: (dom.DragEvent => Unit) = evt => evt.currentTarget match {
    case tgtContainer: html.Div =>
      evt.preventDefault()
      val srcElmId = evt.dataTransfer.getData("text")
      replaceChild(srcElmId, tgtContainer)
  }

  private def clickDebug = (ev: dom.MouseEvent) => ev.currentTarget match {
    case elm: html.Element => console.log(elm.id)
  }

  private def draggableBox(theId: String = Random.nextString(5))(xs: Modifier*) = box(
    id:=theId,
    border:="solid 1px blue",
    margin:="0 0",   // top/bottom right/left
    fontSize:=1.25.em,
    fontFamily:="Arial, Helvetica, sans-serif",
    draggable:="true",
    ondragstart:=drag,
    onclick:=clickDebug
  )(xs)

  private def invisibleBox(xs: Modifier*) = box(
    background:="#FFFF99",
    border:="",
    minHeight:=2.em,
    padding:="0 0",   // top/bottom right/left
    ondrop:=drop,
    ondragover:=allowDrop
  )(xs)

  private def setupUi(container: html.Element): Unit = {
    println("hi there!")
    container.appendChild(
      div(
        maxWidth:=1000.px,
        margin:="auto",
        h1("Scala.js Showcase 1"),
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
      ).render
    )
  }
}
