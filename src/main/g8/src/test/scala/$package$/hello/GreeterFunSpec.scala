package $package$.hello

import $package$.hello.fixtures.GreeterFixture
import org.scalatest.{DiagrammedAssertions, FunSpec}

/**
  * This is intended to show "FunSpec" Style.  There are many other spec styles available from scalatest.
  *
  * For most unit testers, FlatSpec (shoulda-style) and FunSpec (rspec-style) will be the best specs to write.
  *
  * To see some great Scalatest examples using FunSpec, check out this link:
  *   http://www.scalatest.org/at_a_glance/FunSpec
  *
  * Scalatest has great documentation -- check it out here:
  *   http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FunSpec
  *
  * @author sbchapin
  * @since 10/3/18.
  */
class GreeterFunSpec extends FunSpec with DiagrammedAssertions with GreeterFixture {

  describe("A Greeter") {

    it("should greet anyone but sbchapin with 'Hello' and their name") {
      assert(greeter.greet("mostAnyone").contains("Hello, mostAnyone!"))
    }

    it("should reprimand sbchapin") {
      assert(greeter.greet("sbchapin").contains("overcomplicating"))
    }

    describe("with a supplied name") {

      it ("should incorporate its name") {
        val greeter = new Greeter("Jenkins")
        assert(greeter.greet("sbchapin").contains("Jenkins"))
        assert(greeter.greet("mostAnyone").contains("Jenkins"))
      }
    }
  }

}