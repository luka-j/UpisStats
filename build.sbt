name := "UpisStats"

version := "1.0-BETA"

lazy val root = project.in(file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc, cache, javaWs,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "org.jsoup" % "jsoup" % "1.9.1"
)

