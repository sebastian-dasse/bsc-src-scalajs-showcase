package showcase

import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scala.util.Random
import scalatags.JsDom.all._

import htmlib.ConvenientDiv._
import htmlib.DomUtil._


//import lib.html._
//object Showcase3TextBoxDecorators extends Showcase with ConvenientDiv with DomUtil {
object Showcase3TextBoxDecorators extends Showcase {
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
        replaceChild(tgtContainer, srcElmId)
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




    //  import lib.html.{DivDecorator => DDiv}
    //  new TBox("")
    DDiv("") //.stylez

    val am = DDiv(
      DDiv(background:="green"),
      fontFamily:="Arial"
    )("Awe-some!!")

    val bd1 = DDiv(
      background:="#F0F0F0",
      borderRadius:=5.px,
      padding:="0.5% 3%",   // top/bottom right/left
      margin:="1.0em 0.0em" // top/bottom right/left
    )
    val bd2 = DDiv(fontSize:=3.em)
    val bd3 = DDiv(bd1, fontSize:=3.em)


    container.appendChild(
      div(
        maxWidth:=1000.px,
        margin:="auto",
        h1("Scala.js Showcase 3"),
        div(
          am,
          DDiv(
            DDiv(background:="green"),
            fontFamily:="Arial"
          )("G-reat!!"),
          bd1.apply("Exactly!!!"),
          bd2.apply("Exactly!!!"),
          bd3.apply("Exactly!!!")
        //          sb
//          fff("hello 1"),
//          Foo(background:="green", color:="yellow")("hello 2")

        ),
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
