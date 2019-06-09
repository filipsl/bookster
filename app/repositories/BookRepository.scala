package repositories

import models.{Book, Ratings}
import models.isbn.Isbn10

trait BookRepository {
  def ratingsCount: Long
  def booksCount: Long
  def usersCount: Long
  def find(isbn10: Isbn10): Option[Book]
  def findManyByIds(ids: Array[Long]): Array[Book]
  def search(q: String): Array[Book]
  def random(n: Int): Array[Book]
  // def mostPopularIsbns(n: Int): Array[Isbn10]
  def recommend(ratings: Ratings, n: Int): Array[Book]
}
