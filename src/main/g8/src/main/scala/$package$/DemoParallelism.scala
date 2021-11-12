package $package$

import cats.effect.Resource
import com.typesafe.scalalogging.LazyLogging
import monix.eval.Task
import monix.execution.Scheduler
import monix.reactive.Observable

import scala.concurrent.duration._
import scala.util.Random

object DemoParallelism extends LazyLogging {

  /**
   * Demonstrates effective parallelism using Monix and Monte-Carlo pi calculation.
   *
   * @param iterations  Iterations to calculate pi for
   * @param parallelism Concurrent processes
   */
  def apply(iterations: Long, parallelism: Int): Task[Double] = for {
    // log that the work is starting
    _ <- Task {
      logger.info(f"Will approximate Pi with \$iterations%,d iterations of random bubble-filling and up to \$parallelism%,d parallel processes...")
    }

    // create scheduler resource based on desired parallelism (it will be closed once we have finished our calculations)
    schedulerResource = Resource.make(acquire = Task {
      Scheduler.forkJoin(parallelism = parallelism, maxThreads = 1000)
    })(release = s => Task { s.shutdown() })

    // then use the scheduler to...
    calculate <- schedulerResource.use(scheduler => {
      // calculate an approximation of pi
      approximatePi(parallelism, iterations)
        // measure how long it took to perform the calculation
        .timed
        // set a timeout of 30 seconds
        .timeout(30.seconds)
        // ensure the process runs on the provided scheduler
        .executeOn(scheduler)
    })

    // log the results
    (timeTook, pi) = calculate
    _ <- Task {
      logger.info(s"Pi is roughly \${pi}. (Took \${timeTook.toMillis}ms)")
    }
  } yield {
    pi
  }

  /**
    * Approximate pi via [[https://en.wikipedia.org/wiki/Approximations_of_%CF%80#Summing_a_circle's_area Monte Carlo method]].
    */
  def approximatePi(parallelism: Int, iterations: Long, seed: Int = 13): Task[Double] = {
    val random = new Random(seed)

    // Kernel: projects a random point to detect if within bounds of a circle:
    val detectCircularity: Task[Long] = Task { (random.nextDouble(), random.nextDouble()) }
      .map { case (x, y) => (x * 2 - 1, y * 2 - 1) }
      .map { case (x, y) => if (x * x + y * y <= 1) 1 else 0 }

    // create a stream of "observable events" matching the desired iterations
    Observable.range(0, iterations)
      // for each iteration, create a projection and check it
      .mapParallelUnordered(parallelism)(_ => detectCircularity)
      // sum the results of each detection
      .sumL
      // finally, calculate pi from the aggregate sum
      .map(sum => {
        4.0 * sum / (iterations - 1)
      })
  }
}
