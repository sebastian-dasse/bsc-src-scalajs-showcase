package htmlib

import org.scalajs.dom.{document, html}
import scala.annotation.tailrec
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object DomUtil extends DomUtil


trait DomUtil {

  /**
   * Returns the element with the id 'container' or, if no element with this
   * name exists, creates a `&lt;div&gt;` with this id and returns it.
   */
  def getContainer(theId: String = "container"): html.Div = {
    def grabContainer = Option(document.getElementById("container"))
    def createContainer = document.body.appendChild(div(id:=theId).render)
    grabContainer.getOrElse(createContainer)
  }.asInstanceOf[html.Div]

//----------------------------------------------------------

  /// 1. imperative version - the opposite of elegant
  /*def replaceChild(tgtContainer: html.Div, newElmId: String): Unit = {
    val droppedElm = document.getElementById(newElmId)
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
  def replaceChild(tgtContainer: html.Div, newElmId: String): Unit = {
    val droppedElm = Option(document.getElementById(newElmId))
    if (droppedElm.isDefined) {
      val srcContainer = droppedElm.get.parentNode
      val droppedOnElm = Option(tgtContainer.firstElementChild)
      if (droppedOnElm.isDefined) {
        srcContainer.appendChild(droppedOnElm.get)
      }
      tgtContainer.appendChild(droppedElm.get)
    }
  }*/

  /// 3. functional version using for comprehension - very readable
  /** Replaces the first child element of the target container with the specified element. */
  def replaceChild(tgtContainer: html.Element, newElmId: String): Unit =
    for {
      droppedElm <- Option(document.getElementById(newElmId))
      srcContainer = droppedElm.parentNode
      droppedOnElm = Option(tgtContainer.firstElementChild)
    } yield {
      droppedOnElm.foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    }

  /*/// 4. strictly functional version - very concise but harder to read
  def replaceChild(tgtContainer: html.Div, newElmId: String): Unit =
    Option(document.getElementById(newElmId)).foreach( droppedElm => {
      val srcContainer = droppedElm.parentNode
      Option(tgtContainer.firstElementChild).foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    })*/

  //----------------------------------------------------------

  /** Replaces all child elements of the target container with the specified element. */
//  def replaceChildren(cont: html.Element, repl: html.Element): Unit = {
  def replaceChildren(cont: html.Element, repl: html.Element): Unit = {
    val parent = cont.parentElement
//    while (Option(cont.firstElementChild) != None) {
//      cont.removeChild(cont.firstElementChild)
//    }
    @tailrec def loop(): Unit = Option(cont.firstElementChild) match {
      case Some(child) =>
        cont.removeChild(child)
        loop()
      case None => ()
    }
    loop()
    cont.appendChild(repl)
  }

  //----------------------------------------------------------
}
