package showcase.example8

import scala.collection.SortedMap
import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scala.scalajs.js
import org.scalajs.dom.ext.KeyCode
import scalatags.JsDom.all._
//import scalatags.JsDom.TypedTag
//import upickle.default._

import showcase.Showcase

object ShowcaseAjax extends Showcase {
  def setupUi(container: html.Element): Unit = {

    val currencies: SortedMap[String, String] = SortedMap(
      "EUR" -> "Euro",
      "USD" -> "US Dollar"
    )
//    val currencies: Seq[(String, String)] = Seq(
//      "EUR" -> "Euro",
//      "USD" -> "US Dollar"
//    )
    var srcCurr = "EUR"
    var dstCurr = "EUR"
//    var srcAmnt = 1.0

    val currFormat = "%12.4f"

    val Numeric = """[+-]?[0-9]+([,.][0-9]*)?""".r

    def toDouble(str: String): Double =
      Numeric.findFirstIn(str).head.replaceAll(",", ".").toDouble

    val inputAmount = input(
      id:="inp-src-amount",
      placeholder:="Your amount here...",
//      f"$srcAmnt%10.2f"
      value:=currFormat.format(1.0)//,
//      onfocus:={ () =>  }
    ).render

    inputAmount.onfocus = (evt: dom.Event) => {
      inputAmount.select()
    }

    inputAmount.onmouseup = (evt: dom.Event) => {
      evt.preventDefault()
    }

    val outputAmount = textarea(
      id:="out-dst-amount",
      //      f"$dstAmnt%10.2f"
      currFormat.format(1.0)
    ).render


    def queryUrl(srcCurr: String, dstCurr: String) =
      "http://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.xchange where pair in ('" +
        srcCurr + dstCurr + "')&format=json&env=store://datatables.org/alltableswithkeys"

    def doGet(srcCurr: String, dstCurr: String): Unit = {
      val url = queryUrl(srcCurr, dstCurr)

      val xhr = new dom.XMLHttpRequest()
      xhr.open("GET", url)
      xhr.onload = (e: dom.Event) => {
        if (xhr.status == 200) {
          console.log(xhr.response)
          console.log(xhr.responseText)
          console.log("---->")
          val json: js.Dynamic = js.JSON.parse(xhr.responseText)
          assert(json.query.results.rate.id == srcCurr + dstCurr)

          val rate = json.query.results.rate.Rate.asInstanceOf[String].toDouble

          println(rate.toString)

          val res = srcAmnt * rate
          //          dstAmnt = f"$res%10.2f"

          println(res.toString)

          setOutputAmount(res)

          outputAmount.value = f"$res%10.2f"

          // TODO: field for the output rate
          //          val rate = f"$res%10.2f"
        }
      }
      xhr.send()
    }

    def calculate(): Unit = {
      doGet(srcCurr, dstCurr)
    }



    def srcAmnt = toDouble(inputAmount.value)

    def setInputAmount(a: Double) =
      inputAmount.value = currFormat.format(a)
    def setOutputAmount(a: Double) =
      outputAmount.value = currFormat.format(a)

    inputAmount.onkeyup = (evt: dom.KeyboardEvent) => evt.keyCode match {
      case KeyCode.enter => {
        setInputAmount(srcAmnt)
        calculate()
      }
      case KeyCode.escape => setInputAmount(0)
      case _ => ()
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



    def setupCurrencySelect(theId: String, callback: String => Unit) = {
      val sel = select(
        id:=theId,
        for ((code, name) <- currencies.toSeq) yield option(
          id:=s"opt-${code.toLowerCase}",
          value:=code, name
        )
      ).render
      sel.onchange = (e: dom.Event) => {

        println(sel.value)

        callback(sel.value)

        println(srcCurr + " -> " + dstCurr)
      }
      sel
    }

    def updateSrcCurr(c: String): Unit = srcCurr = c

    val selSrcCurr = setupCurrencySelect("sel-src-curr", (c: String) => srcCurr = c)
    val selDstCurr = setupCurrencySelect("sel-dst-curr", (c: String) => dstCurr = c)

//    def selectedOption(sel: html.Select): String =
//      sel.options(sel.selectedIndex).value

//    val srcCurr = selectedOption(selSrcCurr)
//    val dstCurr = selectedOption(selDstCurr)

//    println(srcCurr)
//    println(dstCurr)



    container.appendChild(
      div(
        h1("Scala.js Showcase 8: AJAX!"),
        inputAmount,
        selSrcCurr,
        outputAmount,
        selDstCurr,
        button(
          id:="btn-do-get",
          cls:="input-btn",
          "GET",
          onclick:={ () => calculate() }
        )
      ).render
    )
  }
}
