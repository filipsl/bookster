package controllers

import exceptions.{BookNotFoundException, InvalidIsbnException, NoQueryException, NoTermsException}
import javax.inject._
import models.Ratings
import play.api.mvc._
import models.isbn.Isbn10
import services.PricesService
import repositories.BookRepository

@Singleton
class BooksController @Inject()(
  cc: ControllerComponents,
  bookRepository: BookRepository,
  pricesService: PricesService
) extends AbstractController(cc) {

  def explore = Action {
    Ok(views.html.books.explore(bookRepository.random(20)))
  }

  def search(q: Option[String]) = Action { implicit request =>
    if (q.isEmpty || q.get.isEmpty) {
      throw new NoQueryException
    }
    val query = q.get
    try {
      val isbn10 = new Isbn10(query.replaceAll("-", ""))
      Redirect(routes.BooksController.show(isbn10.toString))
    } catch {
      case _: InvalidIsbnException =>
        val ratings = Ratings(request.session.get("ratings"))
        try {
          val results = bookRepository.search(query)
          /*
          if (results.length == 1) {
            Redirect(routes.BooksController.show(results(0).isbn10.toString))
          } else {
            Ok(views.html.books.search(query, results, ratings))
          }
          */
          Ok(views.html.books.search(query, results, ratings))
        } catch {
          case _: NoTermsException => Ok(views.html.books.search(query, Array(), ratings))
        }
    }
  }

  def show(isbn10String: String) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isEmpty) throw new BookNotFoundException
    val book = result.get
    val rating = Ratings(request.session.get("ratings")).forBook(book)
    Ok(views.html.books.show(book, rating))
  }

  def prices(isbn10String: String) = Action {
    val isbn10 = new Isbn10(isbn10String)
    val results = pricesService.getPricesOfBook(isbn10)
    Ok(views.html.books.prices(results))
  }
}
