package controllers

import javax.inject._
import play.api.mvc._
import repositories.BookRepository

@Singleton
class HomeController @Inject()(cc: ControllerComponents, bookRepository: BookRepository) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index())
  }

}
