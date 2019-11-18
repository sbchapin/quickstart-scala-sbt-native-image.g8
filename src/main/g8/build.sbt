ThisBuild / scalaVersion := "2.13.1"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1")
ThisBuild / organization := "$organization$"
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
ThisBuild / javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")


lazy val root = (project in file("."))
  .settings(
    name := "$name;format="norm"$",
    version := "0.0.0",

    // Option Parsing:
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.1",

    // Configuration:
    libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.1",

    // Logging:
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.29",                                 // slf4j as logging interface
    libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.12.1" % Runtime, // bridge: slf4j -> log4j
    libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.12.1" % Runtime,        // log4j as logging mechanism
    libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.12.1" % Runtime,       // log4j as logging mechanism

    // Testing:
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  )
