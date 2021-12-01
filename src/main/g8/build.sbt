// Handy commands:
//   sbt test
//   sbt testQuick
//   sbt ~testQuick
//   sbt compile
//   sbt app/assembly
//   sbt app/native-image
//   sbt benchmarks/Jmh/run

ThisBuild / scalaVersion := "2.13.7"
ThisBuild / organization := "$organization$"
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
ThisBuild / javacOptions ++= Seq("-source", "11", "-target", "11", "-Xlint")

addCommandAlias("measure", "benchmarks/Jmh/run -rff benchmark-measurements.csv")
addCommandAlias("profile", "benchmarks/Jmh/run -prof jmh.extras.JFR -f 1")

lazy val root = (project in file("."))
  .aggregate(app, benchmarks)

lazy val app = (project in file("app"))
  .enablePlugins(NativeImagePlugin)
  .settings(
    name    := "$name$",
    version := "0.0.0",

    // Options used by `native-image` when building native image.
    // https://www.graalvm.org/docs/reference-manual/native-image/
    nativeImageOptions ++= Seq(
      "-H:+ReportExceptionStackTraces",
      "--no-fallback", // Bakes-in run-time reflection (alternately: --auto-fallback, --force-fallback)
      "--static", // Disable for OSX/Windows (non-docker) builds - Forces statically-linked binary, compatible with libc (linux)
      "--initialize-at-build-time" // Auto-packs all dependent libs at build-time - consider customizing this
    ),
    // Merging strategies used by `assembly` when assembling a fat jar.
    assembly / assemblyMergeStrategy := {
      case PathList(ps @ _*) if ps.last endsWith "module-info.class" => MergeStrategy.discard
      case x                                                         => (assembly / assemblyMergeStrategy).value(x)
    },

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
  .disablePlugins(AssemblyPlugin)
  .dependsOn(app)
  .settings(
    javaOptions ++= Seq(
      "-Dcats.effect.tracing.mode=none",                 // Prevent cats from tracing fibers
      "-Dcats.effect.tracing.exceptions.enhanced=false", // Prevent cats from adding exception handlers
      "-Droot-log-level=ERROR"                           // Set the log-level low so that JMH outputs are not polluted
    )
  )
