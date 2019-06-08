import org.scalatestplus.play._

import models.isbn._

class IsbnSpec extends PlaySpec {

  "Isbn" must {
    "verify" in {
      2+2 mustBe 4
    }

    /*
    "throw NoSuchElementException if an empty stack is popped" in {
      val emptyStack = new mutable.Stack[Int]
      a[NoSuchElementException] must be thrownBy {
        emptyStack.pop()
      }
    }
    */
  }
}