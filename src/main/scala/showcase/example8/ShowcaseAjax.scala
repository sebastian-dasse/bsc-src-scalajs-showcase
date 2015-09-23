package showcase.example8

import scala.collection.SortedMap
import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scala.scalajs.js
import org.scalajs.dom.ext.KeyCode
//import scala.util.Try
import scalatags.JsDom.all._
//import scalatags.JsDom.TypedTag
//import upickle.default._

import showcase.Showcase


/*
 * see: http://lihaoyi.github.io/hands-on-scala-js/#UsingWebServices
 */
object ShowcaseAjax extends Showcase {

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
      "IDR" -> "Indonisian Rupiah",
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
  class Converter(var srcCurr: String = "EUR",
                  var dstCurr: String = "EUR",
                  var srcAmount: Double = 1.0,
                  var rate: Double = 1.0) {
    import Converter._

    def dstAmount: Double = srcAmount * rate

//    def srcAmountFormatted = currFormat.format(srcAmount)
//
//    def dstAmountFormatted = currFormat.format(dstAmount)
  }


  val Numeric = """[+-]?[0-9]+([.,][0-9]*)?""".r

  def toDouble(str: String): Double =
    Numeric.findFirstIn(str).getOrElse("0").replaceAll(",", ".").toDouble

  /// not available for Scala.js
//  private val formatter = java.text.NumberFormat.getInstance()
//  def formatCurr(d: Double): String = formatter.format(d)

  // TODO format like that: "1.000.000,0000 EUR"
  def formatCurr(d: Double): String = "%12.4f".format(d)


  def setupUi(container: html.Element): Unit = {

    val cnv = new Converter

    val inputAmount = input(
      id := "inp-src-amount",
      placeholder:="Your amount here...",
      value:=formatCurr(cnv.srcAmount)
    ).render

    inputAmount.onfocus = (evt: dom.Event) => {
      inputAmount.select()
    }

    inputAmount.onmouseup = (evt: dom.Event) => {
      evt.preventDefault()
    }

//    val outputAmount = textarea(
//    val outputAmount = input(
    val outputAmount = div(
      id:="out-dst-amount",
      disabled,
      formatCurr(cnv.dstAmount)
    ).render


    def queryUrl(srcCurr: String, dstCurr: String) =
      "http://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.xchange where pair in ('" +
        srcCurr + dstCurr + "')&format=json&env=store://datatables.org/alltableswithkeys"

    //    Try {

    def updateConversionRate(): Unit = {
      val url = queryUrl(cnv.srcCurr, cnv.dstCurr)

      val xhr = new dom.XMLHttpRequest()
      xhr.open("GET", url)
      xhr.onload = (e: dom.Event) => {
        if (xhr.status == 200) try {
          val json: js.Dynamic = js.JSON.parse(xhr.responseText) // xhr.responseText could be null
//          val json: js.Dynamic = js.JSON.parse(null) // provoke exception

          /// nice: could use the JavaScript console.assert() method - but might not be that important in this very case
//          console.assert(
//            json.query.results.rate.id == srcCurr + dstCurr,
//            "the rate id of the API did not match your currency combination",
//            json.query.results.rate.id, s"$srcCurr$dstCurr"
//          )

          assert(json.query.results.rate.id == cnv.srcCurr + cnv.dstCurr)

//          val rate = json.query.results.rate.Rate.asInstanceOf[String].toDouble
//          setOutputAmount(srcAmnt * rate)
          cnv.rate = json.query.results.rate.Rate.asInstanceOf[String].toDouble
          outputAmount.textContent = formatCurr(cnv.dstAmount)

          // TODO: field for the output rate
//          val rate = f"$res%10.2f"

        } catch {
          case exc: Throwable => console.error(exc.toString)
        }
      }
      xhr.send()
      console.info(xhr)
    }

    inputAmount.onkeyup = (evt: dom.KeyboardEvent) => evt.keyCode match {
      case KeyCode.enter => {
        cnv.srcAmount = toDouble(inputAmount.value)
        inputAmount.value = formatCurr(cnv.srcAmount)
        updateConversionRate()
      }
      case KeyCode.escape =>
        inputAmount.value = formatCurr(1)
      case _ => ()
    }
    inputAmount.onblur = (evt: dom.Event) => {
      cnv.srcAmount = toDouble(inputAmount.value)
      inputAmount.value = formatCurr(cnv.srcAmount)
    }



    /// version 1: a Seq of tuples works straight ahead with the for-yield-syntax, to generate a Seq of Scalatags tags
//    val currencies: Seq[(String, String)] = Seq(
//      ("EUR", "Euro"),
//      ("USD", "US Dollar")
//    )
//    val tagSeq: Seq[TypedTag[html.Option]] = for ((code, name) <- currencies) yield option(
//      code
//    )

    /// version 2: when using a Map, it needs to be converted to a Seq, to get the for-yield-syntax to generate a Seq of Scalatags tags
//    val currencies: Map[String, String] = Map(
//      "EUR" -> "Euro",
//      "USD" -> "US Dollar"
//    )
//    val tagSeq: Seq[TypedTag[html.Option]] = for ((code, name) <- currencies.toSeq) yield option(
//      code  //                                                               =====
//    )


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

//    def updateSrcCurr(c: String): Unit = srcCurr = c

    val selSrcCurr = setupCurrencySelect("sel-src-curr", (c: String) => cnv.srcCurr = c)
    val selDstCurr = setupCurrencySelect("sel-dst-curr", (c: String) => cnv.dstCurr = c)

    container.appendChild(
      div(
        h1("Scala.js Showcase 8: AJAX!"),
        form(
//        div(
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
            onclick:={ () => {
//              cnv.srcAmount = toDouble(inputAmount.value)
              updateConversionRate()
            } }
          )
        )
      ).render
    )
  }
}
