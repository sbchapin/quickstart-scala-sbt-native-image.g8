package $package$.args

import cats.implicits.catsSyntaxTuple2Semigroupal
import com.monovore.decline.{Command, Opts}
import $package$.args.Cmd.{Hi, Pi}

object Args {

  /**
   * Arguments for the "pi" command
   */
  private val piCommand: Command[Cmd] = Command(
    name = "pi",
    header = "Demonstrate parallelism by approximating pi."
  ) {
    val iterationsOpt = Opts.option[Long](long = "iterations", short = "i", help = "Number of iterations. The higher this is, the more accurate (and expensive) the calculation becomes.")
      .withDefault(Defaults.Pi.iterations)

    val parallelismOpt = Opts.option[Int](long = "parallelism", short = "p", help = "Maximum allowed number of parallel calculations.")
      .withDefault(Defaults.Pi.parallelism)

    (iterationsOpt, parallelismOpt).mapN { (iterations, parallelism) =>
      Pi(iterations, parallelism)
    }
  }

  /**
   * Arguments for the "hi" command
   */
  private val hiCommand: Command[Cmd] = Command(
    name = "hi",
    header = "Demonstrate levels of logging via slf4j -> logback by printing a greeting."
  ) {
    val nameOpt =
      Opts.option[String](long = "name", short = "n", help = "Name to generate a greeting for.").withDefault(Defaults.Hi.name)

    nameOpt.map {
      name => Hi(name)
    }
  }

  /**
   * Arguments for all commands combined into one!
   */
  val allCommands: Opts[Cmd] = Opts.subcommands(piCommand, hiCommand)
}