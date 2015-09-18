package htmlib

import scalatags.DataConverters._


/**
 * Provides some CSS-constants.
 * - (Available via `import`).
 */
object Defs extends Defs

/**
 * Provides some CSS-constants.
 * - (Available via `extends` or `with`).
 */
sealed trait Defs {
  val ColorLightGrey = "#F0F0F0"
  val BorderRadiusDefault = 5.px
  val BorderCode = "solid 1px #C8C8C8"  // style width color
}
