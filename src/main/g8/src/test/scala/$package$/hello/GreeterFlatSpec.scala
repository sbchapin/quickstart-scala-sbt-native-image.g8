package $package$.hello

import $package$.hello.fixtures.GreeterFixture
import org.scalatest.{FlatSpec, Matchers}

/**
  * This is intended to show "FlatSpec" Style.  There are many other spec styles available from scalatest.
  *
  * For most unit testers, FlatSpec (shoulda-style) and FunSpec (rspec-style) will be the best specs to write.
  *
  * To see some great Scalatest examples, check out this link:
  *   http://www.scalatest.org/at_a_glance/FlatSpec
  *
  * Scalatest has great documentation -- check it out here:
  *   http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FlatSpec
  *
  * @author sbchapin
  * @since 10/3/18.
  */
class GreeterFlatSpec extends FlatSpec with Matchers with GreeterFixture {

  "A Greeter" should "greet anyone but sbchapin with 'Hello' and their name" in {
    greeter.greet("mostAnyone") should include ("Hello, mostAnyone!")
  }

  it should "reprimand sbchapin" in {
    greeter.greet("sbchapin") should include ("overcomplicating")
  }

  it should "incorporate its name" in {
    val greeter = new Greeter("Jenkins")
    greeter.greet("sbchapin") should include ("Jenkins")
    greeter.greet("mostAnyone") should include ("Jenkins")
  }
}
