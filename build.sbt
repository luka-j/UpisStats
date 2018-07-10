name := "UpisStats"

version := "1.0-BETA"

lazy val root = project.in(file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.12"

resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies ++= Seq(
  javaJdbc, ehcache, javaWs, guice,
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.github.luq-0" % "UpisScrapper" % "1.2.2.1",
  "com.typesafe.play" %% "play-json" % "2.6.0"
)