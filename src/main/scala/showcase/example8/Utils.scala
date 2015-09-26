package showcase.example8

object Utils {
  val Numeric = """[+-]?[0-9]+([.,][0-9]*)?""".r

  implicit class StringWrapper(str: String) {
    def asDouble: Double =
      Numeric.findFirstIn(str).getOrElse("0").replaceAll(",", ".").toDouble
  }

  val currencyFormat: String = "%.4f"

  // TODO format like that: "1.000.000,0000 EUR"
  def format(d: Double): String = currencyFormat.format(d)
}
