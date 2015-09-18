package showcase.example4

import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scalatags.JsDom.all._
import scalatags.JsDom.tags2._

import showcase.Showcase


object Showcase6InputForms extends Showcase {
  def setupUi(container: html.Element): Unit = {

    // TODO: some form examples, a pseudo-login ...

    div()

    def form1(theId: String,
              classes: Seq[String] = Nil,
              title: String = "",
              buttonText: String = "Button",
              btnOnclick: (dom.MouseEvent) => Unit,
              text: String) =
      div(
        id:=theId,
        cls:="my-form " + classes.mkString(" "),
        if (!title.isEmpty) h3(title) else "",
        p(text),
        p(
          button(
            id:="btn-" + id,
            cls:="my-btn onhover-shrink",
            buttonText,
            onclick:=btnOnclick
          )
        )
      )

    container.appendChild(
      div(
        h1("Scala.js Showcase 4"),

        nav(
          button(
            id:="btn-foo",
            cls:="my-btn onhover-shrink",
            "Foo",
            onclick:={ (ev: dom.MouseEvent) => { console.log("foo clicked!", ev)} }
          ),
          button(
            id:="btn-foo",
            cls:="my-btn onhover-shrink",
            "Foo",
            onclick:={ (ev: dom.MouseEvent) => { console.log("foo clicked!", ev)} }
          ),
          button(
            id:="btn-foo",
            cls:="my-btn onhover-shrink",
            "Foo",
            onclick:={ (ev: dom.MouseEvent) => { console.log("foo clicked!", ev)} }
          ),
          input(
            id:="btn-bar",
            cls:="my-btn onhover-shrink",
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
          id:="article-black-turnstone",
          img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/ArenariaMelanocephala_3864.jpg/318px-ArenariaMelanocephala_3864.jpg"),

          p(b("Black turnstone - "), em("Arenaria melanocephala")),
          p("""
              |Comment: Twenty or so black turnstones were observed picking through the seaweed and other detritus that
              |had washed ashore in a recent storm. The black turnstones were not true to their name in that they did
              |not use their beaks as a tool to turn over small items to search for food underneath them as did the few
              |ruddy turnstones that were picking through the detritus with them.""".stripMargin),
          p("""
              |Several of the black turnstones had the hint of green coloration that is apparent in this image. Whether
              |the green is visible or not seems to be dependent on the angle of the illumination and the angle that the
              |bird is observed at.""".stripMargin)
        ),

        hr,

        div(
          id:="form-1",
          cls:="my-form onhover-grow",
          h3("Form 1"),
          p("foo bar baz"),
          p(
            button(
              id:=s"btn-${"Click me!".take(10).toLowerCase}",
              cls:="my-btn",
              "Click me!",
              onclick:={ () => console.log("Form 1 clicked") }
            )
          )
        ),

        /// the same as above but extracted to own def
        form1(
          theId = "form-2",
          title = "Form 2",
          classes = Seq("onhover-grow"),
          text = "foo bar baz",
          buttonText = "Click me!",
          btnOnclick = { (_) => console.log("Form 2 clicked") }
        ),

        form1(
          theId = "form-3",
          classes = Seq("onhover-grow"),
          title = "Form 3",
          text = "lorem ipsum",
          buttonText = "No, click me!",
          btnOnclick = { (_) => console.log("Form 3 clicked") }
        ),

        form1(
          theId = "form-4",
//          title = "Form 4",
          text = "Sur le pont d'Avignon L'on y danse ...",
          buttonText = "Well, just pick one of us and click already.",
          btnOnclick = { (_) => console.log("Form 4 clicked") }
        )

      ).render
    )
  }
}
