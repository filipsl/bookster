package models.shop

import models.isbn.Isbn10
import scala.math.BigDecimal

object AmazonShop extends AbstractShop(
  "Amazon",
  "https://www.amazon.com",
  "https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg"
) {

  override def isbn10ToUrl(isbn10: Isbn10): String = "https://www.amazon.com/dp/" + isbn10.toString

  override def htmlToPrice(htmlVal: String): Option[BigDecimal] = {
    var html = htmlVal
    val pricePattern = "[0-9]+\\.[0-9]+".r
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
