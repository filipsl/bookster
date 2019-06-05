package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal

object AlibrisShop extends AbstractShop(
  "Alibris",
  "https://www.alibris.com",
  "https://www3.alibris-static.com/images/red/nav/alibris-logo.gif"
) {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.alibris.com/booksearch?keyword=" + isbn10.toIsbn13.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {
    var html = htmlVal
    if (html.contains("New\n</a>")) {
      html = html.drop(html.indexOfSlice("New\n</a>"))
      html = html.drop(html.indexOfSlice("<meta itemprop=\"price\" content=\""))
      val pricePattern = "[0-9]+\\.[0-9]+".r
      if (pricePattern.findFirstIn(html).isDefined) {
        val priceString: String = pricePattern.findFirstIn(html).get
        Some(BigDecimal(priceString))
      }
      else
        None
    }
    else
      None
  }
}