package showcase.example0

import org.scalajs.dom
import org.scalajs.dom.raw.UIEvent
import org.scalajs.dom.{document, html}
import scala.util.Random

import showcase.Showcase


case class Point(x: Int, y: Int)

case class Dimension(w: Int, h: Int) {
  def +(n: Int): Dimension = Dimension(w + n, h + n)
  def +(other: Dimension): Dimension = Dimension(w + other.w, h + other.h)
  def -(n: Int): Dimension = this + (-n)
  def -(other: Dimension): Dimension = Dimension(w - other.w, h - other.h)
  def *(n: Int): Dimension = Dimension(w * n, h * n)
  def /(n: Int): Dimension = Dimension(w / n, h / n)
}


object ShowcaseCanvas extends Showcase {

  private val margin = 50

  override def setupUi(container: html.Element): Unit = {
    val canvas = dom.document.createElement("canvas").asInstanceOf[html.Canvas]
    container.appendChild(canvas)
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val docEl = dom.document.documentElement

    def dims = Dimension(docEl.clientWidth, docEl.clientHeight)

    def resizeWith(margin: Int = 0) = dims match { case d: Dimension =>
      canvas.style.left = s"${margin}px"
      canvas.style.top = s"${margin}px"
      canvas.style.position = "absolute"
      canvas.width = d.w - 2 * margin
      canvas.height = d.h - 2 * margin
    }

    def resize(): Unit = resizeWith(margin)
    resize()

    dom.window.onresize = {
      (evt: UIEvent) => {
        resize()
        clear()
        dom.console.log("[onclick-event-handler] " + evt)
        println(s"resized: ${dims}")
      }
    }

    def clear(): Unit = dims match { case d: Dimension =>
      ctx.fillStyle = "silver"
      ctx.fillRect(0, 0, d.w, d.h)
    }

    def randomColor(alpha: Double = Random.nextDouble) = {
      def rnd = Random.nextInt(256)
      s"rgba($rnd, $rnd, $rnd, ${alpha})"
    }

    def randomPoint(dimension: Dimension = dims) = dimension match { case d: Dimension =>
      Point(Random.nextInt(d.w), Random.nextInt(d.h))
    }

    def interpolate(a: Int, b: Int, factor: Double): Int = {
      require(0 <= factor && factor <= 1)
      ((1 - factor) * a + factor * b).toInt
    }

    var lastPoint: Point = randomPoint()

    def randomPointSmoothed(scale: Int = 1) = {
      val d = dims
      val newPoint = randomPoint(Dimension(d.w/scale, d.h/scale))
      lastPoint
      val factor = 0.1
      Point(interpolate(newPoint.x, lastPoint.x, factor),
        interpolate(newPoint.y, lastPoint.y, factor))
    }

    def drawRndRect() = {
      val p1 = randomPoint()
      dims match { case d: Dimension => Math.min(d.w, d.h) }
      val p2 = randomPointSmoothed(10)
      ctx.fillRect(p1.x, p1.y, p2.x, p2.y)
    }

    def drawRndCircle() = {
      val p1 = randomPoint()
      ctx.beginPath()
      val p2 = randomPointSmoothed(20)
      ctx.arc(p1.x, p1.y, p2.x, 0, 360)
      ctx.fill()
    }

    def drawRndGeo() = {
      Random.nextInt(2) match {
        case 0 => drawRndCircle()
        case 1 => drawRndRect()
      }
    }

    def draw(): Unit = {
      ctx.fillStyle = randomColor()
      drawRndGeo()
    }

    def sayHello(): Unit = dims match { case di: Dimension =>
      val range = di.h/3
      val fontsize = range + Random.nextInt(range)
      val pos = randomPoint(di - Dimension(3*fontsize, fontsize))
      ctx.font = s"${fontsize}px Verdana"
      ctx.fillStyle = randomColor(1.0)
      ctx.textAlign = "left"
      ctx.textBaseline = "top"
      ctx.fillText("Hello!", pos.x, pos.y - fontsize/2)
    }

    dom.document.onclick = {
      (evt: UIEvent) => {
        sayHello()
        dom.console.log(s"[onclick-event-handler] ", evt)
      }
    }

    clear()
    dom.setInterval(() => draw(), 50)
  }
}
