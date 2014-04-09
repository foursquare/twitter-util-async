organization := "com.foursquare"

name := "twitter-util-async"

version := "0.3"

scalaVersion := "2.10.4"

resolvers += "Foursquare Nexus" at "http://nexus.prod.foursquare.com/nexus/content/groups/public/"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  "com.twitter" %% "util-core" % "6.5.0",
  "junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.7" % "test"
)

publishTo <<= version { (v: String) =>
  val nexus = "http://nexus.prod.foursquare.com/nexus/content/repositories/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "thirdparty-snapshots/")
  else
    Some("releases"  at nexus + "thirdparty/")
}

credentials += Credentials(Path.userHome / ".ivy_credentials")

publishMavenStyle := true
 
publishArtifact in Test := false
 
publishArtifact in packageDoc := false

pomIncludeRepository := { x => false }
 
