package $package$

import $package$.hello.Greeter
import $package$.args._
import $package$.config._
import com.typesafe.scalalogging.LazyLogging


/**
  * Hello, world
  *
  * with examples of:
  * - Logging
  * - Argument parsing
  * - Configuration parsing
  *
  * Text headers generated via http://patorjk.com/software/taag/#p=display&f=Stampate
  *
  * @author sbchapin
  * @since 10/3/17.
  */
object Main extends App with LazyLogging {



  //  `.---     .             .-,--.           .  ,.
  //   |__  ,-. |- ,-. . .     '|__/ ,-. . ,-. |- `'
  //  ,|    | | |  |   | |     .|    | | | | | |  ,.
  //  `^--- ' ' `' '   `-|     `'    `-' ' ' ' `' `'
  //                    /|
  //                   `-'


  // Demonstrating configuration parsing
  val configuration: Configuration = parseConfiguration()

  // Demonstrating usage of parsed configurations
  {
    val greeter = new Greeter()
    val greeting = greeter.greet(configuration.name)
    for (i <- 1 to configuration.iterations) {
      logger.info(s"Greeting \$i \$greeting")
    }
  }


  // Demonstrating argument parsing (try passing the --help flag and the -n or --name flags with values.)
  // NOTE: You should validate and respond to invalid user input as soon as possible - this is not a realistic example.
  //       In reality, arg parsing will frequently be one of the first things to happen, and arguments and configuration will not be so closely related.
  val arguments: Arguments = parseArguments(args, defaultName = configuration.name)

  // Demonstrating different parsed arguments and log levels
  {
    val greeter = new Greeter(arguments.name)
    logger.error(greeter.greet("Bad Stuff"))
    logger.warn(greeter.greet("Not-so-good stuff"))
    logger.info(greeter.greet("Biscuits and Tea"))
    logger.debug(greeter.greet("Details"))
    logger.trace(greeter.greet("Juicy Details"))
  }
}
