package models

import models.isbn.Isbn10

class Book(
  val id: Long,
  val isbn10: Isbn10,
  val authors: String,
  val title: String,
  val publicationYear: Short,
  private val _coverUrl: Option[String],
  val averageRating: Double,
  val ratingsCount: Long
) extends Serializable {

  def coverUrl: String = {
    // if (_coverUrl.isDefined) _coverUrl.get else controllers.routes.Assets.versioned("images/default_cover.png").url
    // "https://prodimage.images-bn.com/pimages/" + isbn10.toIsbn13.toString + "_p0_v3_s550x406.jpg"
    "https://www.alibris-static.com/w/isbn/" + isbn10.toIsbn13.toString + "_l.jpg"
  }

}
