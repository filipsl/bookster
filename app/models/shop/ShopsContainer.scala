package models.shop

object ShopsContainer {
  var shopsList: Array[AbstractShop] = Array(
    AlibrisShop,
    AmazonShop,
    BarnesAndNobleShop
  )
}