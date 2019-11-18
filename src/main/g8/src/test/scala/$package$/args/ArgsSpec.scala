package $package$.args

import $package$.args.Command.{Hi, Pi}
import $package$.spec.BaseSpec

import scala.language.reflectiveCalls

class ArgsSpec extends BaseSpec {

  describe("Args parser") {

    def parse(args: String*): Option[Args] = Args.parse(args) { case x => x }

    describe("with no command") {

      it("should not parse zero args") {
        assertThrows[NotImplementedError] {
          Args.parse(Seq.empty)(PartialFunction.empty).isEmpty
        }
      }

      it("should not parse unknown parameter '--not-present'") {
        assert(parse("--not-present").isEmpty)
      }
    }

    describe("with command 'not-present'") {

      it("should not parse") {
        assert(parse("not-present").isEmpty)
      }
    }

    describe("with command 'hi'") {

      it("should provide defaults") {
        assert(parse("hi").contains(Args(Hi(Defaults.Hi.name))))
      }

      it("should parse known parameter '--name' and '-n'") {
        val short = parse("hi", "-n", "valid-name")
        val long = parse("hi", "--name", "valid-name")
        assert(short == long && short.contains(Args(Hi("valid-name"))))
      }
    }

    describe("with command 'pi'") {

      it("should provide defaults") {
        assert(parse("pi").contains(Args(Pi(Defaults.Pi.iterations, Defaults.Pi.parallelism))))
      }

      it("should parse known parameter '--iterations' and '-i'") {
        val short = parse("pi", "-i", "42")
        val long = parse("pi", "--iterations", "42")
        assert(short == long && short.contains(Args(Pi(42, Defaults.Pi.parallelism))))
      }

      it("should parse known parameter '--parallelism' and '-p'") {
        val short = parse("pi", "-p", "13")
        val long = parse("pi", "--parallelism", "13")
        assert(short == long && short.contains(Args(Pi(Defaults.Pi.iterations, 13))))
      }
    }
  }
}
