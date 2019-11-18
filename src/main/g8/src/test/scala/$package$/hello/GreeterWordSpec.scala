package $package$.hello

import $package$.hello.fixtures.GreeterFixture
import org.scalatest.{DiagrammedAssertions, WordSpec}

/**
  * This is intended to show "WordSpec" Style.  There are many other spec styles available from scalatest.
  *
  * To see some great Scalatest examples, check out this link:
  *   http://www.scalatest.org/at_a_glance/WordSpec
  *
  * Scalatest has great documentation -- check it out here:
  *   http://doc.scalatest.org/3.0.0/index.html#org.scalatest.WordSpec
  *
  * @author sbchapin
  * @since 10/3/18.
  */
class GreeterWordSpec extends WordSpec with DiagrammedAssertions with GreeterFixture {

  "A Greeter" when {
    "greeting anyone but sbchapin" should {
      "respond with 'Hello' and their name" in {
        assert(greeter.greet("mostAnyone").contains("Hello, mostAnyone!"))
      }
    }
    "greeting sbchapin" should {
      "reprimand him" in {
        assert(greeter.greet("sbchapin").contains("overcomplicating"))
      }
    }
    "supplied a name" should {
      "incorporate the name" in {
        val greeter = new Greeter("Jenkins")
        assert(greeter.greet("sbchapin").contains("Jenkins"))
        assert(greeter.greet("mostAnyone").contains("Jenkins"))
      }
    }
  }
}