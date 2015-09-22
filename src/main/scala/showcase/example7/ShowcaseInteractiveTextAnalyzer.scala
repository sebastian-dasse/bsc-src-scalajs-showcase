package showcase.example7

import org.scalajs.dom
import org.scalajs.dom.ext.{Color, KeyCode}
import org.scalajs.dom.{console, html}
import scala.scalajs.js
import scalatags.JsDom.all._

import showcase.Showcase
import showcase.example7.TableOrdering._
import StringAnalysis._


/* Note: the word analyzer ignores non-UTF characters such as German umlauts etc. But the letter */
object ShowcaseInteractiveTextAnalyzer extends Showcase {

  object Settings {
    import scala.language.implicitConversions

    val defaultCanvasHeight = 150

    val colCanvasBar  = Color(  0,  51, 204) // dark blue
    val colColorFlash = Color(242, 242, 242) // light grey

    implicit def color2JsAny(col: Color): js.Any = col.toString
  }


  def setupUi(container: html.Element): Unit = {
    import Settings._

    val txtInput = input(
      id:="txt-input",
      tpe:="text",
      onfocus:={ (ev: dom.MouseEvent) => () },
      placeholder:="Your input here..."
    ).render

    val txtOutput = textarea(id:="txt-output").render

    var theText = new AnalyzableString(txtOutput.value)

    var tableOrdering: TableOrdering = CharsAsc

    def freqCharsSortedBy(order: TableOrdering) = theText.freqChars.toSeq.sorted(order)

    def freqCharsSorted = freqCharsSortedBy(tableOrdering)

    // TODO use several Orderings a la freqCharsSorted
    def freqWordsSorted = theText.freqWords.toIndexedSeq.sorted

    val linesCount = span(0).render
    val wordsCount = span(0).render
    val lettersCount = span(0).render
    val charsCount = span(0).render
    val lettersTotal = td(0).render
    val lettersFreq = tbody().render
    val wordsTotal = td(0).render
    val wordsFreq = tbody().render

    def autoNewline(str: String) = if (txtOutput.value.isEmpty) str else "\n" + str

    val theCanvas = canvas(id:="canvas-diagram").render

    val lettersDiagram = div(
      id:="container-letters-diagram",
      theCanvas
    ).render

    def updateCanvas() = {
      val freqs = freqCharsSortedBy(FreqDesc)
      val max = if (freqs.isEmpty) 1 else freqs.head._2
      val num = freqs.length

//      val cnvContainer = dom.document.getElementById("container-letters-diagram").asInstanceOf[html.Div]
      val cnvContainer = lettersDiagram
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
      }
    }

    def updateAnalysisOutput() = {
      linesCount.textContent = theText.numLines.toString
      wordsCount.textContent = theText.numWords.toString
      lettersCount.textContent = theText.numLetters.toString
      charsCount.textContent = theText.numChars.toString

      lettersFreq.innerHTML = ""
      lettersFreq.appendChild((
        for ((chr, freqency) <- freqCharsSorted) yield tr(
          td(chr.toString), td(freqency)
        )
      ).render)
      lettersTotal.textContent = theText.numLetters.toString

      wordsFreq.innerHTML = ""
      wordsFreq.appendChild((
        for ((word, frequency) <- freqWordsSorted) yield tr(
          td(word), td(frequency)
        )
      ).render)
      wordsTotal.textContent = theText.numWords.toString

      updateCanvas()
    }

    def updateOutput() = {
      txtOutput.value += autoNewline(txtInput.value)
      txtInput.value = ""
      theText = txtOutput.value
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
    
    val colorFlash = new ColorFlash(txtOutput)

    val lettersTable = table(
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

    val wordsTable = table(
      id:="tbl-words",
      cls:="my-tbl-sortable",
      thead(
        tr(
          th("Word"/*,
            onclick:={ () => {
              tableOrdering = if (tableOrdering == CharsAsc) CharsDesc else CharsAsc
              updateAnalysisOutput()
            } }*/
          ),
          th("Frequency"/*,
            onclick:={ () => {
              tableOrdering = if (tableOrdering == FreqDesc) FreqAsc else FreqDesc
              updateAnalysisOutput()
            } }*/
          )
        )
      ),
      tfoot(tr(td("total"), wordsTotal)),
      wordsFreq
    )

    container.appendChild(
      div(
        id:="container-toplevel",

        h1("A Simple Text Analyzer"),

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
//              colorFlash(txtOutput, colFlash)
              colorFlash.flash(colColorFlash)
            } }
          )
        ),

        div(
          id:="container-analysis",
          div(id:="container-anaylsis-text",
            table(id:="tbl-text",
              tbody(
                tr(
                  th("lines:"), td(linesCount),
                  th("words:"), td(wordsCount),
                  th("letters:"), td(lettersCount),
                  th("characters:"), td(charsCount)
                )
              )
            )
          ),
          div(
            id:="container-analysis-letters",
            lettersTable,
            lettersDiagram
          ),
          div(
            id:="container-analysis-words",
            wordsTable
          )
        )
      ).render
    )
  }

}
