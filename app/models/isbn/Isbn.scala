package models.isbn

import exceptions.InvalidIsbnException

abstract class Isbn(isbnString: String) extends Serializable {

  if (!verify(isbnString)) {
    throw new InvalidIsbnException
  }

  protected def verify(isbn: String): Boolean

  override def toString: String = isbnString

  def toIsbn13: Isbn13

}
