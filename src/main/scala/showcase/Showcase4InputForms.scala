package showcase

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.ext.Color
import org.scalajs.dom.{console, html}
import scala.scalajs.js
import scalatags.JsDom.all._
import scala.language.implicitConversions

// TODO refactor
object Showcase4InputForms {
  def apply(container: html.Element): Unit = setupUi(container)


  def setupUi(container: html.Element): Unit = {

    val defaultCanvasHeight = 150

    val colCanvasBar = Color(  0,  51, 204) // dark blue
    val colFlash     = Color(242, 242, 242) // light grey

    implicit def color2JsAny(col: Color): js.Any = col.toString


    val txtInput = input(
      id:="txt-input",
      tpe:="text",
      onfocus:={ (ev: dom.MouseEvent) => () },
      placeholder:="Your input here..."
    ).render

    val txtOutput = textarea(
      id:="txt-output",
//      cols:=30,
      rows:=10,
      readonly//,
//      style:="resize:none"
    ).render

    def numLines =
      if (txtOutput.value.isEmpty) 0
      else txtOutput.value.count(_ == '\n') + 1

    def numWords =
      if (txtOutput.value.isEmpty) 0
      else txtOutput.value.split("\\s+").length

    def numLetters = txtOutput.value.count(!_.isWhitespace)

    def numChars = txtOutput.value.length

    def freqChars = {
      val chs = txtOutput.value.toSeq.filter(!_.isWhitespace).map(_.toLower)
      chs.foldLeft(scala.collection.mutable.Map[Char, Int]())(
        (chFreqs, ch) => { chFreqs += (ch -> (chFreqs.getOrElse(ch, 0) + 1)) }
      ).toSeq
    }

    sealed trait MyOrdering extends Ordering[(Char, Int)]
    case object CharsAsc extends MyOrdering {
      override def compare(x: (Char, Int), y: (Char, Int)) = x._1 compare y._1
    }
    case object CharsDesc extends MyOrdering {
      override def compare(x: (Char, Int), y: (Char, Int)) = y._1 compare x._1
    }
    case object FreqAsc extends MyOrdering {
      override def compare(x: (Char, Int), y: (Char, Int)) = x._2 compare y._2
    }
    case object FreqDesc extends MyOrdering {
      override def compare(x: (Char, Int), y: (Char, Int)) = y._2 compare x._2
    }

//    def freqCharsSortedBy(order: Order) = {
//      def Desc[T : Ordering] = implicitly[Ordering[T]].reverse
//      order match {
//        case CharsAsc => freqChars.sortBy(_._1)
//        case CharsDesc => freqChars.sortBy(_._1)(Desc)
//        case FreqAsc => freqChars.sortBy(_._2)
//        case FreqAsc => freqChars.sortBy(_._2)(Desc)
//      }
//    }

//    def freqCharsSortedBy(order: Order) = freqChars.sortWith(order.apply)
    def freqCharsSortedBy(order: MyOrdering) = freqChars.sorted(order)


//    var tableOrdering: Order = CharsAsc
    var tableOrdering: MyOrdering = CharsAsc

    def freqCharsSorted = freqCharsSortedBy(tableOrdering)

    val linesCount = span(0).render
    val wordsCount = span(0).render
    val lettersCount = span(0).render
    val charsCount = span(0).render
    val lettersTotal = td(0).render
    val lettersFreq = tbody().render

    def autoNewline(str: String) = if (txtOutput.value.isEmpty) str else "\n" + str

    val theCanvas = canvas(id:="canvas-diagram").render

    def updateCanvas() = {
      val freqs = freqCharsSortedBy(FreqDesc)
      val max = if (freqs.isEmpty) 1 else freqs.head._2
      val num = freqs.length

      val cnvContainer = dom.document.getElementById("container-letters-diagram").asInstanceOf[html.Div]
      val canvasWidth = cnvContainer.clientWidth
      val canvasHeight = if (num <= 0) defaultCanvasHeight else cnvContainer.clientHeight - 3 // compensation for strange offset of 3px

      theCanvas.width = canvasWidth
      theCanvas.height = canvasHeight

      val pad: Int = 10
      val txtWidth = 15
      val xInc: Double = (canvasWidth - 5 * pad / 2 - txtWidth).toDouble / max
      val yInc: Double = (canvasHeight - 3 * pad / 2).toDouble / num
      val barHeight: Double = yInc - pad / 2
      val xPos: Int = pad
      var yPos: Double = pad

      val ctx = theCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
      ctx.clearRect(0, 0, canvasWidth, canvasHeight)
      ctx.fillStyle = colCanvasBar
      ctx.font = txtWidth.px + " Helvetica, Arial, sans-serif "
      ctx.textAlign = "left"
      ctx.textBaseline = "middle"


      for ((ch, fq) <- freqCharsSorted) {
        ctx.fillRect(xPos.toInt, yPos.toInt, (fq * xInc).toInt, barHeight.toInt)
        ctx.fillText(ch.toString, (xPos + fq * xInc + pad / 2).toInt, (yPos + barHeight/2).toInt)
        yPos += yInc.toInt
        console.log("w: ", (xPos + fq * xInc + pad / 2).toInt, " fq: ", fq, " xInc: ", xInc)
      }
    }

    def updateAnalysisOutput() = {
      linesCount.textContent = numLines.toString
      wordsCount.textContent = numWords.toString
      lettersCount.textContent = numLetters.toString
      charsCount.textContent = numChars.toString

      lettersFreq.innerHTML = ""
      lettersFreq.appendChild((
        for ((ch, fq) <- freqCharsSorted) yield tr(
          td(ch.toString), td(fq)
        )
      ).render)
      lettersTotal.textContent = numLetters.toString

      updateCanvas()
    }

    def updateOutput() = {
      txtOutput.value += autoNewline(txtInput.value)
      txtInput.value = ""
      updateAnalysisOutput()
    }

    def clearOutput() = {
      txtOutput.value = ""
      updateOutput()
    }

    txtInput.onkeyup = (ev: dom.KeyboardEvent) => ev.keyCode match {
      case KeyCode.enter =>
        println("entered")
        updateOutput()
      case KeyCode.escape =>
        txtInput.value = ""
      case _ => ()
    }

    var colorBackup = scala.collection.mutable.Map[String, String]()

    def colorFlash(elm: html.Element, color: Color = Color("Yellow"), timeout: Double = 250): Unit = {
      def backup = {
        val col = elm.style.background
        colorBackup += (elm.id -> col)
        col
      }
      val originalColor = colorBackup.getOrElse(elm.id, backup)

      elm.style.background = color
      dom.setTimeout({ () =>
        elm.style.background = originalColor
      }, timeout)
    }


    val theTable = table(
      id:="tbl-letters",
      cls:="my-tbl-sortable",
      thead(
        tr(
          th("Letter",
            onclick:={ () => {
              tableOrdering = if (tableOrdering == CharsAsc) CharsDesc else CharsAsc
              updateAnalysisOutput()
            } }
          ),
          th("Frequency",
            onclick:={ () => {
              tableOrdering = if (tableOrdering == FreqDesc) FreqAsc else FreqDesc
              updateAnalysisOutput()
            } }
          )
        )
      ),
      tfoot(tr(td("total"), lettersTotal)),
      lettersFreq
    )


//    val exampleMap = Map(
//      "Alice" -> "Alaska",
//      "Bob" -> "Brunei",
//      "Carla" -> "Costa Rica"
//    )


    container.appendChild(
      div(id:="container-toplevel",
        div(
          button(
            id:="btn-foo",
//            `class`:="btn",
            cls:="my-btn",
            "Foo",
            onclick:={ (ev: dom.MouseEvent) => { console.log("foo clicked!", ev)} }
          ),
          input(
            id:="btn-bar",
            cls:="my-btn",
//            `type`:="button",
            tpe:="button",
            value:="Bar",
            onclick:={ () => {
              println("bar clicked!!!")
              dom.alert("foo")
            } }
          )
        ),

        hr,

        div(
          id:="container-input",
          txtInput,
          div(
            id:="container-btns",
              button(
              id:="btn-enter",
              cls:="input-btn",
              "Enter",
              onclick:={ () => updateOutput }
            )
          )
        ),

        div(
          id:="container-output",
          txtOutput,
          button(
            id:="btn-clear",
            cls:="my-btn",
            "Clear",
            onclick:={ () => {
              clearOutput
              colorFlash(txtOutput, colFlash)
            } }
          )
        ),

        div(
          id:="container-analysis",
          div(id:="container-anaylsis-text",
            p("lines: ", linesCount),
            p("words: ", wordsCount),
            p("letters: ", lettersCount),
            p("characters: ", charsCount)
          ),

          div(
            id:="container-analysis-letters",
            theTable,
            div(
              id:="container-letters-diagram",
              theCanvas
            )
          )
        )

//        ,
//        hr,
//
//        div(
//          id:="list-example",
//          ul(
//            (for {(name, destination) <- exampleMap} yield li(
//              b(name), s" wants to travel to $destination"
//            )).toSeq
//          )
//        )
      ).render
    )
  }
}
