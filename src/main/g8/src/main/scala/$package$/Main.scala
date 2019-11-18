package $package$

import $package$.args.Args
import $package$.args.Command.{Hi, Pi}

/**
  * Various demos of what a native-image can do, including...
  * - Typesafe arg parsing using Scopt
  * - Configurable logging interface using Scalalogging and underlying Logback mechanism
  * - Async & parallelization using Monix
  *
  * @author sbchapin
  * @since 11/11/19.
  */
object Main {

  /**
    * Entrypoint
    */
  def main(rawArgs: Array[String]): Unit = {
    Args.parse(rawArgs) {
      case Args(Hi(n))    => DemoLogging(name = n)
      case Args(Pi(i, p)) => DemoParallelism(iterations = i, parallelism = p)
    }
  }
}

