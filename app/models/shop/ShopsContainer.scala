package models.shop

import shop.BookDepositoryShop

object ShopsContainer {
  var shopsList: Array[AbstractShop] = Array(
    AlibrisShop,
    AmazonShop,
    BarnesAndNobleShop,
    BookDepositoryShop,
  )
}