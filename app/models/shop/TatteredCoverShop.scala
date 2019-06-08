package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal


object TatteredCoverShop extends AbstractShop("Tattered Cover",
  "https://www.tatteredcover.com/",
  "https://www.tatteredcover.com/sites/tatteredcover.com/files/tc-logo-horizontal_0.png") {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.tatteredcover.com/book/" + isbn10.toIsbn13.toString


  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {

    val pattern1 = "Please email or write store for pricing and availability information."
    val pattern2 = "<div class=\"abaproduct-price\">"

    htmlVal match{
      case x if x.contains(pattern1) => None
      case x if x.contains(pattern2) =>
        Some(BigDecimal(pricePattern
          .findFirstIn(x.drop(x.indexOfSlice(pattern2))).get))
      case _ => None
    }
  }
}
