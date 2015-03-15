name := "HibernateEntityGenerator"

version := "1.0"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-feature")

libraryDependencies ++= Seq(
  "org.clapper" %% "grizzled-slf4j" % "1.0.2",
  "org.slf4j" % "slf4j-simple" % "1.6.4",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.play" %% "play-json" % "2.3.4")