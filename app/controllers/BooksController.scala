package controllers

import exceptions.InvalidIsbnException
import javax.inject._
import play.api.mvc._
import models.isbn.Isbn10
import services.PricesService
import repositories.BookRepository

@Singleton
class BooksController @Inject()(
  cc: ControllerComponents,
  bookRepository: BookRepository,
  pricesService: PricesService,
  ratingsController: RatingsController
) extends AbstractController(cc) {

  def explore = Action {
    Ok(views.html.books.explore(bookRepository.random(20)))
  }

  def search(q: String) = Action { implicit request =>
    try {
      val isbn10 = new Isbn10(q.replaceAll("-", ""))
      Redirect(routes.BooksController.show(isbn10.toString))
    } catch {
      case _: InvalidIsbnException =>
        val results = bookRepository.search(q)
        if (results.length == 1) {
          Redirect(routes.BooksController.show(results(0).isbn10.toString))
        } else {
          Ok(views.html.books.search(q, results, ratingsController.getRatings))
        }
    }
  }

  def show(isbn10String: String) = Action { implicit request =>
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isDefined) {
      val book = result.get
      val results = pricesService.getPricesOfBook(book)
      var price = if (results.isEmpty) None else results(0)._3
      Ok(views.html.books.show(book, ratingsController.getRatings, price, results))
    } else {
      NotFound("Book not found")
    }
  }

  /*
  def prices(isbn10String: String) = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }
  */
}
