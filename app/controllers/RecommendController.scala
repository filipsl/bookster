package controllers

import exceptions.{BookNotFoundException, NoRatingsException}
import javax.inject._
import models.Ratings
import models.isbn.Isbn10
import play.api.mvc._
import repositories.BookRepository

@Singleton
class RecommendController @Inject()(
  cc: ControllerComponents,
  bookRepository: BookRepository
) extends AbstractController(cc) {

  def loading = Action { implicit request =>
    Ok(views.html.recommend.loading(
      bookRepository.ratingsCount,
      bookRepository.booksCount,
      bookRepository.usersCount
    ))
  }

  def results = Action { implicit request =>
    val ratings = Ratings(request.session.get("ratings"))
    try {
      val books = bookRepository.recommend(ratings, 10)
      Ok(views.html.recommend.results(books, ratings))
    } catch {
      case _: NoRatingsException => Redirect(routes.RatingsController.show())
    }
  }

}
