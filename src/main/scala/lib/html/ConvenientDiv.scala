package lib.html

import org.scalajs.dom.html
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

import Defs._


/**
 * Provides methods for convenient creation of `&lt;div&gt;` elements.
 * - (Available via `import`).
 */
object ConvenientDiv extends ConvenientDiv

/**
 * Provides methods for convenient creation of `&lt;div&gt;` elements.
 * - (Available via `extends` or `with`).
 */
trait ConvenientDiv {

  def box[S <: Modifier](styles: S*)(contents: Modifier*): HtmlTag /*TypedTag[html.Div]*/ = div(
    background:=ColorLightGrey,
    borderRadius:=BorderRadiusDefault,
//    padding:=Seq(0.5.em, 1.5.em).mkString(" "),
    padding:="0.25em 1.5em",  // top/bottom right/left
    margin:="1.0em 0.0em",    // top/bottom right/left
    styles,
    contents
  )


  private[html] trait DivBase {
    protected[html] def stylez: Seq[Modifier]
    def apply(contents: Modifier*): HtmlTag /*TypedTag[html.Div]*/ = div(stylez, contents)
  }

  private[html] class DivDecorator(decorated: DivBase, styles: Modifier*) extends DivBase {
    def this(styles: Modifier*) = this( new DivBase{ override def stylez = styles } )
    override protected[html] def stylez: Seq[Modifier] = decorated.stylez ++ styles
  }

  val DDiv = DivDecorator

  object DivDecorator {
    def apply(styles: Modifier*) = new DivDecorator(styles:_*)
    def apply(decorated: DivBase, styles: Modifier*) = new DivDecorator(decorated, styles:_*)
  }
}
