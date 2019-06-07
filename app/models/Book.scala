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
    if (_coverUrl.isDefined) _coverUrl.get else controllers.routes.Assets.versioned("images/default_cover.png").url
  }

}
