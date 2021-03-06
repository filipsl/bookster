import com.google.inject.AbstractModule
import services.PricesService
import repositories.{BookRepository, SparkBookRepository}

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure(): Unit = {

    // Ask Guice to create an instance of SparkBookRepository when the application starts.
    bind(classOf[BookRepository]).to(classOf[SparkBookRepository]).asEagerSingleton()

    bind(classOf[PricesService]).asEagerSingleton()

  }

}
