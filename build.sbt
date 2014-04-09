organization := "com.foursquare"

name := "twitter-util-async"

version := "0.4-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += "Foursquare Nexus" at "http://nexus.prod.foursquare.com/nexus/content/groups/public/"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  "com.twitter" %% "util-core" % "6.5.0",
  "junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.7" % "test"
)

//publishTo <<= version { (v: String) =>
//  val nexus = "http://nexus.prod.foursquare.com/nexus/content/repositories/"
//  if (v.trim.endsWith("SNAPSHOT"))
//    Some("snapshots" at nexus + "thirdparty-snapshots/")
//  else
//    Some("releases"  at nexus + "thirdparty/")
//}
//
//credentials += Credentials(Path.userHome / ".ivy_credentials")
//
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true
 
publishArtifact in Test := false
 
publishArtifact in packageDoc := false

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
 
