ThisBuild / scalaVersion := "2.13.7"
ThisBuild / organization := "$organization$"
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
ThisBuild / javacOptions ++= Seq("-source", "11", "-target", "11", "-Xlint")

lazy val root = (project in file("."))
  .aggregate(app, benchmarks)

lazy val app = (project in file("app"))
  .enablePlugins(JavaAppPackaging, GraalVMNativeImagePlugin)
  .settings(
    name    := "$name$",
    version := "0.0.0",

    // Options used by `native-image` when building native image.
    // https://www.graalvm.org/docs/reference-manual/native-image/
    graalVMNativeImageOptions ++= Seq(
      "--no-fallback", // Bakes-in run-time reflection (alternately: --auto-fallback, --force-fallback)
      "--no-server",   // Won't be running `graalvm-native-image:packageBin` often, so one less thing to break
      "--static", // Disable for OSX (non-docker) builds - Forces statically-linked binary, compatible with libc (linux)
      "--initialize-at-build-time=" + Seq( // Auto-packs dependent libs at build-time
        "$package$"
        // "some.class.with.long.initialization.Time"
      ).mkString(",")
    ),

    // Concurrency:
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.9",

    // Arg Parsing:
    libraryDependencies += "com.monovore" %% "decline-effect" % "2.2.0",

    // Logging:
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.3.0-alpha10", // See README.md for why we don't use log4j2

    // Testing:
    libraryDependencies += "org.scalatest" %% "scalatest"                     % "3.2.9" % Test,
    libraryDependencies += "org.typelevel" %% "cats-effect-testing-scalatest" % "1.3.0" % Test
  )

lazy val benchmarks = (project in file("benchmarks"))
  .enablePlugins(JmhPlugin)
  .dependsOn(app)
  .settings(
    javaOptions ++= Seq(
      "-Dcats.effect.tracing.mode=none",
      "-Dcats.effect.tracing.exceptions.enhanced=false",
      "-Droot-log-level=ERROR"
    )
  )
