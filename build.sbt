name := "TotalDramaApi"

version := "1.0"

description := "GraphQL Server for Total Drama Api"

scalaVersion := "2.13.6"

scalacOptions ++= Seq("-deprecation", "-feature")

val AkkaVersion = "2.6.15"

libraryDependencies ++= Seq(
  // sangria
  "org.sangria-graphql" %% "sangria" % "2.1.3",
  "org.sangria-graphql" %% "sangria-spray-json" % "1.0.2",

  // akka
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,

  //slick
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.6.6",
  "com.h2database" % "h2" % "1.4.196",

  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)

Revolver.settings