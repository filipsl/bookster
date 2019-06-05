package models

import models.isbn.Isbn10

class Book(
  val isbn10: Isbn10,
  val authors: String,
  val title: String,
  val publicationYear: Int,
  val coverUrl: String,
  val averageRating: Double,
  val ratingsCount: Long
) {

}
