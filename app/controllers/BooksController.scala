package controllers

import exceptions.InvalidIsbnException
import javax.inject._
import play.api.mvc._
import models.isbn.Isbn10
import models.shop.ShopsContainer
import services.repositories.BookRepository

@Singleton
class BooksController @Inject()(cc: ControllerComponents, bookRepository: BookRepository) extends AbstractController(cc) {

  def explore = Action {
    Ok(views.html.books.explore(bookRepository.random(20)))
  }

  def search(q: String) = Action {
    try {
      val isbn10 = new Isbn10(q.replaceAll("-", ""))
      Redirect(routes.BooksController.show(isbn10.toString))
    } catch {
      case _: InvalidIsbnException =>
        val results = bookRepository.search(q)
        Ok(views.html.books.search(q, results))
    }
  }

  def show(isbn10String: String) = Action {
    val isbn10 = new Isbn10(isbn10String)
    val result = bookRepository.find(isbn10)
    if (result.isDefined) {
      val book = result.get
      val results = ShopsContainer.shopsList
        .map(shop => (shop, shop.isbn10ToUrl(isbn10), shop.isbn10ToPrice(isbn10)))
        .sortWith({
          case ((_, _, price1), (_, _, price2)) =>
            if (price1.isDefined && price2.isDefined) price1.get < price2.get
            else price1.isDefined
        })
      var price = if (results.isEmpty) None else results(0)._3
      Ok(views.html.books.show(book, price, results))

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
