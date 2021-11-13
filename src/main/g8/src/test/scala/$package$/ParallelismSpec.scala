package $package$

import cats.effect.testing.scalatest.AsyncIOSpec
import $package$.DemoParallelism
import $package$.spec.BaseSpec

import scala.concurrent.duration._
import scala.concurrent.Await

class ParallelismSpec extends BaseSpec {

  describe("Parallelism Demo") {

    describe("with pi calculation") {

      lazy val parallelism = Runtime.getRuntime.availableProcessors()

      it ("should approximate pi within a digit of precision with 100 iterations") {
        val pio = DemoParallelism.approximatePi(parallelism, 100)
        val timedPi = pio.timeout(5.seconds)
        timedPi.asserting(pi => assert(pi > 3 && pi < 4))
      }

      it ("should approximate pi within two digits of precision with 1000 iterations") {
        val pio = DemoParallelism.approximatePi(parallelism, 1000)
        val timedPi = pio.timeout(5.seconds)
        timedPi.asserting(pi => assert(pi > 3.1 && pi < 3.2))
      }

      it ("should approximate pi within three digits of precision with 10000 iterations") {
        val pio = DemoParallelism.approximatePi(parallelism, 10000)
        val timedPi = pio.timeout(5.seconds)
        timedPi.asserting(pi => assert(pi > 3.14 && pi < 3.16))
      }
    }
  }
}
