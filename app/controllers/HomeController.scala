package controllers

import javax.inject._
import models.Book
import models.isbn.Isbn10
import play.api.mvc._
import repositories.BookRepository

@Singleton
class HomeController @Inject()(cc: ControllerComponents, bookRepository: BookRepository) extends AbstractController(cc) {

  def index = Action {
    val coverUrls = Array[String]() // bookRepository.mostPopularIsbns(1000).map(Book.isbnToCoverUrl)
    Ok(views.html.index(coverUrls))
  }

}
