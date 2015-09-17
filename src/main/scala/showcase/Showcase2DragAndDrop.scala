package showcase

import scala.util.Random
import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scalatags.JsDom.all._

import lib.html.ConvenientDiv.box
import lib.html.DomUtil.replaceChild


//import lib.html._
//object Showcase1TextBoxes extends Showcase with ConvenientDiv with DomUtil {
object Showcase2DragAndDrop extends Showcase {
  def setupUi(container: html.Element): Unit = {
    def allowDrop: (dom.DragEvent => Unit) = evt =>
      evt.preventDefault()

    def drag: (dom.DragEvent => Unit) = evt => evt.target match {
      case targetDiv: html.Div =>
        evt.dataTransfer.setData("text", targetDiv.id)
    }

    def drop: (dom.DragEvent => Unit) = evt => evt.currentTarget match {
      case tgtContainer: html.Div =>
        evt.preventDefault()
        val srcElmId = evt.dataTransfer.getData("text")
        replaceChild(srcElmId, tgtContainer)
    }

    def clickDebug = (ev: dom.MouseEvent) => ev.currentTarget match {
      case elm: html.Element => console.log(elm.id)
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
}
