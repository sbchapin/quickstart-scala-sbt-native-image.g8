package $package$

import cats.effect.ExitCode
import $package$.args.Args
import $package$.args.Command.{Hi, Pi}
import com.typesafe.scalalogging.LazyLogging
import monix.eval.{Task, TaskApp}
import monix.execution.Scheduler

/**
  * Various demos of what a native-image can do, including...
  * - Typesafe arg parsing using Scopt
  * - Configurable logging interface using Scalalogging and underlying Logback mechanism
  * - Async & parallelization using Monix
  *
  * @author sbchapin
  * @since 11/11/19.
  */
object Main extends TaskApp with LazyLogging {

  override def run(rawArgs: List[String]): Task[ExitCode] = {
    val appTask = Args.parse(rawArgs) {
      case Args(Hi(n)) =>
        DemoLogging(name = n)
      case Args(Pi(i, p)) =>
        DemoParallelism(iterations = i, parallelism = p)
    }.getOrElse(Task.unit)

    for {
      _ <- appTask.void
    } yield {
      ExitCode.Success
    }
  }
}
