package $package$.hello

/**
  * A Greeter to say hello
  *
  * @author sbchapin
  * @since 10/3/17.
  *
  * @param greeterName The name of the greeter
  */
class Greeter(greeterName: String = "Jeeves") {
  def greet(name: String): String = name match {
    case "sbchapin" => s"\$greeterName reprimands \$name for overcomplicating a simple hello world."
    case _          => s"""\$greeterName greets you with a hearty "Hello, \$name!""""
  }
}
