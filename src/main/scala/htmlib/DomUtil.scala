package htmlib

import org.scalajs.dom
import org.scalajs.dom.{document, html}
import scala.annotation.tailrec
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

object DomUtil extends DomUtil


trait DomUtil {

  /**
   * Returns the `&lt;div&gt;` with the id 'container' or, if no element with this
   * name exists, creates a `&lt;div&gt;` with this id and returns it.
   */
  def getContainer(theId: String = "container"): html.Div = {
    def grabContainer = Option(document.getElementById("container"))
    def createContainer = document.body.appendChild(div(id:=theId).render)
    grabContainer.getOrElse(createContainer)
  }.asInstanceOf[html.Div]

//----------------------------------------------------------

  /// 1. imperative version - the opposite of elegant
  /*def replaceChild[N <: dom.Node](container: N, newElmId: String): Unit = {
    val droppedElm = document.getElementById(newElmId)
    if (droppedElm != null) {
      val srcContainer = droppedElm.parentNode
      if (container.hasChildNodes) {
        val droppedOnElm = container.firstElementChild
        srcContainer.appendChild(droppedOnElm)
      }
      container.appendChild(droppedElm)
    }
  }*/

  /*/// 2. naive version using Options - quite similar
  def replaceChild[N <: dom.Node](container: N, newElmId: String): Unit = {
    val droppedElm = Option(document.getElementById(newElmId))
    if (droppedElm.isDefined) {
      val srcContainer = droppedElm.get.parentNode
      val droppedOnElm = Option(container.firstElementChild)
      if (droppedOnElm.isDefined) {
        srcContainer.appendChild(droppedOnElm.get)
      }
      container.appendChild(droppedElm.get)
    }
  }*/

  /// 3. functional version using for comprehension - very readable
  /** Replaces the first child of the target container with the specified element. */
  def replaceChild[N <: dom.Node](container: N, newElmId: String): Unit =
    for {
      droppedElm <- Option(document.getElementById(newElmId))
      srcContainer = droppedElm.parentNode
      droppedOnElm = Option(container.firstChild)
    } yield {
      droppedOnElm.foreach(srcContainer.appendChild)
      container.appendChild(droppedElm)
    }

  /*/// 4. strictly functional version - very concise but harder to read
  def replaceChild(tgtContainer: html.Div, newElmId: String): Unit =
    Option(document.getElementById(newElmId)).foreach( droppedElm => {
      val srcContainer = droppedElm.parentNode
      Option(tgtContainer.firstElementChild).foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    })*/

  //----------------------------------------------------------

  /** Replaces all children of the target container with the specified element. */
  def replaceChildrenWithElm[N <: dom.Node](container: N, newElm: N): Unit = {
    @tailrec def removeChildren(): Unit = Option(container.firstChild) match {
      case Some(child) =>
        container.removeChild(child)
        removeChildren()
      case None => ()
    }
    removeChildren()
    container.appendChild(newElm)
  }

  //----------------------------------------------------------

  /** Replaces all children of the target container with the specified elements. */
  def replaceChildren[N <: dom.Node](container: N)(newElms: TypedTag[html.Element]*): Unit =
    replaceChildrenWithElm(container, newElms.render.asInstanceOf[html.Element])

  //----------------------------------------------------------
}
