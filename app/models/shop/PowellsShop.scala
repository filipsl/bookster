/*package shop

import models.isbn.Isbn10
import scalaj.http.{Http, HttpOptions}
import shop.AlibrisShop.isbnToUrl

//TODO add price search

object PowellsShop extends AbstractShop("Powell's Books",
  "https://www.powells.com/",
  "https://www.powells.com/book/-@",
  "https://www.powells.com/Portals/0/logo-0714.jpg") {

  override def isbn10ToPrice(isbn10: Isbn10): Option[BigDecimal] = {
//    println(isbn10ToHtml(isbn10))
    None
  }

}
*/