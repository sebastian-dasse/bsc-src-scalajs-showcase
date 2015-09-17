package showcase.example4

import org.scalajs.dom
import org.scalajs.dom.{console, html}
import showcase.Showcase

import scalatags.JsDom.all._


object Showcase4InputForms extends Showcase {
  def setupUi(container: html.Element): Unit = {

    // TODO: some form examples, a pseudo-login ...

    def form1(theId: String,
                   theClass: String = "",
                   title: String = "",
                   buttonText: String = "Button",
                   btnOnclick: (dom.MouseEvent) => Unit,
                   text: String) =
      div(
        id:=theId,
        cls:=theClass,
        if (!title.isEmpty) h3(title) else "",
        button(
          id:=s"btn-${buttonText.take(10).toLowerCase}",
          cls:="my-btn",
          buttonText,
          onclick:=btnOnclick
        ),
        p(text)
      )

    container.appendChild(
      div(
        h1("Scala.js Showcase 4"),

        button(
          id:="btn-foo",
          //          `class`:="my-btn",
          cls:="my-btn",
          "Foo",
          onclick:={ (ev: dom.MouseEvent) => { console.log("foo clicked!", ev)} }
        ),
        input(
          id:="btn-bar",
          cls:="my-btn",
          //          `type`:="button",
          tpe:="button",
          value:="Bar",
          onclick:={ () => {
            println("bar clicked!!!")
            dom.alert("foo")
          } }
        ),

        hr,

        div(
          id:="form-1",
          cls:="my-form",
          h3("Form 1"),
          button(
            id:=s"btn-${"Click me!".take(10).toLowerCase}",
            cls:="my-btn",
            "Click me!",
            onclick:={ () => console.log("Form 1 clicked") }
          ),
          p("foo bar baz")
        ),

        /// the same as above but extracted to own def
        form1(
          theId = "form-2",
          theClass = "my-form",
          title = "Form 2",
          buttonText = "Click me!",
          btnOnclick = { (_) => console.log("Form 2 clicked") },
          text = "foo bar baz"
        ),

        form1(
          theId = "form-3",
          theClass = "my-form",
          title = "Form 3",
          buttonText = "No, click me!",
          btnOnclick = { (_) => console.log("Form 3 clicked") },
          text = "lorem ipsum"
        ),

        form1(
          theId = "form-4",
//          theClass = "my-form",
//          title = "Form 4",
          buttonText = "Well, just pick one of us and click already.",
          btnOnclick = { (_) => console.log("Form 4 clicked") },
          text = "Sur le pont d'Avignon L'on y danse ..."
        )

      ).render
    )
  }
}
