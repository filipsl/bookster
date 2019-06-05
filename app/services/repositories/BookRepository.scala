package services.repositories

import models.Book
import models.isbn.Isbn10

trait BookRepository {
  def find(isbn10: Isbn10): Book
}
