ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "$organization$"
ThisBuild / maintainer := "$maintainer$"
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
ThisBuild / javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")


lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, GraalVMNativeImagePlugin)
  .settings(
    name := "$name$",
    version := "0.0.0",

    // Options used by `native-image` when building native image.
    // https://www.graalvm.org/docs/reference-manual/native-image/
    graalVMNativeImageOptions ++= Seq(
        "--initialize-at-build-time", // Auto-packs dependent libs at build-time
        "--no-fallback", // Bakes-in run-time reflection (alternately: --auto-fallback, --force-fallback)
        "--no-server", // Won't be running `graalvm-native-image:packageBin` often, so one less thing to break
        "--static" // Disable for OSX (non-docker) builds - Forces statically-linked binary, compatible with libc (linux)
    ),

    // JSON parser, with opinions:
    libraryDependencies += "io.monix" %% "monix" % "3.1.0",

    // Arg Parsing:
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",

    // Logging:
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3", // See README.md for why we don't use log4j2

    // Testing:
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
  )
