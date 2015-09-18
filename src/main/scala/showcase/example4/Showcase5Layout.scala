package showcase.example4

import org.scalajs.dom
import org.scalajs.dom.{console, html}
import scala.language.implicitConversions

/* it's rather hard to get these imports right! */
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.{article, nav}
import scalatags.JsDom.TypedTag

import htmlib.DomUtil._
import showcase.Showcase


object Showcase5Layout extends Showcase {
  def setupUi(container: html.Element): Unit = {


    // TODO: some form examples, a pseudo-login ...


    // TODO clean up MyImage
    // TODO extract to own file

    /// explicit companion object to case class must be defined before its case class
    object MyImage {

//      implicit def src2img(srcStr: String): html.Image = {

//      implicit def src2img(srcStr: String): TypedTag[html.Image] = {
//        require(isValidName(srcStr))
//        img(src:=srcStr).render
//        img(src:=srcStr)
//      }
//      implicit def tpl2myImg(t: Tuple3[String, String, String]): MyImage = MyImage(t._1, t._2, t._3)

//      def apply(x: Tuple3[String, String, String]): MyImage = MyImage(x._1, x._2, x._3)
//      def apply(x: Tuple3[String, String, String]): MyImage = MyImage(x._1, x._2, x._3)
      def apply(x: Tuple3[String, TypedTag[html.Image], String]): MyImage = MyImage(x._1, x._2, x._3)

      private val ValidImage = """.*\.(jpg|png)""".r

      def isValidName(name: String): Boolean = name match {
        case ValidImage(_) => true
        case _ => false
      }
//      def hasValidName(image: html.Image): Boolean = image.src match {
//        case ValidImage(_) => true
//        case _ => false
//      }
    }
//    case class MyImage(name: String, image: html.Image, caption: String = "") {
    case class MyImage(name: String, image: TypedTag[html.Image], caption: String = "") {
      import MyImage._
//      require(hasValidName(image))
    }

    import MyImage._


    /// either explicitly ...
    val imgs: IndexedSeq[MyImage] = IndexedSeq[MyImage](
      MyImage("First", img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/ArenariaMelanocephala_3864.jpg/318px-ArenariaMelanocephala_3864.jpg"), "bla bla bla"),
      MyImage("Second", img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Arenaria_melanocephala_%28tournepierre%29.jpg/316px-Arenaria_melanocephala_%28tournepierre%29.jpg"), ""),
      MyImage("Third", img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Arenaria_melanocephala1.jpg/320px-Arenaria_melanocephala1.jpg"), "piff paff puff"),
      MyImage("Fourth", img(src:="https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Black_Turnstone_%28Arenaria_melanocephala%29.jpg/320px-Black_Turnstone_%28Arenaria_melanocephala%29.jpg"), "piff paff puff")
    )

    /// ... or with implicit conversion
//    val imgs: IndexedSeq[MyImage] = IndexedSeq[MyImage](
//      ("First", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/ArenariaMelanocephala_3864.jpg/318px-ArenariaMelanocephala_3864.jpg", "bla bla bla"),
//      ("Second", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Arenaria_melanocephala_%28tournepierre%29.jpg/316px-Arenaria_melanocephala_%28tournepierre%29.jpg", ""),
//      ("Third", "foo/bar.jpg", "piff paff puff")
//    )

    var imgName = b(imgs.head.name).render
    var imgCaption = em(imgs.head.caption).render
    val imgSub = p().render
    val imgContainer = div(
      id:="container-image",
      imgs.head.image,
      p(b(imgs.head.name), em(imgs.head.caption))
    ).render


    container.appendChild(
      div(
        h1("Scala.js Showcase 4"),

        nav(
          for (MyImage(name, image, caption) <- imgs) yield button(
            id:=s"btn-spam-${name.toLowerCase}",
            cls:="my-btn onhover-shrink",
            name,
            onclick:={ (ev: dom.MouseEvent) => {
              println(s"$name clicked")

//              val toapp: html.Element = Seq(
//                image,
//                p(b(name),
//                  if (caption.isEmpty) "" else " - ", em(caption)
//                )
//              ).render.asInstanceOf[html.Element]

//              val toapp: html.Element = Seq(
              def elemSeq2Elem(xs: TypedTag[html.Element]*): html.Element =
                xs.render.asInstanceOf[html.Element]

              def elemSeq2Elem1000(xs: html.Element*): html.Element =
                xs.render.asInstanceOf[html.Element]

              val repl = Seq(
                image,
                p(b(name),
                  if (caption.isEmpty) "" else " - ", em(caption)
                )
              ).render.asInstanceOf[html.Element]

              replaceChildren(imgContainer, repl)


//              val toapp = elemSeq2Elem(image,
//                p(b(name),
//                  if (caption.isEmpty) "" else " - ", em(caption)
//                ))
//              (
//                image,
//                p(b(name),
//                  if (caption.isEmpty) "" else " - ", em(caption)
//                )
//              ).render.asInstanceOf[html.Element]

//              replaceChildren(imgContainer, toapp)



//              def rep(cont: dom.Node, repl: dom.Node) = {
//              def rep(cont: html.Element, repl: html.Element) = {
//                val parent = cont.parentElement
//                while (Option(cont.firstChild) != None) {
//                  cont.removeChild(cont.firstElementChild)
//                }
//                cont.appendChild(repl)
//              }
//              replaceChildren(imgContainer, toapp)

//                val parent = cont.parentNode

//                parent.childNodes.foreach(parent.removeChild)

                  /// would have been nice, but did not work ...
//                  ext.PimpedNodeList(parent.childNodes).foreach(_ => parent.removeChild(_))



//                val nds: Stream[Option[dom.Node]] = cons(Option(cont.firstChild), nds)
//                def dododo(n: Stream[Option[dom.Node]]): Unit = {
//                  val x = n.head
//                  x match { case Some(_) => dododo(n.tail); case None => ()}
//                }
//
//                def xyxy(): Unit = {
//                  if (Option(cont.firstChild) != None) {
//                    cont.removeChild(cont.firstChild))
//                    xyxy()
//                  }
//                }





//              theText.textContent = b(image.name), "  ", em(caption)

//              theText.textContent = image.name
//              imgName.textContent = name
//              imgCaption.textContent = caption


//              imgSub = p(imgName, "   " , em(imgCaption)).render

            } }
          ),

//          for ((im, tt) <- imgs) yield button(
//            id:=s"btn-spam-$tt",
//            cls:="my-btn onhover-shrink",
//            s"Spam $tt",
//            onclick:={ (ev: dom.MouseEvent) => {
//              console.log(s"$tt clicked!!!", im.render, theImgContainer.firstElementChild)
//              val res = im.render
//              theImgContainer.replaceChild(im.render, theImgContainer.firstElementChild)
//              theText.textContent = s"spam-$tt clicked!"
//
//            } }
//          ),
          input(
            id:="btn-eggs",
            cls:="my-btn",
            tpe:="button",
            value:="Eggs",
            onclick:={ () => {
              println("eggs clicked!!!")
            } }
          )
        ),

        hr,

//        article(
        div(
          id:="article",

//          header(h3("The black turnstone")),

          imgContainer,

//          theImgName,
//
//          p(theImgName, " " , em(theImgCaption)),

//          p(b("Black turnstone - "), em("Arenaria melanocephala")),

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
