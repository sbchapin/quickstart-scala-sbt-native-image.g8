package $package$.hello

import $package$.hello.fixtures.GreeterFixture
import org.scalatest.DiagrammedAssertions
import org.scalatest.refspec.RefSpec

/**
  * This is intended to show "RefSpec" Style.  There are many other spec styles available from scalatest.
  *
  * Keep in mind this style is mainly about optimization - it is significantly faster to run and compile (though that is not a great reason to pick it).
  *
  * To see some great Scalatest examples, check out this link:
  *   http://www.scalatest.org/at_a_glance/RefSpec
  *
  * Scalatest has great documentation -- check it out here:
  *   http://doc.scalatest.org/3.0.0/index.html#org.scalatest.RefSpec
  *
  * @author sbchapin
  * @since 10/3/18.
  */
class GreeterRefSpec extends RefSpec with DiagrammedAssertions with GreeterFixture {

  object `A Greeter when` {
    object `greeting anyone but sbchapin should` {
      object `respond with 'Hello' and their name` {
        assert(greeter.greet("mostAnyone").contains("Hello, mostAnyone!"))
      }
    }
    object `greeting sbchapin should` {
      object `reprimand him` {
        assert(greeter.greet("sbchapin").contains("overcomplicating"))
      }
    }
    object `supplied a name should` {
      object `incorporate the name` {
        val greeter = new Greeter("Jenkins")
        assert(greeter.greet("sbchapin").contains("Jenkins"))
        assert(greeter.greet("mostAnyone").contains("Jenkins"))
      }
    }
  }
}