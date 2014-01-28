organization := "com.foursquare"

name := "twitter-util-async"

version := "0.1"

scalaVersion := "2.10.3"

resolvers += "Foursquare Nexus" at "http://nexus.prod.foursquare.com/nexus/content/groups/public/"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-async" % "0.9.0-M4-fs-a",
  "com.twitter" %% "util-core" % "6.5.0",
  "junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.7" % "test"
)
