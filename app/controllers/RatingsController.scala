package controllers

import exceptions.InvalidRatingException
import javax.inject._
import models.Book
import models.isbn.Isbn10
import models.shop.ShopsContainer
import play.api.libs.json._
import play.api.mvc._
import services.repositories.BookRepository

@Singleton
class RatingsController @Inject()(cc: ControllerComponents, bookRepository: BookRepository) extends AbstractController(cc) {

  def ratings = Action { implicit request =>
    Ok(views.html.books.ratings(getRatings))
  }

  private def unserializeRatings(ratings: String): Map[String,Int] = {
    Json.parse(ratings).as[Map[String,Int]]
  }

  private def serializeRatings(ratings: Map[String,Int]): String = {
    Json.stringify(Json.toJson(ratings))
  }

  private def getRatings(implicit request: Request[_]): Map[String,Int] = {
    unserializeRatings(request.session.get("ratings").getOrElse("{}"))
  }

  private def withRating(book: Book, rating: Int)(implicit request: Request[_]): Map[String,Int] = {
    if (!(1 to 5 contains rating)) {
      throw new InvalidRatingException
    }
    unserializeRatings(request.session.get("ratings").getOrElse("{}")) ++ Map[String,Int](book.id.toString -> rating)
  }

  private def withoutRating(book: Book)(implicit request: Request[_]): Map[String,Int] = {
    unserializeRatings(request.session.get("ratings").getOrElse("{}")) - book.id.toString
  }

  def rate(isbn10String: String, rating: Int) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isDefined) {
      val book = result.get
      Ok("rate").withSession(request.session + ("ratings" -> serializeRatings(withRating(book, rating))))
    } else {
      NotFound("Book not found")
    }
  }

  def unrate(isbn10String: String) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isDefined) {
      val book = result.get
      Ok("unrate").withSession(request.session + ("ratings" -> serializeRatings(withoutRating(book))))
    } else {
      NotFound("Book not found")
    }
  }

  def unrateAll = Action { request =>
    Ok("unrateAll").withSession(request.session - "ratings")
  }

}
