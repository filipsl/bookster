package services.shops

import models.isbn.Isbn10
import scala.math.BigDecimal


object BetterWorldBooksShop extends AbstractShop("Better World Books",
  "https://www.betterworldbooks.com/",
  "https://upload.wikimedia.org/wikipedia/en/a/a9/BetterWorldBooksLogo.svg") {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.betterworldbooks.com/product/detail/-" + isbn10.toIsbn13.toString


  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {

    val pattern1 = "data-price=\""

    htmlVal match {
      case x if x.contains(pattern1) =>
        Some(BigDecimal(pricePattern.findFirstIn(x.drop(x.indexOfSlice(pattern1))).get))
      case _ => None
    }
  }
}
