name := "UpisStats"

version := "1.0-BETA"

lazy val root = project.in(file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies ++= Seq(
  javaJdbc, cache, javaWs,
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.google.code.gson" % "gson" % "2.8.1",
  "org.jsoup" % "jsoup" % "1.9.1",
  "com.github.luq-0" % "UpisScrapper" % "-SNAPSHOT"
)