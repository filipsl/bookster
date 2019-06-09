package repositories

import models.Book
import models.isbn.Isbn10

trait BookRepository {
  def find(isbn10: Isbn10): Option[Book]
  def findManyByIds(ids: Array[Long]): Array[Book]
  def search(q: String): Array[Book]
  def random(n: Int): Array[Book]
  def recommend(ratings: Map[Long, Int], n: Int): Array[Book]
  // def mostPopularIsbns(n: Int): Array[Isbn10]
}
