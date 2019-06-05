package models.shop
import models.isbn.{Isbn, Isbn10}
import scalaj.http.{Http, HttpOptions}
import models.shop.AlibrisShop.isbnToUrl

import scala.math.BigDecimal

object AmazonShop extends AbstractShop("Amazon",
  "https://www.amazon.com",
  "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/175px-Amazon_logo.svg.png") {

  override def isbnToUrl(isbn: Isbn): String = "https://www.amazon.com/dp/" + isbn.toString

  override def isbn10ToPrice(isbn10: Isbn10): Option[BigDecimal] = {
//    println(isbnToHtml(isbn10))
    var html = isbnToHtml(isbn10)
    val pricePattern = "[0-9]+[.][0-9]+".r
    if(html.contains("New</a> from <span class='a-color-price'>$")) {
      html = html.drop(html.indexOfSlice("New</a> from <span class='a-color-price'>$"))
      if (pricePattern.findFirstIn(html).isDefined) {
        val priceString: String = pricePattern.findFirstIn(html).get
        return Some(BigDecimal(priceString))
      }
    }
      else if(html.contains("<span class=\"a-size-medium a-color-price offer-price a-text-normal\">$")){
        html = html.drop(html.indexOfSlice("<span class=\"a-size-medium a-color-price offer-price a-text-normal\">$"))
//        println(html)
        if(pricePattern.findFirstIn(html).isDefined) {
          val priceString: String = pricePattern.findFirstIn(html).get
          return Some(BigDecimal(priceString))
        }
      else
        None
    }
    None
  }
}
