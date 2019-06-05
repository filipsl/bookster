package controllers

import javax.inject._
import play.api.mvc._
import models.isbn.Isbn10
import models.shop.{AbstractShop, ShopsContainer}
import services.repositories.BookRepository

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BooksController @Inject()(cc: ControllerComponents, bookRepository: BookRepository) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def show(isbn10String: String) = Action {

    val isbn10 = new Isbn10(isbn10String)
    val book = bookRepository.find(isbn10)

    val results = ShopsContainer.shopsList.map(shop => (shop, shop.isbnToUrl(isbn10), shop.isbn10ToPrice(isbn10)))

    Ok(views.html.books.show(book, results))

  }

}
