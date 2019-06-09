import exceptions.InvalidIsbnException
import org.scalatestplus.play._
import models.isbn._

class Isbn13Spec extends PlaySpec {

  "Isbn13 class" must {

    "instantiate a new Isbn13 object when valid" +
      " string is passed to constructor" in {
      val isbn13test = new Isbn13("9780374214913")
      assert(isbn13test.isInstanceOf[Isbn13])
    }

    "throw InvalidIsbnException if invalid string is passed to constructor" in {
      a[InvalidIsbnException] must be thrownBy {
        val isbn13est = new Isbn13("97dd37g21f91s")
      }
    }

    "return valid string in toString" in {
      val isbn13test = new Isbn13("9780743269513")
      assert(isbn13test.toString == "9780743269513")
    }

    "return the same Isbn13 when toIsbn13 is called" in {
      val isbn13test = new Isbn13("9780689862212")
      assert(isbn13test.toIsbn13 == isbn13test)
    }

    "be equal to another instance with the same isbnString" in {
      val isbn13test1 = new Isbn13("9780450574580")
      val isbn13test2 = new Isbn13("9780450574580")
      assert(isbn13test1 == isbn13test2)
    }

  }

}