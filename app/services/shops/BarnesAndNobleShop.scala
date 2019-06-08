package services.shops

import models.isbn.Isbn10
import scala.math.BigDecimal

object BarnesAndNobleShop extends AbstractShop(
  "Barnes & Noble",
  "https://www.barnesandnoble.com",
  "https://upload.wikimedia.org/wikipedia/commons/a/ae/Barnes_%26_Noble_logo.svg"
) {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.barnesandnoble.com/w/?ean=" + isbn10.toIsbn13.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {

    val pattern1 = "<title>No Results Page"
    val pattern2 = "<span id=\"pdp-cur-price\" class=\"price current-price ml-0\"><sup>$</sup>"

    htmlVal match{
      case x if x.contains(pattern1) => None
      case x if x.contains(pattern2) =>
        Some(BigDecimal(pricePattern
          .findFirstIn(x.drop(x.indexOfSlice(pattern2))).get))
      case _ => None
    }
  }
}
