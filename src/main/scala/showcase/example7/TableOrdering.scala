package showcase.example7

object  TableOrdering {
  sealed trait TableOrdering extends Ordering[(Char, Int)]

  case object CharsAsc extends TableOrdering {
    override def compare(x: (Char, Int), y: (Char, Int)) = x._1 compare y._1
  }
  case object CharsDesc extends TableOrdering {
    override def compare(x: (Char, Int), y: (Char, Int)) = y._1 compare x._1
  }
  case object FreqAsc extends TableOrdering {
    override def compare(x: (Char, Int), y: (Char, Int)) = x._2 compare y._2
  }
  case object FreqDesc extends TableOrdering {
    override def compare(x: (Char, Int), y: (Char, Int)) = y._2 compare x._2
  }
}
