package repositories

import exceptions.{NoRatingsException, NoTermsException}
import javax.inject._

import scala.concurrent.Future
import models.{Book, Ratings}
import models.isbn.Isbn10
import play.api.inject.ApplicationLifecycle
import org.apache.spark.SparkConf
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.mllib.recommendation._

@Singleton
class SparkBookRepository @Inject() (appLifecycle: ApplicationLifecycle) extends BookRepository {

  // This code is called when the application starts.
  private val ratingsSchema = StructType(
    List(
      StructField("user_id", IntegerType, true),
      StructField("book_id", IntegerType, true),
      StructField("rating", DoubleType, true)
    )
  )

  private val spark = {
    val conf = new SparkConf().setAppName("bookster").setMaster("local[*]")
    val spark = SparkSession.builder.config(conf).getOrCreate()

    var reader = spark.read
      .format("csv")
      .option("sep", ",")
      .option("header", "true")

    reader.load("resources/books.csv")
      .createOrReplaceTempView("books")

    reader.schema(ratingsSchema)
      .load("resources/ratings.csv")
      // .where("INT(user_id) > 100 AND INT(user_id) < 20000")
      .createOrReplaceTempView("ratings")

    spark
  }

  private val model = {
    new ALS().setIterations(10)
      .setBlocks(-1)
      .setAlpha(1)
      .setLambda(0.1)
      .setRank(20)
      .setSeed(321456L)
      .setImplicitPrefs(true)
  }

  private val _ratingsCount = 5976479 // spark.sql("SELECT * FROM ratings").count()
  private val _booksCount = 10000 // spark.sql("SELECT INT(book_id) FROM ratings").distinct().count()
  private val _usersCount = 53424 // spark.sql("SELECT INT(user_id) FROM ratings").distinct().count()

  def ratingsCount: Long = _ratingsCount
  def booksCount: Long = _booksCount
  def usersCount: Long = _usersCount

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
      val unorderedBooksArray = getManyBySql("SELECT * FROM books WHERE book_id IN (" + ids.mkString(", ") + ")")
      val booksByIdMap = unorderedBooksArray.toSeq.groupBy(_.id)
      ids.toSeq.flatMap(id => booksByIdMap.getOrElse(id, Seq.empty)).toArray
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

    if (terms.isEmpty) throw new NoTermsException

    val sql = Seq(
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
    getManyBySql("SELECT * FROM books WHERE isbn IS NOT NULL AND isbn != \"\" ORDER BY RAND() ASC LIMIT " + n*2).take(n)
  }

  override def recommend(ratings: Ratings, n: Int): Array[Book] = {
    if (ratings.isEmpty) throw new NoRatingsException

    val data = ratings.toMap.map({
      case (bookId, rating) => Row(0, bookId.toInt, rating.toDouble)
    }).toSeq
    // val maxBookId = ratings.keys.max

    val ownRatingsDF = spark.createDataFrame(spark.sparkContext.parallelize(data), ratingsSchema)

    val trainingData = spark.sql("SELECT * FROM ratings").union(ownRatingsDF)
    // WHERE book_id <= " + maxBookId

    val ratingsRDD = trainingData.rdd.map(row => Rating(row.getInt(0), row.getInt(1), row.getDouble(2)))

    try {
      val topRecsForUser = model.run(ratingsRDD)
        .recommendProducts(0, n * 2)
        .map(rating => rating.product)
        .map(_.toLong)
        .filter(bookId => !(ratings.bookIds contains bookId))
      findManyByIds(topRecsForUser).take(n)
    } catch {
      case _: IllegalArgumentException => throw new NoRatingsException
    }
  }

  /*
  override def mostPopularIsbns(n: Int): Array[Isbn10] = {
    val sql = Array(
      "SELECT isbn FROM books",
      "WHERE isbn IS NOT NULL AND isbn != \"\"",
      "ORDER BY INT(book_id) ASC",
      "LIMIT " + n
    ).mkString(" ")

    spark.sql(sql)
      .rdd.collect
      .map(row => new Isbn10(row.getString(0).reverse.padTo(10, '0').reverse))
  }
  */
}
