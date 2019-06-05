package models.isbn

abstract class Isbn(isbnString: String) {

  if (!verify(isbnString)) {
    throw new IllegalArgumentException("Invalid ISBN")
  }

  protected def verify(isbn: String): Boolean

  override def toString: String = isbnString

  protected def toIsbn13: Isbn13
}
