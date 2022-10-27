import java.awt.Color
import java.io.File


class UserInterface {
    /**
     * returns int
     */
    fun askForInt(message: String = "") = askForConverted(message, String::toInt)
    /**
     * returns boolean
     */
    fun askForBool(message: String = "") = askForConverted(message, String::toBoolean)
    /**
     *  returns string
     */
    fun askForString(message: String = "") = askForConverted(message, String::toString)
    /**
     * returns directory location from user.
     */
    fun askForDirectory(message: String = "") = askForConverted(message) {
        File(it)
    }
    /**
     * Converts input to datatype using provided method
     */
    private fun <T> askForConverted(message: String, conversionFunction: (String) -> T ): T {
        output(message)
        output(">>> ", hasNewLine = false)
        return try {return conversionFunction(readln())} catch(ex: Exception) { askForConverted(message, conversionFunction)}
    }
    /**
     * An interface for print
     * Supports ANSI colors: BLACK, RED, GREEN, YELLOW, BLUE, CYAN, and WHITE
     */
    fun output(output: String, hasNewLine: Boolean = true) {
        print("$output${if(hasNewLine) "\n" else ""}" )
    }
}