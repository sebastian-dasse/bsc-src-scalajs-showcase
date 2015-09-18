package showcase.example1

import org.scalajs.dom.html
import scalatags.JsDom.all._

import htmlib.ConvenientDiv._
import htmlib.TextBox._
import showcase.Showcase


/* Note on the htmllib: as an altvernative to importing all members of the object, the trait can be extended or with-ed like this: */
//import lib.html._
//object Showcase1TextBoxes extends Showcase with TextBox with ConvenientDiv {
object Showcase1TextBoxes extends Showcase {
  def setupUi(container: html.Element): Unit = {
    container.appendChild(
      div(
        maxWidth:=1000.px,
        margin:="auto",
        h1("Scala.js Showcase 1"),
        div(
          h3("foo bar baz"),
          box()("some stuff", p("some stuff")),
          importantBox("some stuff", p("some stuff")),
          greenBox("some stuff", p("some stuff")),
          codeBox("some stuff", p("some stuff")),
          importantBox("some stuff"),
          importantBox(
            span("some stuff"), br,
            "some stuff",
            p("some stuff")
          ),
          importantBox(
            h3("Foo"),
            p("some stuff and ..."),
            p("... some other stuff")
          ),
          p(
            "asfd lsf ksajf sdaflk sdf lksdfa assjflk sdlf sdal jsda jsdalf jsdf lsadf sa jsdaflk jdsaf lsdaf lsdf sdlaasd asd asd asd sad as asd asd asd das f jk " +
              "This could look like this: ",
            codeInline(
              """println("foo bar")"""
            ),
            " or else." +
              "ouieweqrouiweewrh wero iwq oiure ewqo ewqo ewr oeiwqur rewouirq ewqou rierwo riuer oiuweqrh uiwerou iwerqh "
          ),
          codeBox(
            em(
              """
                |/**
                | * Foo bar
                | */
              """.stripMargin
            ),
            """
              |if (true) {
              |    println("foo bar")
              |} else {
              |    // do something else
              |}
            """.stripMargin
          )
        )
      ).render
    )
  }
}
