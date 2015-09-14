package showcase

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.{console, html}
import scala.collection.parallel.mutable
import scalatags.JsDom.all._


object Showcase4InputForms {
  def apply(container: html.Element): Unit = setupUi(container)

  def setupUi(container: html.Element): Unit = {

    val txtInput = input(
      id:="txt-input",
      onfocus:={ (ev: dom.MouseEvent) => () },
      placeholder:="Your input here...",
      width:=300.px,
      height:=1.em,
      fontSize:="2.em",
      fontFamily:="Helvetica, Arial, sans-serif"
    ).render

    val txtOutput = textarea(
      id:="txt-output",
      width:=300.px,
//      cols:=30,
      rows:=10,
      readonly,
      style:="resize:none"
    ).render

    def numLines =
      if (txtOutput.value.isEmpty) 0
      else txtOutput.value.count(_ == '\n') + 1
    def numWords =
      if (txtOutput.value.isEmpty) 0
      else txtOutput.value.split("\\s+").length
    def numLetters = txtOutput.value.count(!_.isWhitespace)
    def numChars = txtOutput.value.length

    val linesCount = span(0).render
    val wordsCount= span(0).render
    val lettersCount = span(0).render
    val charsCount = span(0).render

    def autoNewline(str: String) = if (txtOutput.value.isEmpty) str else "\n" + str

    def updateOutput() = {
      txtOutput.value += autoNewline(txtInput.value)
      txtInput.value = ""
      linesCount.textContent = numLines.toString
      wordsCount.textContent = numWords.toString
      lettersCount.textContent = numLetters.toString
      charsCount.textContent = numChars.toString
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

    def colorFlash(elm: html.Element, color: String = "yellow", timeout: Double = 250): Unit = {
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

    val exampleMap = Map(
      "Alice" -> "Alaska",
      "Bob" -> "Brunei",
      "Carla" -> "Costa Rica"
    )


    container.appendChild(
      div(
        div(
          button(
            id:="btn-foo",
//            `class`:="btn",
            cls:="btn",
            "Foo",
            onclick:={ (ev: dom.MouseEvent) => { console.log("foo clicked!", ev)} }
          ),
          input(
            id:="btn-bar",
            cls:="btn",
//            `type`:="button",
            tpe:="button",
            value:="Bar",
            onclick:={ () => {
              println("bar clicked!!!")
              dom.alert("foo")
            } }
          )
        ),
        div(
          txtInput,
          button(
            id:="btn-enter",
            cls:="btn",
            "Enter",
            onclick:={ () => updateOutput }
          ),
          button(
            id:="btn-clear",
            cls:="btn",
            "Clear",
            onclick:={ () => {
              clearOutput
              colorFlash(txtOutput, "rgb(230, 230, 230)")
            } }
          )
        ),
        txtOutput,

        div(
          id:="txt-analysis",
//          if (numLines > 2) p("oho!!!") else p(""),
          p("lines: ", linesCount),
          p("words: ", wordsCount),
          p("letters: ", lettersCount),
          p("characters: ", charsCount)
        ),

        hr,

        div(
          id:="list-example",
          ul(
            (for {(name, destination) <- exampleMap} yield li(
              b(name), s" wants to travel to $destination"
            )).toSeq
          )
        )
      ).render
    )
  }
}
