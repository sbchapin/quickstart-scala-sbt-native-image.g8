package benchmarks

import org.openjdk.jmh.annotations._
import $package$.DemoParallelism
import java.util.concurrent.TimeUnit
import cats.effect.unsafe.implicits.global

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
class DemoParallelismBenchmarks {

  @Param(
    Array(
      "10000", // ten thousand
      "100000" // hundred thousand
    )
  )
  var monteCarloIterations: Long = _

  @Param(Array("1", "3", "10"))
  var monteCarloParallelism: Int = _

  @Benchmark
  def measure: Double = {
    DemoParallelism.approximatePi(monteCarloParallelism, monteCarloIterations).unsafeRunSync()
  }

}
