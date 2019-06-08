package models.shop

object ShopsContainer {
  var shopsList: Array[AbstractShop] = Array(
    AbeBooksShop,
    AlibrisShop,
    AmazonShop,
    BarnesAndNobleShop,
    BetterWorldBooksShop,
    PowellsShop,
    TatteredCoverShop,
    ValoreBooksShop
  )
}