package models.shop

import models.isbn.{Isbn, Isbn10}

import scala.math.BigDecimal

object AlibrisShop extends AbstractShop("Alibris",
  "https://www.alibris.com",
  "https://www3.alibris-static.com/images/red/nav/alibris-logo.gif") {

  override def isbnToUrl(isbn: Isbn): String = "https://www.alibris.com/booksearch?keyword=" + isbn.toIsbn13.toString

  override def isbn10ToPrice(isbn10: Isbn10): Option[BigDecimal] = {
    println(isbn10.toIsbn13)
    var html = isbnToHtml(isbn10.toIsbn13)
//    println(html)
    if (html.contains("New\n</a>")){
      html = html.drop(html.indexOfSlice("New\n</a>"))
      html = html.drop(html.indexOfSlice("<meta itemprop=\"price\" content=\""))
      val pricePattern = "[0-9]+[.][0-9]+".r
      if(pricePattern.findFirstIn(html).isDefined){
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
