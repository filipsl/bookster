package models.shop

import models.isbn.{Isbn, Isbn10}

import scala.math.BigDecimal

object BarnesAndNobleShop extends AbstractShop("Barnes & Noble",
  "https://www.barnesandnoble.com",
  "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Barnes_%26_Noble_logo.svg/250px-Barnes_%26_Noble_logo.svg.png") {

  override def isbnToUrl(isbn: Isbn): String = "https://www.barnesandnoble.com/w/?ean=" + isbn.toIsbn13.toString + "#/"

  override def isbn10ToPrice(isbn10: Isbn10): Option[BigDecimal] = {
    var html = isbnToHtml(isbn10.toIsbn13)
    //    println(html)
    val pricePattern = "[0-9]+[.][0-9]+".r
    if (html.contains("<title>No Results Page")) {
      None
    }
    else {
      if (html.contains("<span id=\"pdp-cur-price\" class=\"price current-price ml-0\"><sup>$</sup>")) {
        html = html.drop(html.indexOfSlice("<span id=\"pdp-cur-price\" class=\"price current-price ml-0\"><sup>$</sup>"))
        if (pricePattern.findFirstIn(html).isDefined) {
          val priceString: String = pricePattern.findFirstIn(html).get
          return Some(BigDecimal(priceString))
        }
        else
          None
      }
    }
    //9789876543217
    //9781416524793
    None
  }
}
