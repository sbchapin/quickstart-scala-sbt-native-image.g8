package $package$

import cats.effect.{ExitCode, IO}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.DurationInt
import scala.util.Random

object DemoParallelism extends LazyLogging {

  /**
   * Demonstrates effective parallelism using Monix and Monte-Carlo pi calculation.
   *
   * @param iterations  Iterations to calculate pi for
   * @param parallelism Concurrent processes
   */
  def apply(iterations: Long, parallelism: Int): IO[ExitCode] = {
    logger.info(f"Will approximate Pi with \$iterations%,d iterations of random bubble-filling and \$parallelism%,d parallel processes...")

    // Approximation of pi using iteration:
    val piApproximation = approximatePi(parallelism, iterations).map(calculatedResult => {
      logger.info(s"Pi is roughly \$calculatedResult")
    }).timeout(30.seconds)

    piApproximation.as(ExitCode.Success)
  }

  /**
   * Approximate pi via [[https://en.wikipedia.org/wiki/Approximations_of_%CF%80#Summing_a_circle's_area Monte Carlo method]].
   */
  def approximatePi(parallelism: Int, iterations: Long, seed: Int = 13): IO[Double] = {
    val random = new Random(seed)

    // Kernel: projects a random point to detect if within bounds of a circle:
    val detectCircularity: IO[Long] = IO { (random.nextDouble(), random.nextDouble()) }
      .map { case (x, y) => (x * 2 - 1, y * 2 - 1) }
      .map { case (x, y) => if (x * x + y * y <= 1) 1 else 0 }

    // The list of all tasks needed for execution:
    val tasks = for { _ <- 0L until iterations } yield detectCircularity
    // Gathering tasks, then summing the final result:
    val aggregate = IO.parSequenceN(parallelism)(tasks.toList).map(_.sum)
    // Calculating pi from the aggregate:
    aggregate.map { 4.0 * _ / (iterations - 1) }
  }
}
