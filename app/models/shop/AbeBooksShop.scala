package models.shop

import models.isbn.Isbn10

import scala.math.BigDecimal


object AbeBooksShop extends AbstractShop("AbeBooks",
  "https://www.abebooks.com/",
  "https://upload.wikimedia.org/wikipedia/en/e/e3/Abebooks-logo.png") {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.abebooks.com/products/isbn/" + isbn10.toIsbn13.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] ={

    val pattern1 = "The ISBN is not valid"
    val pattern2 = "<span class=\"priceNoBold x-large\">US$ "

    htmlVal match {
      case x if x.contains(pattern1) => None
      case x if x.contains(pattern2)
            => Some(BigDecimal(pricePattern.findFirstIn(x
              .drop(x.indexOfSlice(pattern2))).get))
      case _ => None
    }

  }
}
