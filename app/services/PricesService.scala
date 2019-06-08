package services

import models.Book
import services.shops._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, TimeoutException}
import scala.concurrent.ExecutionContext.Implicits.global

class PricesService {

  private val shopsList: Array[AbstractShop] = Array(
    AbeBooksShop,
    AlibrisShop,
    AmazonShop,
    BarnesAndNobleShop,
    BetterWorldBooksShop,
    PowellsShop,
    TatteredCoverShop,
    ValoreBooksShop
  )

  def getPricesOfBook(book: Book): Array[(AbstractShop, String, Option[BigDecimal])] = {
    shopsList
      .map(shop => (shop, shop.isbn10ToUrl(book.isbn10), Future(shop.isbn10ToPrice(book.isbn10))))
      .map({
        case (shop, url, futurePrice) =>
          try {
            (shop, url, Await.result(futurePrice, 5 second))
          } catch {
            case _: TimeoutException => (shop, url, None)
          }
      })
      .sortWith({
        case ((_, _, price1), (_, _, price2)) =>
          if (price1.isDefined && price2.isDefined) price1.get < price2.get
          else price1.isDefined
      })
  }

}
