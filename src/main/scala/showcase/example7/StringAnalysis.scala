package showcase.example7

object StringAnalysis extends StringAnalysis {
  import scala.language.implicitConversions

  implicit class AnalyzableString(str: String) extends StringAnalysis {
    def numLines: Int = numLines(str)
    def numWords: Int = numWords(str)
    def numChars: Int = str.length
    def numLetters: Int = numLetters(str)
    def freqChars: Map[Char, Int] = freqChars(str)
    def freqWords: Map[String, Int] = freqWords(str)
  }
}

trait StringAnalysis {
  def numLines(str: String): Int =
    if (str.isEmpty) 0
    else str.count(_ == '\n') + 1

  def numWords(str: String): Int =
    if (str.isEmpty) 0
    else str.split("\\s+").length

  def numLetters(str: String): Int = str.count(!_.isWhitespace)

  def freqDistinct[T](seq: Seq[T]): Map[T, Int] = seq.foldLeft(Map[T, Int]())(
    (accu, elm) => accu + (elm -> (accu.getOrElse(elm, 0) + 1))
  )

  def freqChars(str: String): Map[Char, Int] =
    freqDistinct( str.toSeq.filterNot(_.isWhitespace).map(_.toLower) )

  val Letter = """[a-zA-Z]""".r

  def freqWords(str: String): Map[String, Int] = {
//      def removeNonLetters(str: String) = str.toSeq.filter(_.isLetter).mkString       /* Character.isLetter does not work, because it's Java */
    def removeNonLetters(str: String) = Letter.findAllIn(str).foldLeft("")(_ + _)
    val words = str.split("\\s+").map( w => removeNonLetters(w).toLowerCase ).filterNot(_.isEmpty)
    freqDistinct(words)
  }
}
