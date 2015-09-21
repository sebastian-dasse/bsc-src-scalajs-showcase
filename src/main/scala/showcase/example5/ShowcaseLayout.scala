package showcase.example5

import org.scalajs.dom
import org.scalajs.dom.html
import scala.language.implicitConversions

/* it's rather hard to get these imports right! */
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.{article, nav}

import htmlib.DomUtil._
import showcase.Showcase


case class MyImage(name: String, image: TypedTag[html.Image], caption: String = "")

object ShowcaseLayout extends Showcase {
  def setupUi(container: html.Element): Unit = {

    val imgs: IndexedSeq[MyImage] = IndexedSeq[MyImage](
      MyImage(
        "First",
        img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/ArenariaMelanocephala_3864.jpg/318px-ArenariaMelanocephala_3864.jpg"),
        "bla bla bla"
      ),
      MyImage(
        "Second",
        img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Arenaria_melanocephala_%28tournepierre%29.jpg/316px-Arenaria_melanocephala_%28tournepierre%29.jpg")
      ),
      MyImage(
        "Third",
        img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Arenaria_melanocephala1.jpg/320px-Arenaria_melanocephala1.jpg"),
        "piff paff puff"
      ),
      MyImage(
        "Fourth",
        img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Black_Turnstone_%28Arenaria_melanocephala%29.jpg/320px-Black_Turnstone_%28Arenaria_melanocephala%29.jpg"),
        "piff paff puff"
      )
    )

    var imgName = b(imgs.head.name).render
    var imgCaption = em(imgs.head.caption).render
    val imgSub = p().render
    def captionPar(name: String, caption: String) = p(
      b(name),
      if (caption.isEmpty) "" else " - ", em(caption)
    )
    val imgContainer = div(
      id:="container-image",
      imgs.head.image,
      captionPar(imgs.head.name, imgs.head.caption)
    ).render


    container.appendChild(
      div(
        h1("Scala.js Showcase 5"),

        nav(
          for (MyImage(name, image, caption) <- imgs) yield button(
            id:=s"btn-spam-${name.toLowerCase}",
            cls:="my-btn onhover-shrink",
            name,
            onclick:={ (ev: dom.MouseEvent) => {
              replaceChildren(imgContainer)(
                image,
                captionPar(name, caption)
              )
            } }
          )
        ),

        hr,

        article(
          id:="article",

          imgContainer,

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
