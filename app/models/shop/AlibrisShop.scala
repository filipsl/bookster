package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal

object AlibrisShop extends AbstractShop(
  "Alibris",
  "https://www.alibris.com",
  "https://cdn.codes.co.uk/img/merchants/100119/360-logo/v2/alibris-discount-codes.png"
) {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.alibris.com/booksearch?keyword=" + isbn10.toIsbn13.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {

    val pattern1 = "<td class=\"price\" valign=\"top\">"

    htmlVal match {
      case x if x.contains(pattern1) =>
        Some(BigDecimal(pricePattern.findFirstIn(x.drop(x.indexOfSlice(pattern1))).get))
      case _ => None
    }
  }
}