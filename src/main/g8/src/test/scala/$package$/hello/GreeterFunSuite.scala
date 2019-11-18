package $package$.hello

import $package$.hello.fixtures.GreeterFixture
import org.scalatest.{DiagrammedAssertions, FunSuite}

/**
  * This is intended to show "FunSuite" Style.  There are many other spec styles available from scalatest.
  *
  * To see some great Scalatest examples, check out this link:
  *   http://www.scalatest.org/at_a_glance/FunSuite
  *
  * Scalatest has great documentation -- check it out here:
  *   http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FunSuite
  *
  * @author sbchapin
  * @since 10/3/18.
  */
class GreeterFunSuite extends FunSuite with DiagrammedAssertions with GreeterFixture {

  test("A Greeter should greet anyone but sbchapin with 'Hello' and their name") {
    assert(greeter.greet("mostAnyone").contains("Hello, mostAnyone!"))
  }

  test("A Greeter should reprimand sbchapin") {
    assert(greeter.greet("sbchapin").contains("overcomplicating"))
  }

  test("A Greeter with a supplied name should incorporate its name") {
    val greeter = new Greeter("Jenkins")
    assert(greeter.greet("sbchapin").contains("Jenkins"))
    assert(greeter.greet("mostAnyone").contains("Jenkins"))
  }
}