val projectName = "twitter-api"

resolvers += Resolver.sonatypeRepo("releases")

lazy val commonSettings = Seq(
  organization := "edu.jproyo.dojos",
  name := projectName,
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.2"
)

val circeVersion = "0.8.0"

lazy val circle = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val akka = Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.4.19",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.19",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.9",
  "de.heikoseeberger" %% "akka-http-circe" % "1.16.0"
)

val twitterVersion = "5.1"
val cacheVersion = "0.9.4"

lazy val twitter = Seq(
  "com.danielasfregola" %% "twitter4s" % twitterVersion,
  "com.github.cb372" %% "scalacache-guava" % cacheVersion
)

lazy val others = Seq(
	"org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.github.kxbmap" %% "configs" % "0.4.4"
)

lazy val testing = Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "it,test",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % "it,test"
)

lazy val dependencies = Seq(
	libraryDependencies ++= akka ++ circle ++ twitter ++ others ++ testing
)

scalacOptions += "-feature"
coverageEnabled := true

lazy val mainClassName = "edu.jproyo.dojos.twitter.api.WebApp"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(dependencies: _*)
  .settings(Defaults.itSettings: _*)
  .settings(
  	mainClass in Compile := Some(mainClassName)
  )

  packAutoSettings ++ Seq(
    packMain := Map(projectName -> mainClassName),
    packJvmOpts := Map(projectName -> Seq("-Xmx2048m")),
    packGenerateWindowsBatFile := false,
    packJarNameConvention := "default",
    packExpandedClasspath := false,
    packResourceDir += (baseDirectory.value / "web" -> "web-content")
  )
