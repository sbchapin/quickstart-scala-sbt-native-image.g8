package $package$

import com.typesafe.scalalogging.LazyLogging
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

object DemoParallelism extends LazyLogging {

  /**
    * Demonstrates effective parallelism using Monix and Monte-Carlo pi calculation.
    *
    * @param iterations  Iterations to calculate pi for
    * @param parallelism Concurrent processes
    */
  def apply(iterations: Long, parallelism: Int): Unit = {
    logger.info(f"Will approximate Pi with $iterations%,d iterations of random bubble-filling and $parallelism%,d parallel processes...")

    // Scheduler for parallelism:
    implicit val s: Scheduler = Scheduler.forkJoin(parallelism, maxThreads = 1000)

    // Approximation of pi using iteration:
    val pi = approximatePi(iterations).runToFuture

    logger.info(s"Pi is roughly ${Await.result(pi, atMost = 30.seconds)}")
  }

  /**
    * Approximate pi via [[https://en.wikipedia.org/wiki/Approximations_of_%CF%80#Summing_a_circle's_area Monte Carlo method]].
    */
  def approximatePi(iterations: Long, seed: Int = 13): Task[Double] = {
    val random = new Random(seed)
    // Kernel: projects a random point to detect if within bounds of a circle:
    val detectCircularity: Task[Long] = Task { (random.nextDouble(), random.nextDouble()) }
      .map { case (x, y) => (x * 2 - 1, y * 2 - 1) }
      .map { case (x, y) => if (x * x + y * y <= 1) 1 else 0 }

    // The list of all tasks needed for execution:
    val tasks = for { _ <- 0L until iterations } yield detectCircularity
    // Gathering tasks, then summing the final result:
    val aggregate = Task.gather(tasks).map(_.sum)
    // Calculating pi from the aggregate:
    aggregate.map { 4.0 * _ / (iterations - 1) }
  }
}
