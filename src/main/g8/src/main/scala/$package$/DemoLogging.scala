package $package$

import cats.effect.{ExitCode, IO}
import com.typesafe.scalalogging.LazyLogging

object DemoLogging extends LazyLogging {

  /**
   * Demonstrates log levels and usage of scalalogging -> slf4j -> logbac
   */
  def apply(name: String): IO[ExitCode] = {

    val logStuff = IO {
      logger.info(s"Hello, \$name!")

      logger.error("Bad Stuff")
      logger.warn("Not-so-good stuff")
      logger.info("Biscuits and Tea")
      logger.debug("Details")
      logger.trace("Juicy Details")
    }

    logStuff.as(ExitCode.Success)
  }

}
