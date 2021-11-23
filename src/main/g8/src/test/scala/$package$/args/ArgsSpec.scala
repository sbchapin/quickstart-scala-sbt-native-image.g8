package $package$.args

import com.monovore.decline.{Command, Help}
import $package$.args.{Cmd, Args, Defaults}
import $package$.spec.BaseSpec

class ArgsSpec extends BaseSpec {

  describe("Args parser") {

    def parse(args: String*): Either[Help, Cmd] = Command("", "")(Args.allCommands).parse(args)

    describe("with no command") {

      it("should print help if provided zero args") {
        assert(parse().isLeft)
      }

      it("should print help if provided unknown parameter '--not-present'") {
        assert(parse("--not-present").isLeft)
      }
    }

    describe("with command 'not-present'") {

      it("should not parse") {
        assert(parse("not-present").isLeft)
      }
    }

    describe("with command 'hi'") {

      it("should provide defaults") {
        assert(parse("hi") === Right(Cmd.Hi(Defaults.Hi.name)))
      }

      it("should parse known parameter '--name' and '-n'") {
        val short = parse("hi", "-n", "valid-name")
        val long = parse("hi", "--name", "valid-name")
        assert(short == long && short === Right(Cmd.Hi("valid-name")))
      }
    }

    describe("with command 'pi'") {

      it("should provide defaults") {
        assert(parse("pi").contains(Cmd.Pi(Defaults.Pi.iterations, Defaults.Pi.parallelism)))
      }

      it("should parse known parameter '--iterations' and '-i'") {
        val short = parse("pi", "-i", "42")
        val long = parse("pi", "--iterations", "42")
        assert(short == long && short.contains(Cmd.Pi(42, Defaults.Pi.parallelism)))
      }

      it("should parse known parameter '--parallelism' and '-p'") {
        val short = parse("pi", "-p", "13")
        val long = parse("pi", "--parallelism", "13")
        assert(short == long && short.contains(Cmd.Pi(Defaults.Pi.iterations, 13)))
      }
    }
  }
}
