package showcase.example8

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.ext.KeyCode
import scalatags.JsDom.all._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow


import showcase.Showcase
import Utils._

/*
 * see:
 * - http://lihaoyi.github.io/hands-on-scala-js/#UsingWebServices
 * - http://lihaoyi.github.io/hands-on-scala-js/#dom.extensions
 */
object ShowcaseAjax2 extends Showcase {

  def setupUi(container: html.Element): Unit = {

    var converter = Converter2()

    val inputAmount = input(
      id := "inp-src-amount",
      placeholder:="Your amount here...",
      value:=format(1)
    ).render

    inputAmount.onfocus = (evt: dom.Event) => {
      inputAmount.select()
    }

    inputAmount.onmouseup = (evt: dom.Event) => {
      evt.preventDefault()
    }

    val outputAmount = div(
      id:="out-dst-amount",
      disabled,
      format(1)
    ).render

    def srcAmount = inputAmount.value.asDouble
    def updateSrcAmount(d: Double) = inputAmount.value = format(d)

    /// version 1: with futures
    def updateDstAmount(): Unit = converter.convert(srcAmount).onSuccess{
      case dstAmount => outputAmount.textContent = format(dstAmount)
    }

    /// alternative 2: with async-await
//    import scala.async.Async.{async, await}
//    def updateDstAmount(): Unit = async {
//      val dstAmount = await{ converter.convert(srcAmount) }
//      outputAmount.textContent = format(dstAmount)
//    }

    inputAmount.onkeyup = (evt: dom.KeyboardEvent) => evt.keyCode match {
      case KeyCode.enter => {
        updateSrcAmount(srcAmount)
        updateDstAmount()
      }
      case KeyCode.escape =>
        updateSrcAmount(1)
        inputAmount.select()
      case _ => ()
    }

    inputAmount.onblur = (evt: dom.Event) => {
      updateSrcAmount(srcAmount)
    }

    def setupCurrencySelect(theId: String, callback: String => Unit, defaultOption: String = "EUR"): html.Select = {
      val sel = select(
        id:=theId,
        for ((code, name) <- Converter2.currencies.toSeq) yield option(
          id:=s"$theId-opt-${code.toLowerCase}",
          value:=code, name
        )
//        ,onchange:={ () => callback(sel.value) } // does not work: sel.value can not be referenced before the select was rendered
      ).render
      sel.onchange = (e: dom.Event) => callback(sel.value)
      sel.value = defaultOption
      sel
    }

    val selSrcCurr = setupCurrencySelect("sel-src-curr", (c: String) => converter = converter.copy(srcCurr = c))
    val selDstCurr = setupCurrencySelect("sel-dst-curr", (c: String) => converter = converter.copy(dstCurr = c))

    container.appendChild(
      div(
        h1("Scala.js Showcase 8: AJAX!"),
        form(
          id:="form-conv",
          onsubmit:={ (evt: dom.Event) => evt.preventDefault() },
          inputAmount,
          selSrcCurr,
          outputAmount,
          selDstCurr,
          button(
            id:="btn-do-get",
            cls:="input-btn",
            "Convert",
            onclick:={ () => updateDstAmount() }
          )
        )
      ).render
    )
  }
}
