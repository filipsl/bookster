/*package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal

object BookDepositoryShop extends AbstractShop(
  "Book Depository",
  "https://www.bookdepository.com",
  "https://upload.wikimedia.org/wikipedia/commons/8/8a/The_Book_Depository.svg"
) {
  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.bookdepository.com/w/" + isbn10.toIsbn13.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {
    println(htmlVal)
    None
  }
}
*/