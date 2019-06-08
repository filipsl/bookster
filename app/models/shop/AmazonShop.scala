package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal

object AmazonShop extends AbstractShop(
  "Amazon",
  "https://www.amazon.com",
  "https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg"
) {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.amazon.com/dp/" + isbn10.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {

    val pattern1 = "New</a> from <span class='a-color-price'>$"
    val pattern2 = "<span class=\"a-size-medium a-color-price offer-price a-text-normal\">$"

    htmlVal match{
      case x if x.contains(pattern1) =>
        Some(BigDecimal(pricePattern
          .findFirstIn(x.drop(x.indexOfSlice(pattern1))).get))
      case x if x.contains(pattern2) =>
        Some(BigDecimal(pricePattern
          .findFirstIn(x.drop(x.indexOfSlice(pattern2))).get))
      case _ => None
    }
  }
}
