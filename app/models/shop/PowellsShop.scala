package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal


object PowellsShop extends AbstractShop("Powell's Books",
  "https://www.powells.com/",
  "https://www.powells.com/Portals/0/logo-0714.jpg") {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.powells.com/book/-" + isbn10.toIsbn13.toString


  override def htmlToPrice(htmlVal: String): Option[BigDecimal] ={

    val pattern1 = "<div class=\"price\" style=\"display: block;\">"

    htmlVal match {
      case x if x.contains(pattern1) =>
        Some(BigDecimal(pricePattern.findFirstIn(x.drop(x.indexOfSlice(pattern1))).get))
      case _ => None
    }
  }
}
