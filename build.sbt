val projectName = "twitter-api"

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
	"com.typesafe.akka" %% "akka-actor" % "2.5.1",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.1",
  "com.typesafe.akka" %% "akka-http" % "10.0.6",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.6",
  "de.heikoseeberger" %% "akka-http-circe" % "1.16.0"
)

lazy val others = Seq(
	"org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
)

lazy val dependencies = Seq(
	libraryDependencies ++= akka ++ circle ++ others
)

scalacOptions += "-feature"

lazy val mainClassName = "edu.jproyo.dojos.twitter.api.WebApp"

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(dependencies: _*).
  settings(
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
