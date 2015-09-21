package showcase.example4

import org.scalajs.dom
/* To avoid name clash with scalatags.JsDom.tags2.html. More explicit imports
 * would be an alternative, i.e.: scalatags.JsDom.tags2.{article, nav} */
import org.scalajs.dom.{console, html => sjs_html}

/* possibliy clutters namespace */
//import scalatags.JsDom.all._

/* imports almost the same as 'scalatags.JsDom.all._', but
 *  - attrs and styles have to be prefixed with '*.'
 *  - 'scalatags.JsDom.tags._' has to be imported explicitly
 */
import scalatags.JsDom.short._
import scalatags.JsDom.tags._
import scalatags.JsDom.tags2._

import showcase.Showcase


object ShowcaseLayout extends Showcase {
  def setupUi(container: sjs_html.Element): Unit = {

    val theImg = img(*.src:="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/ArenariaMelanocephala_3864.jpg/318px-ArenariaMelanocephala_3864.jpg").render

    container.appendChild(
      div(
        h1("Scala.js Showcase 4"),

        nav(
          button(
            *.id:="btn-spam-1",
            *.cls:="my-btn onhover-shrink",
            "Spam",
            *.onclick:={ (ev: dom.MouseEvent) => { console.log("spam-1 clicked!", ev)} }
          ),
          button(
            *.id:="btn-spam-2",
            *.cls:="my-btn onhover-shrink",
            "Spam",
            *.onclick:={ (ev: dom.MouseEvent) => { console.log("spam-2 clicked!", ev)} }
          ),
          button(
            *.id:="btn-spam-3",
            *.cls:="my-btn onhover-shrink",
            "Spam",
            *.onclick:={ (ev: dom.MouseEvent) => { console.log("spam-3 clicked!", ev)} }
          ),
          input(
            *.id:="btn-eggs",
            *.cls:="my-btn onhover-rotate",
            *.tpe:="button",
            *.value:="Eggs",
            *.onclick:={ () => {
              println("eggs clicked!!!")
              theImg.classList.toggle("rotated")
            } }
          )
        ),

        hr,

        article(
          *.id:="article-black-turnstone",

          header(h3("The black turnstone")),
            theImg,

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
        )

      ).render
    )
  }
}
