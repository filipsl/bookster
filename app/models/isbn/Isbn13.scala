package models.isbn

object Isbn13 {
  val Regex = "978[0-9]{10}"
}

class Isbn13(val isbnString: String) extends Isbn(isbnString) {

  override def verify(isbnString: String): Boolean = {
    isbnString.matches(Isbn13.Regex)
  }

  override def toIsbn13: Isbn13 = this
}
