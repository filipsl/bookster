package services.repositories

import models.Book
import models.isbn.Isbn10

trait BookRepository {
  def find(isbn10: Isbn10): Option[Book]
  def search(q: String): Array[Book]
  def random(n: Int): Array[Book]
}
