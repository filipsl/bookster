package controllers

import exceptions.InvalidRatingException
import javax.inject._
import models.Book
import models.isbn.Isbn10
import play.api.libs.json._
import play.api.mvc._
import repositories.BookRepository

@Singleton
class RatingsController @Inject()(cc: ControllerComponents, bookRepository: BookRepository) extends AbstractController(cc) {

  private def unserializeRatings(ratings: String): Map[Long,Int] = {
    Json.parse(ratings).as[Map[String,Int]].map({
      case (key, value) => (key.toLong, value)
    })
  }

  private def serializeRatings(ratings: Map[Long,Int]): String = {
    Json.stringify(Json.toJson(ratings.map({
      case (key, value) => (key.toString, value)
    })))
  }

  def getRatings(implicit request: Request[_]): Map[Long,Int] = {
    unserializeRatings(request.session.get("ratings").getOrElse("{}"))
  }

  private def withRating(book: Book, rating: Int)(implicit request: Request[_]): Map[Long,Int] = {
    if (!(1 to 5 contains rating)) {
      throw new InvalidRatingException
    }
    unserializeRatings(request.session.get("ratings").getOrElse("{}")) ++ Map[Long,Int](book.id -> rating)
  }

  private def withoutRating(book: Book)(implicit request: Request[_]): Map[Long,Int] = {
    unserializeRatings(request.session.get("ratings").getOrElse("{}")) - book.id
  }

  def show = Action { implicit request =>
    val book_ids = getRatings.keys.toArray
    val books = bookRepository.findManyByIds(book_ids)
    Ok(views.html.ratings.show(books, getRatings))
  }

  def rate(isbn10String: String, rating: Int) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isDefined) {
      val book = result.get
      Redirect(request.headers("referer")).withSession(request.session + ("ratings" -> serializeRatings(withRating(book, rating))))
    } else {
      NotFound("Book not found")
    }
  }

  def unrate(isbn10String: String) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isDefined) {
      val book = result.get
      Redirect(request.headers("referer")).withSession(request.session + ("ratings" -> serializeRatings(withoutRating(book))))
    } else {
      NotFound("Book not found")
    }
  }

  def unrateAll = Action { request =>
    Redirect(request.headers("referer")).withSession(request.session - "ratings")
  }

  def recommend = Action { implicit request =>
    val book_ids = Array[Long](1, 2, 3) // mock-up
    val books = bookRepository.findManyByIds(book_ids)
    Ok(views.html.ratings.recommend(books))
  }

}
