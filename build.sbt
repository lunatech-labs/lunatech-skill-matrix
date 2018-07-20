name := """lunatech-tech-matrix"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

val slickPgVersion = "0.15.3"
val playSlickEvolutions = "3.0.1"

lazy val AcceptanceTest = config("acc") extend Test
lazy val IntegrationTest = config("it") extend Test
lazy val PactDependentTest = config("pd") extend Test
lazy val All = config("all") extend Test

val isAcceptanceTest: String => Boolean = _ startsWith "acceptance"
val isIntegrationTest: String => Boolean = _ startsWith "integration"
val isPactDependentTest: String => Boolean = _ startsWith "pactdependent"

val isUnitTest: String => Boolean = name => !(isIntegrationTest(name) || isAcceptanceTest(name) || isPactDependentTest(name))

val isAll: String => Boolean = name => isUnitTest(name) || isIntegrationTest(name) || isAcceptanceTest(name) || isPactDependentTest(name)


lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(AcceptanceTest)
  .configs(IntegrationTest)
  .configs(PactDependentTest)
  .configs(All)
  .settings(inConfig(AcceptanceTest)(Defaults.testTasks): _*) //adds the tasks and settings for the new test configuration
  .settings(inConfig(IntegrationTest)(Defaults.testTasks): _*) //adds the tasks and settings for the new test configuration
  .settings(inConfig(PactDependentTest)(Defaults.testTasks): _*) //adds the tasks and settings for the new test configuration
  .settings(inConfig(All)(Defaults.testTasks): _*) //adds the tasks and settings for the new test configuration
  .settings(testOptions in AcceptanceTest := Seq(Tests.Filter(isAcceptanceTest)))
  .settings(testOptions in IntegrationTest := Seq(Tests.Filter(isIntegrationTest)))
  .settings(testOptions in PactDependentTest := Seq(Tests.Filter(isPactDependentTest)))
  .settings(testOptions in All := Seq(Tests.Filter(isAll)))
  .settings(javaOptions in Test += "-Dconfig.file=test/application-test.conf")
  .settings(testOptions in Test := Seq(Tests.Filter(isUnitTest)))

libraryDependencies ++= Seq(
  ehcache,
  ws,
  evolutions,
  jdbc,
  guice,
  "ch.qos.logback"             % "logback-classic"                % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"                 % "3.5.0",
  "com.github.julien-truffaut" %% "monocle-core"                  % "1.4.0-M1",
  "com.github.julien-truffaut" %% "monocle-macro"                 % "1.4.0-M1",
  "com.typesafe.slick"         %% "slick"                         % "3.2.1",
  "com.typesafe.slick"         %% "slick-hikaricp"                % "3.2.1",
  "com.typesafe.play"          %% "play-slick"                    % playSlickEvolutions,
  "com.typesafe.play"          %% "play-slick-evolutions"         % playSlickEvolutions,
  "com.github.tminglei"        %% "slick-pg"                      % slickPgVersion,
  "com.github.tminglei"        %% "slick-pg_play-json"            % slickPgVersion,
  "com.github.tminglei"        %% "slick-pg_joda-time"            % slickPgVersion,
  "org.postgresql"             % "postgresql"                     % "9.3-1102-jdbc41",
  "org.scalaz"                 %% "scalaz-core"                   % "7.2.8",
  "io.kanaka"                  %% "play-monadic-actions"          % "2.1.0",

  "joda-time"                  % "joda-time"                      % "2.9.9",

  "com.google.api-client"      % "google-api-client"              % "1.22.0",
  "com.google.http-client"     % "google-http-client-jackson"     % "1.22.0",
  "io.circe"                   %% "circe-core"                    % "0.7.0",
  "io.circe"                   %% "circe-generic"                 % "0.7.0",
  "ch.qos.logback"             % "logback-classic"                % "1.1.7",
  "com.typesafe.play"          %% "play-json"                     % "2.6.3",

  "com.h2database"             % "h2"                             % "1.4.196" % Test,
  "org.scalatestplus.play"     %% "scalatestplus-play"            % "3.1.1"   % Test,
  "com.atlassian.oai"          % "swagger-request-validator-core" % "1.0.7"   % Test,
  "com.itv"                    %% "scalapact-scalatest"           % "2.1.3"   % Test,
  "ru.yandex.qatools.embed"    % "postgresql-embedded"            % "2.4"     % Test
)

coverageExcludedPackages := """controllers\..*Reverse.*;router.Routes.*;views.html.*"""