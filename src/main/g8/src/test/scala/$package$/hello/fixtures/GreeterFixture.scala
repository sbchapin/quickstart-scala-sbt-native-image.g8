package $package$.hello.fixtures

import $package$.hello.Greeter
import org.scalatest.{TestSuite, TestSuiteMixin}

trait GreeterFixture extends TestSuiteMixin { this: TestSuite =>

  val greeter = new Greeter

// To provide surrounding setup for the fixture:
//
//  abstract override def withFixture(test: NoArgTest): Outcome = {
//    try super.withFixture(test) // To be stackable, must call super.withFixture
//    finally greeter.close
//  }
}
