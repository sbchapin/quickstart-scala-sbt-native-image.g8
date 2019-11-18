package $package$.args

/** For scoping commands within args */
sealed trait Command

object Command {
  case object Empty extends Command
  case class Pi(iterations: Long, parallelism: Int) extends Command
  case class Hi(name: String) extends Command
}
