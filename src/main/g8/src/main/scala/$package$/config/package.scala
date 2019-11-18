package $package$

import pureconfig.ConfigSource

package object config {


  //  ,---.         ,_                   .              .              .                   .-,--.                       ,.
  //  |  -' ,-. ,-. |_ . ,-. . . ,-. ,-. |- . ,-. ,-.   |    ,-. ,-. ,-| . ,-. ,-.     ,    '|__/ ,-. ,-. ,-. . ,-. ,-. `'
  //  |  -. | | | | |  | | | | | |   ,-| |  | | | | |   |    | | ,-| | | | | | | |    /     .|    ,-| |   `-. | | | | | ,.
  //  `---' `-' ' ' |  ' `-| `-' '   `-^ `' ' `-' ' '   `--' `-' `-^ `-' ' ' ' `-|   '      `'    `-^ '   `-' ' ' ' `-| `'
  //                '     ,|                                                    ,|                                   ,|
  //                      `'                                                    `'                                   `'

  /**
    * Data type to represent configuration that is supplied to this program - compiletime, runtime, or otherwise.
    *
    * @param name A name to say hello to
    * @param iterations How many times to say hello
    */
  final case class Configuration(name: String, iterations: Short)

  /**
    * Parse configuration files loaded via java `resources/application.conf`.
    *
    * Pureconfig and Typesafe Config do the job of loading and parsing.
    *
    * @return a loaded & parsed Configuration object
    */
  def parseConfiguration(): Configuration = {

    // This is an example of...
    //
    // 1. Loading a Config object via Typesafe's Config lib, and
    // 2. Reading that Config object via pureconfig into OUR object that we control
    //
    //
    // Typesafe's config library provides a lot out of the box:
    // - *.conf, *.properties, *.json
    // - Multi-config merging
    // - ENV substitution
    // - Variable substitution
    //
    // Supported (and pre-parsed) data types brought in by Pureconfig+Typesafe:
    // - Primitives & friends (time, duration, byte sizes)
    // - Files
    // - Maps
    // - Regex
    //
    // Pureconfig also provides the scala stuff, like:
    // - Compile-time config mapping to typesafe config (via macros)
    // - Integration with shapeless for non-uniform configs (!)
    //
    //
    // These are much more comprehensive than scopt, so read as you need it.
    // If you're writing a config-heavy app, invest some time to read up on your options from typesafe config.
    // @see https://github.com/typesafehub/config AND https://github.com/pureconfig/pureconfig
    // TODO: Move the data type "Configuration" along with the configuration parsing process, then test them (or remove them from your project).
    // TODO: As your project grows, revisit pureconfig to see how you can decompose your config into smaller pieces (and how to namespace it).

    // TODO: Depending on the usage of your program, this may need to be
    //  1. more flexible (won't path correctly if being delegated, maybe this is a file input by user, etc...)
    //  2. more durable (how do you proceed when it fails to load, malformed, etc...):
    import pureconfig.generic.auto._
    val loadedConfig = ConfigSource.default.loadOrThrow[Configuration]

    loadedConfig
  }


}
