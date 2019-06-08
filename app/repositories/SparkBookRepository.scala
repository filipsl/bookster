package repositories

import javax.inject._
import scala.concurrent.Future
import models.Book
import models.isbn.Isbn10
import play.api.inject.ApplicationLifecycle
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}

@Singleton
class SparkBookRepository @Inject() (appLifecycle: ApplicationLifecycle) extends BookRepository {

  // This code is called when the application starts.
  private val spark = {
    val conf = new SparkConf().setAppName("bookster").setMaster("local[*]")
    val spark = SparkSession.builder.config(conf).getOrCreate()

    var reader = spark.read
      .format("csv")
      .option("sep", ",")
      .option("header", "true")

    reader.load("resources/books.csv")
      .createOrReplaceTempView("books")

    reader.load("resources/ratings.csv")
      .createOrReplaceTempView("ratings")

    spark
  }

  // When the application starts, register a stop hook with the
  // ApplicationLifecycle object. The code inside the stop hook will
  // be run when the application stops.
  appLifecycle.addStopHook { () =>
    Future.successful(spark.stop())
  }

  private def rowToBook(row: Row): Option[Book] = {
    try {
      Some(new Book(
        row.getString(0).toLong,
        new Isbn10(row.getString(5).reverse.padTo(10, '0').reverse),
        row.getString(7),
        if (row.getString(9).isBlank) row.getString(10) else row.getString(9),
        row.getString(8).dropRight(2).toShort,
        if (row.getString(21) == "https://s.gr-assets.com/assets/nophoto/book/111x148-bcc042a9c91a29c1d680899eff700a03.png") None else Some(row.getString(21)),
        row.getString(12).toDouble,
        row.getString(13).toString.toLong
      ))
    } catch {
      case _: NullPointerException | _: NumberFormatException => None
    }
  }

  override def find(isbn10: Isbn10): Option[Book] = {
    val results = spark.sql(s"SELECT * FROM books WHERE isbn = '${isbn10.toDirty}'").rdd
    if (results.isEmpty()) None else Some(rowToBook(results.first).get)
  }

  override def findManyByIds(ids: Array[Long]): Array[Book] = {
    if (ids.isEmpty) {
      Array()
    } else {
      getManyBySql("SELECT * FROM books WHERE book_id IN (" + ids.mkString(", ") + ")")
    }
  }

  protected def getManyBySql(sql: String): Array[Book] = {
    spark.sql(sql)
      .rdd.collect
      .map(rowToBook)
      .filter(_.isDefined)
      .map(_.get)
  }

  override def search(q: String): Array[Book] = {
    val terms = q.toLowerCase
      .replaceAll("[^a-z0-9 ]", " ")
      .replaceAll("  ", " ")
      .split(" ")
      .filter(_.length >= 2)

    val sql = Array(
      "SELECT * FROM books",
      "WHERE " + (
        terms.map("CONCAT(LOWER(original_title), ' ', LOWER(authors)) LIKE '%" + _ + "%'")
          ++ Array("isbn IS NOT NULL")).mkString(" AND "),
      "ORDER BY INT(book_id) ASC",
      "LIMIT 100"
    ).mkString(" ")

    getManyBySql(sql)
  }

  override def random(n: Int): Array[Book] = {
    getManyBySql("SELECT * FROM books WHERE isbn IS NOT NULL ORDER BY RAND() ASC LIMIT " + n)
  }
}
