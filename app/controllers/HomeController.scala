package controllers

import javax.inject._
import play.api.mvc._
import repositories.BookRepository

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

}
