package showcase

import scalajs.js.annotation.JSExport

import lib.html.DomUtil.getContainer


@JSExport
//object App extends lib.html.DomUtil {
object App {

  // TODO write tests for this
  //   cross-compile-project to see the differences
  //   -->  http://www.scala-js.org/doc/semantics.html
  def foo() = {
    println(1.0)
    println(1.1)
    println(1.1f)
    println(1.2)
    println(1.2f)
    println(1.5)
    println(1.5f)
//    println(1/0)   // undefined behavior, whereas on the JVM it should be java.lang.ArithmeticException
//    println(1/0.0) // Infinity
    val str = ""

//    str.charAt(-1)  // intercept scala.scalajs.runtime.UndefinedBehaviorError
//    str.charAt(100) // intercept scala.scalajs.runtime.UndefinedBehaviorError
    class Bar { def baz() = println("ping") }
    val bar: Bar = null
    bar.baz()
    println("-------------")
  }

  @JSExport
  def main(): Unit = {
    println("Hello world!")
//    foo()

    val container = getContainer()
    container.innerHTML = ""
//    Showcase1TextBoxes(container)
//    Showcase2DragAndDrop(container) // TODO use a proper data model
//    Showcase3TextBoxDecorators(container)
    Showcase4InputForms(container)
  }
}
