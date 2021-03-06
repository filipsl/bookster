name := "bookster"
 
version := "1.0" 
      
lazy val `bookster` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.3",
  "org.apache.spark" %% "spark-sql" % "2.4.3",
  "org.apache.spark" %% "spark-mllib" % "2.4.3",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9",
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.0" % "test"
)
