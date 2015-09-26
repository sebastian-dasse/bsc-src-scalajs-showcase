package showcase.example8

import org.scalajs.dom
import org.scalajs.dom.{console, html}
import org.scalajs.dom.ext.{Ajax, KeyCode}
import scala.scalajs.js
import scala.util.{Failure, Success, Try}
import scalatags.JsDom.all._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.async.Async.{async, await} /* seems occasionally not to work well with the workbench? */

import showcase.Showcase
import Utils._

/*
/// LESSONS LEARNED

/// version 1: a Seq of tuples works straight ahead with the for-yield-syntax, to generate a Seq of Scalatags tags
val currencies: Seq[(String, String)] = Seq(
  ("EUR", "Euro"),
  ("USD", "US Dollar")
)
val tagSeq: Seq[TypedTag[html.Option]] = for ((code, name) <- currencies) yield option(
  code
)

/// version 2: when using a Map, it needs to be converted to a Seq, to get the for-yield-syntax to generate a Seq of Scalatags tags
val currencies: Map[String, String] = Map(
  "EUR" -> "Euro",
  "USD" -> "US Dollar"
)
val tagSeq: Seq[TypedTag[html.Option]] = for ((code, name) <- currencies.toSeq) yield option(
  code  //                                                               =====
)

//----------

/// not available for Scala.js:
val formatter = java.text.NumberFormat.getInstance()
def formatCurr(d: Double): String = formatter.format(d)

/// solution (but not for more sophisticated formatting, like so: 1.000.000,00 EUR):
def formatCurr(d: Double): String = "%.4f".format(d)

//----------

/// nice: could use the JavaScript console.assert() method - but might not be that important in this very case
org.scalajs.dom.console.assert(
  json.query.results.rate.id == srcCurr + dstCurr,
  "the rate id of the API did not match your currency combination",
  json.query.results.rate.id, s"$srcCurr$dstCurr"
)
Scala.Predef.assert(json.query.results.rate.id.toString == converter.srcCurr + converter.dstCurr)

//----------

import js.JSStringOps._
val str = "foo,bar,baz"
val res = str.jsReplace("/\./g", ".")
*/


/*
 * see:
 * - http://lihaoyi.github.io/hands-on-scala-js/#UsingWebServices
 * - http://lihaoyi.github.io/hands-on-scala-js/#dom.extensions
 */
object ShowcaseAjax extends Showcase {

  def setupUi(container: html.Element): Unit = {

    var converter = new Converter()

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

    def updateDstAmount(): Unit = {
//    updateConversionRate1()
      updateConversionRate2()
//      updateConversionRate3()
    }


    def url = "http://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.xchange where pair in ('" +
      converter.srcCurr + converter.dstCurr + "')&format=json&env=store://datatables.org/alltableswithkeys"

    /// version 1: with pure JavaScript - org.scalajs.dom.XMLHttpRequest
    def updateConversionRate1(): Unit = {
      val xhr = new dom.XMLHttpRequest()
      xhr.open("GET", url)
      xhr.onload = (e: dom.Event) => {
        if (xhr.status == 200) {
          handleXhrResponse(xhr)
        }
      }
      xhr.send()
    }

    /// version 2: with futures - org.scalajs.dom.ext.Ajax
    def updateConversionRate2(): Unit = Ajax.get(url).onSuccess{
      case xhr => handleXhrResponse(xhr)
    }

    /// version 3: with async-await - org.scalajs.dom.ext.Ajax and scala.async.Async.{async, await}
    ///   this way one can write imperative style for a asynchronous control flow
    def updateConversionRate3(): Unit = async{
      val xhr = await{ Ajax.get(url) }
      handleXhrResponse(xhr)
    }

    def handleXhrResponse(xhr: dom.XMLHttpRequest): Unit = {
      // xhr.responseText could be null, but only if the request was unsuccessful or not yet been sent
      //   -->  https://developer.mozilla.org/de/docs/Web/API/XMLHttpRequest
      val json: js.Dynamic = js.JSON.parse(xhr.responseText)
      assert(json.query.results.rate.id.toString == converter.srcCurr + converter.dstCurr)
      converter.rate = json.query.results.rate.Rate.toString.toDouble
//      outputAmount.textContent = format(converter.dstAmount)
      outputAmount.textContent = format(converter.convert(srcAmount))
      updateDstAmount()
    }


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
        for ((code, name) <- Converter.currencies.toSeq) yield option(
          id:=s"$theId-opt-${code.toLowerCase}",
          value:=code, name
        )
//        ,onchange:={ () => callback(sel.value) } // does not work: sel.value can not be referenced before the select was rendered
      ).render
      sel.onchange = (e: dom.Event) => callback(sel.value)
      sel.value = defaultOption
      sel
    }

    val selSrcCurr = setupCurrencySelect("sel-src-curr", (c: String) => converter.srcCurr = c)
    val selDstCurr = setupCurrencySelect("sel-dst-curr", (c: String) => converter.dstCurr = c)

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
