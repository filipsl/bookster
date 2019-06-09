import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._
import javax.inject.Singleton

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful({
      val error = if (message.nonEmpty) {
        message
      } else {
        statusCode match {
          case 404 => "404 Not Found"
          case _ => statusCode.toString
        }
      }
      Status(statusCode)(views.html.error(error))
    })
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(
      InternalServerError(views.html.error(exception.getMessage))
    )
  }
}