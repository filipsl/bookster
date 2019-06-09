import org.scalatestplus.play._
import models.isbn._
import exceptions.InvalidIsbnException

class Isbn10Spec extends PlaySpec {

  "Isbn10" must {

    "instantiate a new Isbn10 object when valid" +
      " string is passed to constructor" in {
      val isbn10test = new Isbn10("0060530928")
      assert(isbn10test.isInstanceOf[Isbn10])
    }

    "throw InvalidIsbnException if invalid string is passed to constructor" in {
      a[InvalidIsbnException] must be thrownBy {
        val isbn10test = new Isbn10("sffs98fs")
      }
    }

    "return valid string in toString" in {
      val isbn10test = new Isbn10("0061120081")
      assert(isbn10test.toString == "0061120081")
    }

    "return ISBN without leading zeros when ISBN does not end with X/x" in {
      val isbn10test = new Isbn10("0156012197")
      assert(isbn10test.toDirty == "156012197")
    }

    "return ISBN with leading zeros when ISBN ends with X/x" in {
      val isbn10test = new Isbn10("006075995X")
      assert(isbn10test.toDirty == "006075995X")
    }

    "return formatted ISBN10" in {
      val isbn10test = new Isbn10("1595144293")
      assert(isbn10test.toPretty == "1-5951-4429-3")
    }

    "return valid ISBN13 when toIsbn13 is called" in {
      val isbn10test = new Isbn10("045057458X")
      val isbn13test = isbn10test.toIsbn13
      assert(isbn13test.toString == "9780450574580")
    }

    "be equal to another instance with the same isbnString" in {
      val isbn10test1 = new Isbn10("045057458X")
      val isbn10test2 = new Isbn10("045057458X")
      assert(isbn10test1 == isbn10test2)
    }
  }
}