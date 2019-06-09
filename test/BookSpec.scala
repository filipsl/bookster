import models.Book
import org.scalatestplus.play._
import models.isbn._

class BookSpec extends PlaySpec {

  "Book" must {
    "have the same attributes as those that it was initialized with" in {
      val isbn10 = new Isbn10("0722536542")
      val book = new Book(2902, isbn10, "Paulo Coelho",
        "O Monte Cinco", 1996, None, 3.61, 26060)

      book.id mustBe 2902
      book.isbn10 mustBe new Isbn10("0722536542")
      book.authors mustBe "Paulo Coelho"
      book.title mustBe "O Monte Cinco"
      book.publicationYear mustBe 1996
      book.averageRating mustBe 3.61
      book.ratingsCount mustBe 26060
    }

    "return proper coverUrl" in {
      val isbn10 = new Isbn10("0722536542")
      Book.isbnToCoverUrl(isbn10) mustBe "https://img.valorebooks.com/FULL/97/9780/978072/9780722536544.jpg"
    }
  }
}