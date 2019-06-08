package services.shops

import java.net.SocketTimeoutException

import models.isbn._
import scalaj.http.Http
import scalaj.http.HttpOptions

import scala.math.BigDecimal
import scala.util.matching.Regex

abstract class AbstractShop(val name: String, val url: String, val logoUrl: String) {

  protected val pricePattern: Regex = "[0-9]+\\.[0-9]+".r

  def isbn10ToPrice(isbn10: Isbn10): Option[BigDecimal] = {
    val url = isbn10ToUrl(isbn10)
    try {
      val html = Http(url)
        .option(HttpOptions.followRedirects(true))
        .header("User-Agent", "Bookster-Bot")
        .header("Accept-Language", "en-US")
        .asString
        .toString
      val price = htmlToPrice(html)
      price
    } catch {
      case _: SocketTimeoutException => None
    }
  }

  def isbn10ToUrl(isbn10: Isbn10): String

  def htmlToPrice(html: String): Option[BigDecimal]

}
