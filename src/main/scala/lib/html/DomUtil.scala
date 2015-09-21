package lib.html

import org.scalajs.dom.{document, html}

import scalatags.JsDom.all._


object DomUtil extends DomUtil

trait DomUtil {
  def getContainer(theId: String = "container"): html.Div = {
    def grabContainer = Option(document.getElementById("container"))
    def createContainer = document.body.appendChild(div(id:=theId).render)
    grabContainer.getOrElse(createContainer)
  }.asInstanceOf[html.Div]

//----------------------------------------------------------

  /*/// 1. imperative version - the opposite of elegant
  def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit = {
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
  def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit = {
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
  def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit =
    for {
      droppedElm <- Option(document.getElementById(srcElmId))
      srcContainer = droppedElm.parentNode
      droppedOnElm = Option(tgtContainer.firstElementChild)
    } yield {
      droppedOnElm.foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    }

  /*/// 4. strictly functional version - very concise but harder to read
  def replaceChild(srcElmId: String, tgtContainer: html.Div): Unit =
    Option(document.getElementById(srcElmId)).foreach( droppedElm => {
      val srcContainer = droppedElm.parentNode
      Option(tgtContainer.firstElementChild).foreach(srcContainer.appendChild)
      tgtContainer.appendChild(droppedElm)
    })*/

  //----------------------------------------------------------
}
