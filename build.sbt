organization := "com.foursquare"

name := "twitter-util-async"

version := "1.2.2"

isSnapshot := false

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.8",
  "org.scala-lang.modules" %% "scala-async" % "0.9.7",
  "com.twitter" %% "util-core" % "6.40.0",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

publishTo := {
  val jfrog = "https://foursquaredev.jfrog.io/foursquaredev/"
  if (isSnapshot.value)
    Some("snapshots" at jfrog + "thirdparty-snapshots/")
  else
    Some("central"  at jfrog + "thirdparty/")
}

credentials += Credentials(Path.userHome / ".ivy_credentials")

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/foursquare/twitter-util-async</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.scala-lang.org/license.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:foursquare/twitter-util-async.git</url>
    <connection>scm:git@github.com:foursquare/twitter-util-async.git</connection>
  </scm>
  <developers>
    <developer>
      <id>tdyas-4sq</id>
      <name>Tom Dyas</name>
    </developer>
  </developers>)

