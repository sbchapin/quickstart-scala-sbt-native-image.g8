package $package$

import $package$.spec.BaseSpec
import monix.execution.Scheduler

import scala.concurrent.duration._
import scala.concurrent.Await

class ParallelismSpec extends BaseSpec {

  describe("Parallelism Demo") {

    describe("with sequential pi calculation") {

      implicit val s: Scheduler = Scheduler.singleThread("TestScheduler")

      it("should approximate pi within a digit of precision with 100 iterations") {
        val futurePi = DemoParallelism.approximatePi(1, 100).runToFuture
        val pi = Await.result(futurePi, atMost = 5.seconds)
        assert(pi > 3 && pi < 4)
      }

      it("should approximate pi within two digits of precision with 1000 iterations") {
        val futurePi = DemoParallelism.approximatePi(1, 1000).runToFuture
        val pi = Await.result(futurePi, atMost = 5.seconds)
        assert(pi > 3.1 && pi < 3.2)
      }

      it("should approximate pi within three digits of precision with 10000 iterations") {
        val futurePi = DemoParallelism.approximatePi(1, 10000).runToFuture
        val pi = Await.result(futurePi, atMost = 5.seconds)
        assert(pi > 3.14 && pi < 3.16)
      }
    }
  }

}
