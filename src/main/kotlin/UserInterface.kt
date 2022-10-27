import java.awt.Color
import java.io.File


class UserInterface {
    /**
     * returns int
     */
    fun askForInt(message: String = "", messageColor: Color = Color.WHITE, inputColor: Color = Color.WHITE) = askForConverted(message, messageColor, inputColor, String::toInt)
    /**
     * returns boolean
     */
    fun askForBool(message: String = "", messageColor: Color = Color.WHITE, inputColor: Color = Color.WHITE) = askForConverted(message, messageColor, inputColor, String::toBoolean)
    /**
     *  returns string
     */
    fun askForString(message: String = "", messageColor: Color = Color.WHITE, inputColor: Color = Color.WHITE) = askForConverted(message, messageColor, inputColor, String::toString)
    /**
     * returns directory location from user.
     */
    fun askForDirectory(message: String = "", messageColor: Color = Color.WHITE, inputColor: Color = Color.WHITE) = askForConverted(message, messageColor, inputColor) {
        File(it)
    }
    /**
     * Converts input to datatype using provided method
     */
    private fun <T> askForConverted(message: String, messageColor: Color, inputColor: Color, conversionFunction: (String) -> T ): T {
        output(message, color = messageColor)
        output(">>> ", color = inputColor, hasNewLine = false)
        return try {return conversionFunction(readln())} catch(ex: Exception) { askForConverted<T>(message, Color.RED, inputColor, conversionFunction)}
    }
    /**
     * An interface for print
     * Supports ANSI colors: BLACK, RED, GREEN, YELLOW, BLUE, CYAN, and WHITE
     */
    fun output(output: String, color: Color = Color.WHITE, hasNewLine: Boolean = true) {
        val consoleColor = when(color) {
            Color.BLACK -> "\u001B[30m"
            Color.RED ->   "\u001B[31m"
            Color.GREEN -> "\u001B[32m"
            Color.YELLOW ->"\u001B[33m"
            Color.BLUE -> "\u001B[34m"
            Color.CYAN -> "\u001B[36m"
            Color.WHITE ->"\u001B[37m"
            else -> "\u001B[37m"
        }

        print("$consoleColor$output\u001B[0m${if(hasNewLine) "\n" else ""}" )
    }
}