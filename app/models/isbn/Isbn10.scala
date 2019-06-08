package models.isbn

object Isbn10 {
  val Regex = "[0-9]{9}[0-9Xx]"
}

class Isbn10(val isbnString: String) extends Isbn(isbnString) {

  override def verify(isbnString: String): Boolean = {
    isbnString.matches(Isbn10.Regex)
  }

  override def toIsbn13: Isbn13 = {
    val rawIsbn = isbnString.take(9)

    /*
    val sum = 38 + rawIsbn.toArray.map(_.toString.toInt).zipWithIndex.map({
      case (digit, index) => {
        (if (index % 2 == 1) 1 else 3) * digit
      }
    }).sum
    */
    val sum = rawIsbn
      .toArray
      .map(_.toString.toInt)
      .zip(Array(3,1,3,1,3,1,3,1,3))
      .map({ case (digit, weight) => digit * weight })
      .sum + 38 // 9*1 + 7*3 + 8*1 = 38

    val checkDigit = (10 - sum % 10) % 10

    new Isbn13("978" + rawIsbn + checkDigit.toString)
  }

  def toDirty: String = {
    val last = isbnString.charAt(9)
    if (last == 'X' || last == 'x') isbnString else isbnString.toLong.toString
  }

  def toPretty: String = {
    Array(
      isbnString.charAt(0),
      isbnString.substring(1, 5),
      isbnString.substring(5, 9),
      isbnString.charAt(9)
    ).mkString("-")
  }

}
