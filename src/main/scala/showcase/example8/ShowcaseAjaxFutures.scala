package showcase.example8

import scala.collection.SortedMap
import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scala.scalajs.js
import org.scalajs.dom.ext.{Ajax, KeyCode}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.async.Async.{async, await} /* seems not to work well with the workbench */

//import scala.util.Try
import scalatags.JsDom.all._
//import scalatags.JsDom.TypedTag
//import upickle.default._

import showcase.Showcase

/*
 * see:
 * - http://lihaoyi.github.io/hands-on-scala-js/#UsingWebServices
 * - http://lihaoyi.github.io/hands-on-scala-js/#dom.extensions
 */
object ShowcaseAjaxFutures extends Showcase {

  object Converter {
    val currencies: SortedMap[String, String] = SortedMap(
      "EUR" -> "Euro",
      "CHF" -> "Swiss Franc",
      "USD" -> "US Dollar",
      "AUD" -> "Australian Dollar",
      "BGN" -> "Bulgarian Lev",
      "BRL" -> "Brazilian Real",
      "CAD" -> "Canadian Dollar",
      "CNY" -> "Chinese Renminbi",
      "CZK" -> "Czech Koruna",
      "DKK" -> "Danish Krone",
      "GBP" -> "British Pound",
      "HRK" -> "Croatian Kuna",
      "HKD" -> "Honk Kong Dollar",
      "HUF" -> "Hungarian Forint",
      "IDR" -> "Indonesian Rupiah",
      "ILS" -> "Israli Shekel",
      "ISK" -> "Islandic Krona",
      "INR" -> "Indian Rupee",
      "JPY" -> "Japanase Yen",
      "KRW" -> "South Korean Won",
      "LTL" -> "Lithuanian Litas",
      "LVL" -> "Latvian Lat",
      "MXN" -> "Mexican Peso",
      "MYR" -> "Malaysian Ringgit",
      "NZD" -> "New Zealand Dollar",
      "NOK" -> "Norwegian Krone",
      "PHP" -> "Philippine Peso",
      "PLN" -> "Polish Zloty",
      "RON" -> "Romanian New Leu",
      "RUB" -> "Russian Ruble",
      "SEK" -> "Swedish Krona",
      "SGD" -> "Singapore Dollar",
      "THB" -> "Thai Baht",
      "TRY" -> "Turkish Lira",
      "ZAR" -> "South African Rand"
    )
  }
  case class Converter(
    var srcCurr: String = "EUR",
    var dstCurr: String = "EUR",
    var srcAmount: Double = 1.0,
    var rate: Double = 1.0
  ) {
    def dstAmount: Double = srcAmount * rate
  }


  val Numeric = """[+-]?[0-9]+([.,][0-9]*)?""".r

  def toDouble(str: String): Double =
    Numeric.findFirstIn(str).getOrElse("0").replaceAll(",", ".").toDouble

  // TODO format like that: "1.000.000,0000 EUR"
  def formatCurr(d: Double): String = "%.4f".format(d)


  def setupUi(container: html.Element): Unit = {
    val converter = new Converter

    val inputAmount = input(
      id := "inp-src-amount",
      placeholder:="Your amount here...",
      value:=formatCurr(converter.srcAmount)
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
      formatCurr(converter.dstAmount)
    ).render


    /// version 1: with futures - org.scalajs.dom.ext.Ajax
    /*def updateConversionRate(): Unit = {
      val url = "http://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.xchange where pair in ('" +
          converter.srcCurr + converter.dstCurr + "')&format=json&env=store://datatables.org/alltableswithkeys"

      val f = Ajax.get(url)
      f.onSuccess{ case xhr =>
        val json: js.Dynamic = js.JSON.parse(xhr.responseText)
        assert(json.query.results.rate.id.toString == converter.srcCurr + converter.dstCurr)
        converter.rate = json.query.results.rate.Rate.toString.toDouble
        outputAmount.textContent = formatCurr(converter.dstAmount)
      }
      f.onFailure{ case exc => console.error(exc.toString) }
    }*/


    /// version 2: with async await - org.scalajs.dom.ext.Ajax and scala.async.Async.{async, await}
    def updateConversionRate(): Unit = async{
      val url = "http://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.xchange where pair in ('" +
        converter.srcCurr + converter.dstCurr + "')&format=json&env=store://datatables.org/alltableswithkeys"

      val xhr = await{ Ajax.get(url) }
      val json: js.Dynamic = js.JSON.parse(xhr.responseText)
      assert(json.query.results.rate.id.toString == converter.srcCurr + converter.dstCurr)
      converter.rate = json.query.results.rate.Rate.toString.toDouble
      outputAmount.textContent = formatCurr(converter.dstAmount)
    }



    inputAmount.onkeyup = (evt: dom.KeyboardEvent) => evt.keyCode match {
      case KeyCode.enter => {
        converter.srcAmount = toDouble(inputAmount.value)
        inputAmount.value = formatCurr(converter.srcAmount)
        updateConversionRate()
      }
      case KeyCode.escape =>
        inputAmount.value = formatCurr(1)
        inputAmount.select()
      case _ => ()
    }

    inputAmount.onblur = (evt: dom.Event) => {
      converter.srcAmount = toDouble(inputAmount.value)
      inputAmount.value = formatCurr(converter.srcAmount)
    }

    def setupCurrencySelect(theId: String, callback: String => Unit, defaultOption: String = "EUR"): html.Select = {
      val sel = select(
        id:=theId,
        for ((code, name) <- Converter.currencies.toSeq) yield option(
          id:=s"$theId-opt-${code.toLowerCase}",
          value:=code, name
        )
      ).render
      sel.onchange = (e: dom.Event) => callback(sel.value)
      sel.value = defaultOption
      sel
    }

    val selSrcCurr = setupCurrencySelect("sel-src-curr", (c: String) => converter.srcCurr = c) // TODO geht das noch besser
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
              onclick:={ () => updateConversionRate() }
          )
        )
      ).render
    )
  }
}
