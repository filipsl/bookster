package services.repositories

import javax.inject._
import models.Book
import models.isbn.Isbn10
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

@Singleton
class SparkBookRepository @Inject() (appLifecycle: ApplicationLifecycle) extends BookRepository {

  // This code is called when the application starts.
  private val spark = {
    val conf = new SparkConf().setAppName("bookster-test").setMaster("local[*]")
    val spark = SparkSession.builder.config(conf).getOrCreate()

    val df1 = spark.read.format("csv")
      .option("sep", ",")
      .option("header", "true")
      .load("resources/books.csv")

    val moviesDF = df1.select(df1.col("isbn"), df1.col("original_title"), df1.col("image_url"))
    moviesDF.show(false)
    moviesDF.createOrReplaceTempView("books")

    spark
  }

  // When the application starts, register a stop hook with the
  // ApplicationLifecycle object. The code inside the stop hook will
  // be run when the application stops.
  appLifecycle.addStopHook { () =>
    Future.successful(spark.stop())
  }

  override def find(isbn10: Isbn10): Book = {
    try {
      val tuple = spark.sql("select * from books where isbn = " + isbn10.toDirty).rdd.map(row => {
        (row.getString(1), row.getString(2))
      }).first

      val coverUrl = if (tuple._2 == "https://s.gr-assets.com/assets/nophoto/book/111x148-bcc042a9c91a29c1d680899eff700a03.png") "/assets/images/default_cover.png" else tuple._2

      new Book(tuple._1, coverUrl, isbn10)
    } catch {
      case e: UnsupportedOperationException => throw new IllegalArgumentException("Book not found")
    }

  }

}
