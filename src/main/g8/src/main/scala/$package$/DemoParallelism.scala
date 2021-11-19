package $package$

import cats.effect.std.Random._
import cats.effect.{ExitCode, IO}
import cats.implicits.catsSyntaxTuple2Semigroupal
import com.typesafe.scalalogging.LazyLogging

object DemoParallelism extends LazyLogging {

  /**
   * Demonstrates effective parallelism using Monix and Monte-Carlo pi calculation.
   *
   * @param iterations  Iterations to calculate pi for
   * @param parallelism Concurrent processes
   */
  def apply(iterations: Long, parallelism: Int): IO[ExitCode] = {
    // Approximation of pi using iteration:
    val piApproximation = approximatePi(parallelism - 1, iterations)

    piApproximation.as(ExitCode.Success)
  }


  /**
   * Approximate pi via [[https://en.wikipedia.org/wiki/Approximations_of_%CF%80#Summing_a_circle's_area Monte Carlo method]].
   *
   * @parallelism Concurrent processes
   * @iterations Iterations to calculate pi for
   * @seed for randomness
   */
  def approximatePi(parallelism: Int, iterations: Long, seed: Int = 13): IO[Double] = for {
    _ <- IO { logger.info(f"Will approximate Pi with \$iterations%,d iterations of random bubble-filling and \$parallelism%,d parallel processes...") }

    // create random number generator
    random <- scalaUtilRandomSeedInt[IO](seed)

    // Kernel: projects a random point to detect if within bounds of a circle:
    detectCircularity = { (random.nextDouble, random.nextDouble).tupled }
      .map { case (x, y) => (x * 2 - 1, y * 2 - 1) }
      .map { case (x, y) => if (x * x + y * y <= 1) 1 else 0 }

    // The list of all tasks needed for execution:
    tasks = (for { _ <- 0L until iterations } yield detectCircularity).toList

    // execute tasks in parallel, then sum them together for the final result:
    aggregate <- IO.parSequenceN(parallelism)(tasks).map(_.sum)

    // Calculating pi from the aggregate:
    pi = 4.0 * aggregate / (iterations - 1)

    // log the result
    _ <- IO { logger.info(s"Calculated: \$pi") }
  } yield pi
}
