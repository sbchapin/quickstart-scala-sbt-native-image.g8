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
      "--no-server" // Won't be running `graalvm-native-image:packageBin` often, so one less thing to break
    ),
    // Additional options used by `native-image` when building native image that vary based on the
    // operating system of the machine that is building the image
    graalVMNativeImageOptions ++= {
      // Note: Ideally, we should be checking if the current system has a working libc implementation here,
      // but os.name works surprisingly well in most cases.
      System.getProperty("os.name").toLowerCase match {
        case mac if mac.contains("mac") =>
          Nil
        case win if win.contains("win") =>
          Nil
        case _ =>
          // Forces statically-linked binary, compatible with libc (linux)
          Seq("--static")
      }
    },

    // Contains substitutions that are necessary for native image creation. These substitutions are required since monix
    // depends on JCTools which contains a lot of code that can't be compiled by graal native:
    libraryDependencies ++= Seq(
      "science.doing" % "jctools-graal-native_20_2_0" % "1.0.0",
      // Important: Keep the version of the "org.graalvm.nativeimage:svm" dependency synced with whatever version of
      // graal we are using!
      "org.graalvm.nativeimage" % "svm" % "20.2.0" % Provided
    ),

    // Concurrency:
    libraryDependencies += "io.monix" %% "monix" % "3.2.2",

    // Arg Parsing:
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",

    // Logging:
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3", // See README.md for why we don't use log4j2

    // Testing:
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % Test
  )
