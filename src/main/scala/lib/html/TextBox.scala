package lib.html

import org.scalajs.dom.html
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

import ConvenientDiv.box
import Defs._


/**
 * Provides convenience methods for CSS-styled textboxes.
 * - (Available via `import`).
 */
object TextBox extends TextBox

/**
 * Provides convenience methods for CSS-styled textboxes.
 * - (Available via `extends` or `with`).
 */
trait TextBox {
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
