package models

import exceptions.InvalidRatingException
import play.api.libs.json.Json

object Ratings {

  def apply(ratingsSession: Option[String] = None): Ratings = {
    unserialize(ratingsSession.getOrElse("{}"))
  }

  def unserialize(string: String): Ratings = {
    new Ratings(Json.parse(string).as[Map[String,Int]].map({
      case (key, value) => (key.toLong, value)
    }))
  }

}

class Ratings(private val data: Map[Long,Int]) {

  def withRating(book: Book, rating: Int): Ratings = {
    if (!(1 to 5 contains rating)) {
      throw new InvalidRatingException
    }
    new Ratings(data ++ Map[Long,Int](book.id -> rating))
  }

  def withoutRating(book: Book): Ratings = {
    new Ratings(data - book.id)
  }

  def nonEmpty: Boolean = data.nonEmpty

  def isEmpty: Boolean = data.isEmpty

  def size: Int = data.size

  def bookIds: Array[Long] = data.keys.toArray

  def forBook(book: Book): Option[Int] = data.get(book.id)

  def toMap: Map[Long,Int] = data

  def serialize: String = {
    Json.stringify(Json.toJson(data.map({
      case (key, value) => (key.toString, value)
    })))
  }

}
