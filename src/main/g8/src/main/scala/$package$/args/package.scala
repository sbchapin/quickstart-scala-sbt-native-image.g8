package $package$

import scopt.OptionParser

package object args {

  //      ,.                             .    .-,--.                               .-,--.                            .      .            ,.
  //     / |   ,-. ,-. . . ,-,-. ,-. ,-. |-    '|__/ ,-. ,-. ,-. . ,-. ,-.     ,   ' |   \ ,-. ,-. . . ,-,-. ,-. ,-. |- ,-. |- . ,-. ,-. `'
  //    /~~|-. |   | | | | | | | |-' | | |     .|    ,-| |   `-. | | | | |    /    , |   / | | |   | | | | | |-' | | |  ,-| |  | | | | | ,.
  //  ,'   `-' '   `-| `-' ' ' ' `-' ' ' `'    `'    `-^ '   `-' ' ' ' `-|   '     `-^--'  `-' `-' `-' ' ' ' `-' ' ' `' `-^ `' ' `-' ' ' `'
  //                ,|                                                  ,|
  //                `'                                                  `'

  /**
    * Data type to represent any arguments that a user passes to the program.
    *
    * @param name A name to say hello to
    */
  final case class Arguments(name: String)


  /**
    * Parse user-input args into cleanly defined "Arguments" object.
    *
    * Scopt does the job of parsing.
    *
    * @param rawArgs raw args straight from the command line.
    * @param defaultName a default name to give to args
    * @return parsed Arguments.
    */
  def parseArguments(rawArgs: Array[String], defaultName: String): Arguments = {
    // Defaulted arguments instance
    val defaultArguments = Arguments(
      name = defaultName
    )

    // This is an example of an argument parser provided by scopt.
    //
    // Scopt can do a lot:
    // - Parsing
    // - Preprocessing
    // - Validation
    // - Documentation
    //
    // Supported (and pre-parsed) data types via scopt:
    // - Primitives & friends (time, duration, byte sizes)
    // - Case classes
    // - Files
    // - Tuples
    // - Maps
    // - Lists of files
    //
    // Save yourself some time, read the docs (< 5 min):
    // @see https://github.com/scopt/scopt
    // TODO: Move the data type "Arguments" along with this argParser, then test them (or remove them from your project)
    // TODO: Add a better description in head(...)
    // TODO: Add more options to arguments (firstName, lastName, personToGreet)
    // TODO: Add commands/subcommands to differentiate commands & subcommands of your program (like git _add_ or git _commit_)

    val argParser: OptionParser[Arguments] = new scopt.OptionParser[Arguments]("$name;format="norm"$") {

      head("$name$", "You'll be wanting to set a decent description for the help text of your program here")

      help("help").text("prints this usage text")

      opt[String]('n', "name")
        .text("the name that the default toy example will greet")
        .optional()
        .action((newName, conf) => conf.copy(name = newName))
    }

    argParser.parse(rawArgs.toSeq, defaultArguments).getOrElse(defaultArguments)
  }



}
