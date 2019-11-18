# $name$ #

An opinionated quickstart of a scala project using sbt as the build system.

By default, this will create a project containing a great base to make a production-level application.

The intention is to use `sbt new` on this library's exterior g8 template, then trim it down to keeping only what you need.


# Includes... #

- `Scala 2.12` for **coding**
- `sbt 1.+` for **building**
- `sbt-assembly` plugin for **running as a package** _(builds a fat jar with all dependencies via `sbt assembly`)_
- `scalatest` for **testing** _(default test runner via `sbt test`)_
- `sbt-scoverage` for **test coverage** _(statement-level coverage via `sbt coverage`)_
- `scalalogging -> slf4j -> log4j2` for **logging** _(scala code -> interface -> mechanism)_
- `scopt` for command-line **argument parsing**
- `pureconf -> typesafe config` for **configuration parsing** _(scala code -> mechanism)_
- `sbt-updates` for **keeping up-to-date** _(dependency updates via `sbt dependencyUpdates`)_
- `sbt-dependency-graph` for **understanding your package** _(dependency graph via `sbt dependencyBrowseGraph` and dependency stats via `sbt dependencyStats`)_

Check out the [Libs](#markdown-header-libs) below for more details on each.

# How to... #

## Become an Olympic gymnast before you crawl: ##

Remove previous build, check for updates for dependencies, view dependency graph in-browser, compile, test with coverage, generate coverage report, create a fat jar, and finally run from source.
```sh
sbt clean dependencyUpdates dependencyBrowseGraph compile coverage test coverageReport coverageOff assembly run
```


## Build: ##

Compile, test, and create a fat jar:
```sh
sbt assembly # jar created under build/libs/.
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

If you want to run **in isolation...**

1. Build the jar once: `sbt assembly`
3. Distribute to your heart's content (example shown): `scp /target/scala*/$name;format="norm"$-*.jar user@wherever:.`
2. Have your clients run the jar (only requires the jar, no code or repo): `java -jar $name;format="norm"$-*.jar`

A quick sanity check can be done via the following:
```sh
sbt assembly
scala target/scala*/$name;format="norm"$-*.jar
# or
java -jar target/scala*/$name;format="norm"$-*.jar
```


# Libs #

Below are the libraries used to provide a broad starting base for this project.



## [sbt-assembly](https://github.com/sbt/sbt-assembly) - bundled jars ##

`sbt-assembly` is an sbt plugin that allows us to build a large jar that is packed with all of its dependencies.  sbt-assembly is analogous to the Maven "shade" plugin.

###### Why do we use it? ######

- Build jars that can run on their own
- Build jars that hide conflicting dependencies

###### How do we use it? ######

- By running `sbt assembly` to generate a jar



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



## [scoverage](https://github.com/scoverage/sbt-scoverage) - coverage ##

`scoverage` is a sbt plugin that works in conjunction with scalatest that allows the generation of "coverage reports".  Coverage reports allow us to see what portions of our codebase have been hit by tests.  Most importantly, scoverage generates reports at a statement-level, so things like anonymous functions are checked for usage correctly.

###### Why do we use it? ######

- Coverage reports let you know where you should be focusing on testing.
- Coverage reports allow you to reason about technical debt and liability in your codebase.
- Statement-level coverage (rather than line-level) is important to scala because of the abundance of complex statements (blocks, anonymous functions, macros, etc...)

###### How do we use it? ######

- sbt plugin [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) allows us to run scoverage on top of the sbt test task. Due to scalatest being the default test runner, this will run scoverage with scalatest via `sbt coverage test` or `sbt coverageReport`
- HTML and XML Reports are generated via `sbt coverageReport`, and are dumped in `target/scala*/?coverage-report/`



## [scopt](https://github.com/scopt/scopt) - argument parsing ##

`scopt` is a framework that allows succinct and powerful command-line argument parsing.

###### Why do we use it? ######

- Parsing arguments is a chore that has been solved.
- Self-documenting with standardization.
- Makes it easy to simultaneously standardize, document, validate, and parse.
- Options like `Argot` are deprecated, where others like `Scallop` allow arguments that get confusing to document (passing `-42` where the argument is numerically flexible).

###### How do we use it? ######

- Refer to the entry point of the application to see it in action.

###### Quick Example: ######

`src/main/scala/com/example/Main.scala`:
```scala
import scopt.OptionParser

object Main {

  final case class OurArguments(maybeName: Option[String] = None)

  object ArgParser extends OptionParser[OurArguments]("project-name") {

    head("program description goes here")

    help("help").text("prints this usage text")

    opt[String]('n', "name")
      .text("Add a description for the name argument")
      .optional() // signifies this is not a required argument
      .validate( name => if (name == "sam") failure("name can't be 'sam'") else success )
      .action( (newName, conf) => conf.copy(maybeName = Some(newName)) )
  }

  def main(args: Array[String]) {
    val defaultArguments = OurArguments()

    val maybeParsedArgs: Option[OurArguments] = ArgParser.parse(args, defaultArguments)

    maybeParsedArgs match {
      case Some(parsedArgs) =>
        // Go nuts
      case None =>
        ArgParser.showUsageAsError()
        // Exit, log it, report it, whatever.
    }
  }
}
```


## [pureconfig](https://github.com/pureconfig/pureconfig) - configuration parsing ##

`pureconfig` is a framework that allows succinct and powerful configuration parsing & management.  It is designed to target scala, and uses many scala idioms (you'll find yourself slowly being led to write monoids for your configurations and using `pureconfig` to represent your configuration as a data structure.)

###### Why do we use it? ######

- Parsing configurations is a chore that has been solved.
- It allows us to keep out configuration DRY and commented (refer to `src/main/resources/application.conf`)
- Allows multiple configuration types (`.properties`, `.conf`, `.json`).
- It doesn't force us to make decisions about how we use or store configuration files or streams, it just does the heavy lifting (while providing sensible defaults like `application.conf`).
- Options like `JCommander` and plain `typesafe config` are too java-specific, while `sbt` configurations are too simple. `pureconfig` is just right.

###### How do we use it? ######

- `pureconfig` uses [typesafe config](https://github.com/typesafehub/config) to handle configuration files. `pureconfig` is really just a pretty scala interoperability layer.  Expect to be using and passing around instances of the `com.typesafe.config.Config` class.
- Refer to the entry point of the application to see it in action.

###### Quick Example: ######

`src/main/resources/application.conf`:
```conf
// Notice these two are not used in the configuration object
firstName = "Mr."
lastName = "Wooster"
// And that we can express concatenation here in the configuration
name = \${firstName} \${lastName}
age = 30
```

`src/main/scala/com/example/Main.scala`:
```scala
import com.typesafe.config.{Config, ConfigFactory}

object Main {

  final case class OurConfiguration(name: String, age: Int)

  def main(args: Array[String]) {

    // Use typesafe config to load config with sensible defaults
    val defaultConfig: Config =
      ConfigFactory           // Typesafe ConfigFactory for loading Configs
        .defaultApplication() // Defaults to this classloader's resources' `appliction.conf`
        .resolve()            // Not necessary unless your config has any imports, var substitutions, conditionals, etc...

    // Use pureconfig to convert to our scala object
    val configuration: OurConfiguration = pureconfig.loadConfigOrThrow[OurConfiguration](defaultConfig)

    // Go nuts
  }
}
```


## [scala-logging](https://github.com/lightbend/scala-logging) - universal logging ##

`scala-logging` is a logging adapter specific to Scala that uses the [slf4j logging interface](https://www.slf4j.org/manual.html).  In turn, that interface is hooked up to [Log4J as the logging mechanism](http://logging.apache.org/log4j/2.x/manual/layouts.html) (the thing that actually logs), which has been sensibly preconfigured.

###### Why do we use it? ######

- `scala-logging` gives you a limited subset of the functionality (only the stuff you need).
- `scala-logging` is coupled tightly to scala, and thus is very performant, making use of scala macros to intelligently not log when the logging level is not requested.  This allows us to leave trace & debug logs everywhere we need them without worrying about performance.
- Abstraction over `slf4j` allows the end-user to decide how they want to handle logs.
- `Log4J2` can be drop-in-replaced - all of the dependencies are run-time.  If you want to use logback (or whatever logging mechanism), simply bring along the dependency in the environment you wish to run in, and configure it.

###### How do we use it? ######

- Modify `src/main/resource/log4j2.xml` to your desire for local development. It currently has good defaults for moderate log usage in production (20 files rotating, 25 MB max each, INFO level)
- Inherit `LazyLogger` to a class and use the logger (or don't).
- Alternatively, instantiate a new Logger and pass it a name or a class.

###### Quick Example: ######

`src/main/scala/com/example/Main.scala`:
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

- Email: chap.s.b@gmail.com
- Github: https://github.com/sbchapin/
- Bitbucket: https://bitbucket.org/sbchapin/
- Discord: sbchapin#7435

## What's the future of this repo? ##

- Keep this baby up to date
- More robust examples of the existing libraries
- CI & CD Integrations
