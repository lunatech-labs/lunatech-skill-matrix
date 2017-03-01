
name := """lunatech-tech-matrix"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val AcceptanceTest = config("acc") extend Test
val isAcceptanceTest: String => Boolean = _ startsWith "acceptance"


lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(AcceptanceTest)
  .settings(inConfig(AcceptanceTest)(Defaults.testTasks): _*) //adds the tasks and settings for the new test configuration
  .settings(
    testOptions in AcceptanceTest := Seq(Tests.Filter(isAcceptanceTest))
  ).settings(
    javaOptions in Test += "-Dconfig.file=test/application-test.conf"
  )

libraryDependencies ++= Seq(
  cache,
  ws,
  evolutions,
  jdbc,
  "com.github.julien-truffaut" %% "monocle-core"  % "1.4.0-M1",
  "com.github.julien-truffaut" %% "monocle-macro" % "1.4.0-M1",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalaz" %% "scalaz-core" % "7.2.8",
  "io.kanaka" %% "play-monadic-actions" % "2.0.0",

  "com.google.api-client" % "google-api-client" % "1.22.0",
  "com.google.http-client" % "google-http-client-jackson" % "1.22.0",
  "io.circe" %% "circe-core" % "0.7.0",
  "io.circe" %% "circe-generic" % "0.7.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7",

  "com.h2database" % "h2" % "1.3.148" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

)

