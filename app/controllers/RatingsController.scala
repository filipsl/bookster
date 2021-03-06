package controllers

import exceptions.{BookNotFoundException, NoRatingsException}
import javax.inject._
import models.Ratings
import models.isbn.Isbn10
import play.api.mvc._
import repositories.BookRepository

@Singleton
class RatingsController @Inject()(
  cc: ControllerComponents,
  bookRepository: BookRepository
) extends AbstractController(cc) {

  def show = Action { implicit request =>
    val ratings = Ratings(request.session.get("ratings"))
    val books = bookRepository.findManyByIds(ratings.bookIds)
    Ok(views.html.ratings.show(books, ratings))
  }

  private def redirectToReferer(request: Request[_]) = Redirect(
    request.headers.get("referer").getOrElse(routes.RatingsController.show().url)
  )

  def rate(isbn10String: String, rating: Int) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isEmpty) {
      throw new BookNotFoundException
    }
    val book = result.get
    val ratings = Ratings(request.session.get("ratings")).withRating(book, rating)
    redirectToReferer(request).withSession(
      request.session + ("ratings" -> ratings.serialize)
    )
  }

  def unrate(isbn10String: String) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isEmpty) throw new BookNotFoundException
    val book = result.get
    val ratings = Ratings(request.session.get("ratings")).withoutRating(book)
    redirectToReferer(request).withSession(
      request.session + ("ratings" -> ratings.serialize)
    )
  }

  def clear = Action { implicit request =>
    redirectToReferer(request).withSession(
      request.session - "ratings"
    )
  }

}
