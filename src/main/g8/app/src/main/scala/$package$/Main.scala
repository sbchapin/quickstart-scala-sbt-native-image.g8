package $package$

import cats.effect.{ExitCode, IO}
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp
import $package$.args.{Args, Cmd}

/** Various demos of what a native-image can do, including...
  *   - Typesafe arg parsing using Decline
  *   - Configurable logging interface using Scalalogging and underlying Logback mechanism
  *   - Async & parallelization using Monix
  *
  * @author
  *   sbchapin
  * @since 11/11/19.
  */
object Main
    extends CommandIOApp(
      name = "$name$",
      header = "Does some things with Graaaaaal!"
    ) {

  /** Entrypoint
    */
  override def main: Opts[IO[ExitCode]] = {
    // parse the user's arguments...
    Args.allCommands.map {

      // when provided the "pi" command, run the pi calculation demo
      case Cmd.Pi(iterations, parallelism) =>
        DemoParallelism(iterations, parallelism)

      // when provided the "hi" command, run the logging demo
      case Cmd.Hi(name) =>
        DemoLogging(name)
    }
  }
}
