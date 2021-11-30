package $package$.args

/** For scoping commands within args */
sealed trait Cmd

object Cmd {

  case class Pi(iterations: Long, parallelism: Int) extends Cmd

  case class Hi(name: String) extends Cmd

}
