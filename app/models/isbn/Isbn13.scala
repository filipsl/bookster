package models.isbn

object Isbn13 {
  val Regex = "978[0-9]{10}"
}

class Isbn13(val isbnString: String) extends Isbn(isbnString) {

  override def verify(isbnString: String): Boolean = {
    isbnString.matches(Isbn13.Regex)
  }

  override def toIsbn13: Isbn13 = this

  def toPretty: String = {
    Array(
      isbnString.substring(0, 3),
      isbnString.substring(3, 5),
      isbnString.substring(5, 11),
      isbnString.charAt(11),
      isbnString.charAt(12),
    ).mkString("-")
  }

  override def canEqual(a: Any): Boolean = a.isInstanceOf[Isbn13]

  override def equals(that: Any): Boolean =
    that match {
      case that: Isbn13 => that.canEqual(this) && this.toString == that.toString
      case _ => false
    }

}
