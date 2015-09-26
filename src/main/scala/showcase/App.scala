package showcase

import org.scalajs.dom.html
import scalajs.js.annotation.JSExport

import htmlib.DomUtil._


trait Showcase {
  def setupUi(container: html.Element): Unit
  def apply(container: html.Element): Unit = {
    container.innerHTML = ""
    setupUi(container)
  }
}

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
//    foo()

    val container = getContainer()

    // TODO
    // - refactor to correct packages
    // - extract lib functions etc.
    // - add central navigation to the several showcases as sub-pages

//    example0.ShowcaseCanvas(container)
//    example1.ShowcaseTextBoxes(container)
//    example2.ShowcaseDragAndDrop(container) // TODO use a proper data model
//    example3.ShowcaseTextBoxDecorators(container)
//    example4.ShowcaseLayout(container)
//    example5.ShowcaseLayout(container)
//    example6.ShowcaseInputForms(container) // TODO add login example and bigger form, maybe
//    example7.ShowcaseInteractiveTextAnalyzer(container) // could be extended and improved (see below)
//    example8.ShowcaseAjax(container)
    example8.ShowcaseAjax2(container)

    /* TODO
     * - autocomplete
     * - autocomplete, with Rx
     * 1. use the canvas frenzy-example -> playground
     * 2. login UI and a more complex form
     * 3. a simple blog
     * - maybe inheritance
     * - an advanced drag-and-drop-example -> maybe a blog
     *   - with a data model for the containers
     *   - with nice containers like in Showcase6InputForms
     * x AJAX, with Futures, Async  -->  WeatherSearch, as in Hands-on Scala.js, or if possible finance API for currency conversion
     * - JavaScript-interop (in separate hello-interop project?)  -->  reuse what is already prepared in playground
     *   - export Scala.js to JavaScript
     *     - just the usual @JSExport (like used in the main, see above)
     *     - use cross-compiling statistics-lib in JavaScript
     *  - import JavaScript into Scala.js
     *    - as done all the time with the facades from Scala.js, ScalaTags, ...
     *    - own JavaScript example-code
     * - Rx / Async for control flow with clicks -> sketchpad or something similar
     * - Server/Client with routing with Spray and Autowire  -->  reuse what was tested in preparation and maybe use it with this webpage here
     * - cross-compile example to test semantic differences (could be called hello-semantic-differences)
     *
     * - possible TextAnalyzer improvements
     *   - make text searchable -> highlight found locations  -->  Hands-on Scala.js
     *   - diagram for word count
     *   - toggle between letter count und word count
     *   - use functions from statistics library
     *   - use Rx or Async for better interactivity
     *   - import text
     *     - via multi-line paste
     *     - via file upload
     */

    println("UI was set up.")
  }
}
