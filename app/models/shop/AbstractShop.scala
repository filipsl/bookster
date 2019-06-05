package models.shop

import models.isbn._
import scalaj.http.Http
import scalaj.http.HttpOptions

abstract class AbstractShop(val name: String, val url: String, val logoUrl: String) {

  def isbnToUrl(isbn: Isbn): String // = queryUrl.replace("@", isbn.toString)

  def isbn10ToPrice(isbn10: Isbn10): Option[BigDecimal]

  def isbnToHtml(isbn: Isbn): String = {
    val html = Http(isbnToUrl(isbn))
      .option(HttpOptions.followRedirects(true))
      .header("User-Agent", "Opera")
      .asString
    html.toString
  }

}
