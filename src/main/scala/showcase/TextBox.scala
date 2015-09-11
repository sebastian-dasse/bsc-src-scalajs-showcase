package showcase

//import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._
//import scalatags.generic.StylePair


object TextBox extends TextBox

trait TextBox {
  private[TextBox] object Defs{
    val ColorLightGrey = "#F0F0F0"
    val BorderRadiusDefault = 5.px
    val BorderCode = "solid 1px #C8C8C8"  // style width color
  }
  import Defs._

//  def box(styles: StylePair[dom.Element, _]*)(xs: Modifier*): TypedTag[html.Div] = div(
  def box[T <: Modifier](predefined: T*)(xs: Modifier*): TypedTag[html.Div] = div(
    background:=ColorLightGrey,
    borderRadius:=BorderRadiusDefault,
//    padding:=Seq(0.5.em, 1.5.em).mkString(" "),
    padding:="0.25em 1.5em",  // top/bottom right/left
    margin:="1.0em 0.0em",    // top/bottom right/left
    predefined,
    xs
  )

  def importantBox(xs: Modifier*): TypedTag[html.Div] = box(
    border:="solid 2px red", // style width color
    fontSize:=1.25.em
  )(xs)

  def greenBox(xs: Modifier*): TypedTag[html.Div] = box(
    background:="#00F0F0",
    border:="solid 2px green", // style width color
    fontSize:="small"
  )(xs)

  def codeBox(xs: Modifier*): TypedTag[html.Pre] = pre(
    background:=ColorLightGrey,
    border:=BorderCode,
    borderRadius:=5.px,
    padding:="0.0em 1.5em", // top/bottom right/left
    code( xs )
  )

  def codeInline(xs: Modifier*): TypedTag[html.Element] = code(
    background:=ColorLightGrey,
    border:=BorderCode,
    borderRadius:=BorderRadiusDefault,
    padding:="0.25em 0.5em",      // top/bottom right/left
    margin:="0.0em 0.25em",       // top/bottom right/left
    xs
  )
}
