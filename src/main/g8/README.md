# $name$ #

An opinionated quickstart of a scala project that builds to a native binary without need for the JRE.

By default, this will create a project containing only dependencies that compile on GraalVM, with examples of the stuff you will very likely already have need for. _(Arg parsing, Logging, parallelism...)_

The intention is to use `sbt new` on [this library's exterior g8 template](https://github.com/sbchapin/quickstart-scala-sbt-native-image.g8), then trim it down to keeping only what you need.

# Includes... #

- `Scala 2.13` for **coding**
- `sbt 1.+` for **building**
- `sbt-native-image` plugin for **building a linked binary** _(assembles ahead-of-time using GraalVM statically linking and building all dependencies in a docker image via `sbt nativeImage`)_
- `scalatest` for **testing** _(default test runner via `sbt test`)_
- `sbt-scoverage` for **test coverage** _(statement-level coverage via `sbt coverage`)_
- `scalalogging -> logback-classic` for **logging** _(`logback-classic` is used as a backend)_
- `decline` for command-line **argument parsing**
- `sbt-updates` for **keeping up-to-date** _(dependency updates via `sbt dependencyUpdates`)_
- `sbt-dependency-graph` for **understanding your package** _(dependency graph via `sbt dependencyBrowseGraph` and dependency stats via `sbt dependencyStats`)_

Check out the [Libs](#libs) below for more details on each.

# How to... #

## Prerequisites

There are a couple of things you're probably gonna want to have installed on your machine before you go any further.

- SBT: Used for building, testing and inspecting the project.
    - Install on [OSX](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Mac.html)
    - Install on [Windows](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html)
    - Install on [Linux](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)
- Docker: Wraps the entire build process to make your life easier. This means you don't need GraalVM or native-image in your environment.
    - Install via [Docker Desktop](https://hub.docker.com/?overlay=onboarding)
- OSX without Docker: If you intend to build this project to a binary on your machine rather than build it within Docker...
    - Install [GraalVM CE](https://www.graalvm.org/docs/getting-started/) - or better yet - [install jabba](https://github.com/shyiko/jabba#installation) Java Version Manager to [install GraalVM](https://github.com/shyiko/jabba#usage)
    - Install [native-image](https://www.graalvm.org/docs/reference-manual/native-image/) via `gu install native-image`

## Become an Olympic gymnast before you crawl: ##

Remove previous build, check for updates for dependencies, view dependency graph in-browser, compile, test with coverage, generate coverage report, create a fat jar, and finally run from source.
```sh
sbt clean dependencyUpdates dependencyBrowseGraph compile coverage test coverageReport coverageOff nativeImage run
```


## Build: ##

Test, compile ahead-of-time, and statically link an executable:
```sh
docker build -t $name$ .
docker run $name$ --help
docker run $name$ hi -n '$name$'
docker run $name$ pi --iterations 100000 --parallelism 10
# ...or if on linux (or other libc system)...
sbt app/nativeImage # binary created under app/target/native-image/$name$\
app/target/native-image/$name$ --help
# ...see above docker examples...
```

## Test: ##

Test the code using scalatest:
```sh
sbt test # In-depth readable reports will be generated in build/scala*/tests
sbt clean coverage test reportcoverage  # In-depth readable coverage reports will be generated in target/scala*/scoverage-report
```

## Clean: ##

Completely clean this project's build:
```sh
sbt clean
```

## Run: ##

If you want to run **while the repo is present...**
```sh
sbt run
```

---


# Libs #

Below are the libraries used to provide a broad starting base for this project.



## [sbt-native-image](https://github.com/scalameta/sbt-native-image) - JRE-less executable ##

`sbt-native-image` is an sbt plugin that allows sbt to build assembled JRE-less executables using GraalVM.  This plugin handles downloading and installation of the related Graal JDK and native-image tool which are required to make the executable binary.  However, these are very particular about their targets - That's where we pick up with docker in this repo, ultimately ending up with a portable, fast, minified docker image with a single binary in it:

1. **sbt + sbt-native-image + GraalVM + native-image** get us from **scala-to-bytecode-to-llvm-to-libc**.
2. **docker** gets us from **libc-to-kernel**, providing a consistent "target" for GraalVM & native-image.

However, it isn't all roses.  **The native-image & AoT aspect of any project building with Graal is very brittle**, as all classes exposed exclusively via reflection at run-time [must be explicitly provided to Graal](https://www.graalvm.org/reference-manual/native-image/Reflection/), whether determined automatically or manually.  

Do your reading before you decide to push the outputs of native-image to production, and consider the cost of maintaining reflection mappings.

###### Why do we use it? ######

- GraalVM integration allows us to build small fast-starting binaries out of Scala that would have otherwise been bytecode.  Throwing the JRE baby out with the bathwater offers several benefits:
  - No JVM cold start
  - AoT target-based compilation means no JIT jitters or hotspot hotflashes
  - Tiny docker images (tens of MB) with the smallest possible base image *(even from [scratch](https://hub.docker.com/_/scratch), if you want)*

###### How do we use it? ######

- By running `docker build -t $name$ . && docker run $name$` to generate a very small (<10 MB) image with very little (milliseconds) of start-up time.
- Or by running `sbt app/nativeImage` to generate an executable if in a compatible environment (most linux distros), then executing or distrubuting the resultant binary in `app/app/targetive-image/$name$`



## [scalatest](https://github.com/scalatest/scalatest) - test framework ##

`scalatest` is a testing framework (like JUnit/RSpec/Chai/Mocha).

###### Why do we use it? ######

- JUnit just doesn't cut it for scala.
    - JUnit assertions & matchers can be simplified.
    - JUnit supports only test specifications.
- Scalatest [gives many options](http://www.scalatest.org/user_guide/selecting_a_style) when it comes to testing style.
    - Specification style (like Rspec).
    - Behavior Specifications (like cucumber).
    - Basic build-your-own-spec.
    - Even JUnit style.

- The matchers and assertions are amazing (try to `assert(something)`, it gives great results.)

###### How do we use it? ######

- All tests run every time the program is built, providing that run-time guarantee that everything is working.
- SBT allows us to run the scalatest framework as the default testrunner via `sbt test`.
- SBT can auto-test when project changes are detected via `sbt ~testQuick`.
- All tests in `src/test/...` are written using scalatest.



## [jmh](https://github.com/openjdk/jmh) - performance (micro)benchmarking ##

`jmh` is a microbenching runner and harness (like a test framework, but for measuring and reporting rather than qualifying & asserting). 

###### Why do we use it? ######

- To gauge performance bottlenecks in isolation under a variety of circumstances
- To tightly integrate with parts of your program and performance test them thoroughly
- To win arguments with friends

###### How do we use it? ######

- SBT and [the sbt-jmh plugin](https://github.com/sbt/sbt-jmh) allow us to run via `sbt benchmarks/Jmh/run` - In addition, there are two aliases of this command:
  - `sbt measure` to run JMH and record results to `benchmark-measurements.csv`
  - `sbt profile` to run JMH along with the included JFR JVM profiler *([which could be parameterized for whatever](https://blog.couchbase.com/all-the-small-things-jvm-profiling-lessons-from-the-trenches/))*
- The `./benchmarks` subproject contains JMH benchmarks that test components of the project, add new benchmarks there
  - Any `./app` code is available to test directly
  - If you are measuring performance on the larger scale (milliseconds and beyond), use [example benchmarks](https://github.com/sbt/sbt-jmh/tree/main/plugin/src/sbt-test/sbt-jmh/run/src/main/scala/org/openjdk/jmh/samples) as good starting points for JMH tests
  - If you are measuring performance on the small-scale (nanoseconds), [read this first](https://shipilev.net/blog/2014/nanotrusting-nanotime/) to ensure you are doing so correctly and reasoning about your results appropriately



## [scoverage](https://github.com/scoverage/sbt-scoverage) - coverage ##

`scoverage` is a sbt plugin that works in conjunction with scalatest that allows the generation of "coverage reports".  Coverage reports allow us to see what portions of our codebase have been hit by tests.  Most importantly, scoverage generates reports at a statement-level, so things like anonymous functions are checked for usage correctly.

###### Why do we use it? ######

- Coverage reports let you know where you should be focusing on testing.
- Coverage reports allow you to reason about technical debt and liability in your codebase.
- Statement-level coverage (rather than line-level) is important to scala because of the abundance of complex statements (blocks, anonymous functions, macros, etc...)

###### How do we use it? ######

- sbt plugin [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) allows us to run scoverage on top of the sbt test task. Due to scalatest being the default test runner, this will run scoverage with scalatest via `sbt coverage test` or `sbt coverageReport`
- HTML and XML Reports are generated via `sbt coverageReport`, and are dumped in `target/scala*/?coverage-report/`



## [scalafmt](https://scalameta.org/scalafmt/) - *(automatic)* code formatting ##

`scalafmt` is a scala code formatter. It integrates directly into the interpreter and can use the full power of scala's symbolic interpretation, enabling [all sorts of crazy configurations](https://scalameta.org/scalafmt/docs/configuration.html).  What's important here is consistency, so we enforce it with scalafmt.

###### Why do we use it? ######

- Automatic formatting means you just slap that save button and let the interpreter and scalafmt do the rest
- Consistent formatting ensured, ruled by the iron fist of your CI system
- Less arguing and nitpicking - your S.O. won't dump you because you use spaces rather than tabs

###### How do we use it? ######

- For normal development, [it's integrated with your editor](https://scalameta.org/scalafmt/docs/installation.html)
- sbt plugin [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) allows us to run scalafmt to check format for tasks like CI, or to actually perform the formatting via `sbt scalafmtCheckAll` and `sbt scalafmtAll` respectively
- Experiment with the highly-configurable and flexible `./.scalafmt.conf` to see what feels right for you



## [decline](https://github.com/bkirwi/decline) - argument parsing ##

`decline` is a framework that allows succinct and powerful command-line argument parsing.

###### Why do we use it? ######

- Integrates really well with cats-effect via [CommandIOApp](https://ben.kirw.in/decline/effect.html#interpreting-our-command-line-interface)
- Parsing arguments is a chore that has been solved.
- Self-documenting with standardization.
- Makes it easy to simultaneously standardize, document, validate, and parse.
- Options like `Argot` are deprecated, where others like `Scallop` allow arguments that get confusing to document (passing `-42` where the argument is numerically flexible).
- It plays well with Graal

###### How do we use it? ######

- Refer to the entry point of the application to see it in action.

###### Quick Example: ######

`app/src/main/scala/com/example/Main.scala`:
```scala
import cats.implicits._
import com.monovore.decline._

object HelloWorld extends CommandApp(
  name = "hello-world",
  header = "Says hello!",
  main = {
    val userOpt =
      Opts.option[String]("target", help = "Person to greet.")
              .withDefault("world")

    val quietOpt = Opts.flag("quiet", help = "Whether to be quiet.").orFalse

    (userOpt, quietOpt).mapN { (user, quiet) =>

      if (quiet) println("...")
      else println(s"Hello \$user!")
    }
  }
)
```

## [scala-logging](https://github.com/lightbend/scala-logging) - universal logging ##

`scala-logging` is a logging adapter specific to Scala that uses the [slf4j logging interface](https://www.slf4j.org/manual.html).  In turn, that interface is hooked up to [logback as the logging mechanism](http://logback.qos.ch/) (the thing that actually logs), which has been sensibly preconfigured.

###### Why do we use it? ######

- `scala-logging` gives you a limited subset of the functionality (only the stuff you need).
- `scala-logging` is coupled tightly to scala, and thus is very performant, making use of scala macros to intelligently not log when the logging level is not requested.  This allows us to leave trace & debug logs everywhere we need them without worrying about performance.
- Abstraction over `slf4j` allows the end-user to decide how they want to handle logs.
- `logback-classic` can be drop-in-replaced - all of the dependencies are run-time.
- If you want to use `Log4J2` (or another logging backend that relies heavily upon reflection), be warned that the SubstrateVM's "out of the box" configuration won't be enough and [you'll need to do some configuration first](https://github.com/oracle/graal/issues/808#issuecomment-470769285). If you don't absolutely need log4j2, you could simply wait for SubstrateVM's [automatic detection of reflection](https://github.com/oracle/graal/blob/master/substratevm/REFLECTION.md#automatic-detection) to be expanded upon so you can keep your project a little more lean in the meantime.
- It plays well with Graal


###### How do we use it? ######

- Modify `app/src/main/resources/logback.xml` to your desire. It currently has a basic, yet colorful, logger that simply prints INFO level and above to the console.
- Provide system properties to override values in the configuration like `root-log-level`, or add your own.
- Inherit `LazyLogger` to a class and use the logger (or don't).
- Alternatively, instantiate a new Logger and pass it a name or a class.

###### Quick Example: ######

`app/src/main/scala/com/example/Main.scala`:
```scala
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {
  def main(args: Array[String]) {
    logger.info("Hello, World!")
  }
}
```


## [sbt-updates](https://github.com/rtimush/sbt-updates) - dependency updates ##

`sbt-updates` is a sbt plugin that will generate a report of stale dependencies for your project.

###### Why do we use it? ######

- Manual updates are better than pluses in your dependencies. (Automatic updates may seem like a good idea in the short term, but they create problems of reproducibility in your build system.)
- Allows for build-blockers for updates, blocking your CI and forcing developers to pay off debt earlier.
- Allows for pre-releases for updates, allowing you to stay up-to-date with highly coupled dependencies.

###### How do we use it? ######

- `sbt dependencyUpdates` gives you a list of outdated dependencies.



## [sbt-dependency-graph](https://github.com/jrudolph/sbt-dependency-graph) - dependency grokking ##

`sbt-dependency-graph` is a sbt plugin that will generate a graph, tree, or statistics about your dependencies (and their dependencies).

###### Why do we use it? ######

- Optimizing your dependency graph can be hard, the stats from this plugin provide a great starting point.
- Understanding where your dependencies are coming from is critical to excluding them or managing them.
- Creating an assembly requires understanding of your dependencies, this plugin helps you iterate on your assembly in a meaningful way.

###### How do we use it? ######

- `sbt dependencyBrowseGraph` will open a rendered graph of dependencies.
- `sbt dependencyTree` will print to terminal a text-based tree of dependencies.
- `sbt dependencyStats` will give a breakdown of largest and most connected dependencies.



## Who do I talk to? ##

This project was generated from [this quickstart](https://github.com/sbchapin/quickstart-scala-sbt-native-image.g8)