package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal

object BarnesAndNobleShop extends AbstractShop(
  "Barnes & Noble",
  "https://www.barnesandnoble.com",
  "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Barnes_%26_Noble_logo.svg/250px-Barnes_%26_Noble_logo.svg.png"
) {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.barnesandnoble.com/w/?ean=" + isbn10.toIsbn13.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {
    var html = htmlVal
    val pricePattern = "[0-9]+\\.[0-9]+".r
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
    None
  }
}
