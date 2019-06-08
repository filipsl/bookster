package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal


object ValoreBooksShop extends AbstractShop("ValoreBooks",
  "https://www.valorebooks.com/",
  "https://bookscouter.com/dist/images/logos/vendors/logo-valorebooks.png") {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.valorebooks.com/textbooks/+/" + isbn10.toIsbn13.toString


  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {

    val pattern1 = "No results match your search"
    val pattern2 = "\"price\": \""

    htmlVal match{
      case x if x.contains(pattern1) => None
      case x if x.contains(pattern2) =>
        Some(BigDecimal(pricePattern
          .findFirstIn(x.drop(x.indexOfSlice(pattern2))).get))
      case _ => None
    }
  }
}
