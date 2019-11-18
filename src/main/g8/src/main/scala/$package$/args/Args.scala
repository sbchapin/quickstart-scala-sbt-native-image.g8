package $package$.args

import $package$.args.Command.{Hi, Pi}
import scopt.OParser

/** Full representation of potential args for [[$package$.Main]] */
case class Args(cmd: Command)

object Args {

  def parse[T](args: Seq[String])(handleArgs: PartialFunction[Args, T]): Option[T] = {
    val parsed = OParser.parse[Args](parser, args, Args(Command.Empty))
    val breakEmptyArgs: PartialFunction[Args, T] = {
      case Args(Command.Empty) => throw new NotImplementedError(s"No command passed - must provide command.")
    }
    parsed.map(handleArgs orElse breakEmptyArgs)
  }

  /** Arg Parser */
  val parser: OParser[Unit, Args] = {
    val builder = OParser.builder[Args]
    import builder._
    OParser.sequence(
      programName("$name$"),
      head("$name$"),
      cmd("pi")
        .text("Demonstrate parallelism by approximating pi.")
        .action((_, args) => args.copy(cmd = Pi(Defaults.Pi.iterations, Defaults.Pi.parallelism)))
        .children(
          opt[Long]('i', "iterations")
            .action { case (x, args @ Args(cmd: Pi)) => args.copy(cmd = cmd.copy(iterations = x)) },
          opt[Int]('p', "parallelism")
            .action { case (x, args @ Args(cmd: Pi)) => args.copy(cmd = cmd.copy(parallelism = x)) }
        ),
      cmd("hi")
        .text("Demonstrate levels of logging via slf4j -> logback.")
        .action((_, args) => args.copy(cmd = Hi(Defaults.Hi.name)))
        .children(
          opt[String]('n', "name")
            .action { case (x, args @ Args(cmd: Hi)) => args.copy(cmd = cmd.copy(name = x)) }
        )
    )
  }
}
