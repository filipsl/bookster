package controllers

import javax.inject._
import models.isbn.Isbn10
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index()).withSession("foo" -> "bar")
  }

}
