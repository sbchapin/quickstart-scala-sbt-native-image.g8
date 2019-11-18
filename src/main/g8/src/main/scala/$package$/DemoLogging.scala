package $package$

import com.typesafe.scalalogging.LazyLogging

import scala.math.random

object DemoLogging extends LazyLogging {

  /**
    * Demonstrates log levels and usage of scalalogging -> slf4j -> logbac
    */
  def apply(name: String): Unit = {
    logger.info(s"Hello, $name!")

    logger.error("Bad Stuff")
    logger.warn("Not-so-good stuff")
    logger.info("Biscuits and Tea")
    logger.debug("Details")
    logger.trace("Juicy Details")
  }

}
