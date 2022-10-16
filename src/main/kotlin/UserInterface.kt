import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Image
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame
import kotlin.system.measureTimeMillis

class UserInterface {

    /**
     * returns int
     */
    fun askForInt(message: String = ""): Int {
        output(message)
        return try { return readln().toInt() } catch(ex: NumberFormatException) { askForInt("Invalid input. Please enter an Int:")}
    }

    /**
     * returns boolean
     */
    fun askForBool(message: String = ""): Boolean {
        output(message)
        return try { return readln().toBoolean() } catch(ex: NumberFormatException) { askForBool("Invalid input. Please enter an Int:")}
    }

    fun askForString(message: String = ""): String {
        output(message)
        return readln()
    }

    /**
     * returns directory location from user.
     */
    fun askForDirectory(message: String = ""): File {
        output(message)
        val file  = File(askForString("Please enter directory:"))

        return if(file.isDirectory) file else askForDirectory("Please select a directory.")
    }

    /**
     * returns a list of images
     */
    fun askForImageList(): List<Image> {
        //asks for user image folder.
        val filesDialog = openFileDialog(fileType = ".png")

        //checks to see if anything was selected
        if(filesDialog.isEmpty()) {
            output("Please selected valid image(s)")
            askForImageList()
        }

        val files = filesDialog.toList()
        val imageList = mutableListOf<Image>()

        try { files.forEach { imageList.add(ImageIO.read(it)) } }
        catch (ioe: IOException) {
            output("Failed. One or more files were not images.")
            askForImageList()
        }
        return imageList
    }

    /**
     * Opens window to select files
     */
    private fun openFileDialog(startingDirectory: String = "C:\\", fileType: String): Array<File> {
        val fileWindow = FileDialog(JFrame(), "Open Files", FileDialog.LOAD)
        fileWindow.directory = "C:\\"
        fileWindow.file = fileType
        fileWindow.isMultipleMode = true
        fileWindow.isVisible = true
        return fileWindow.files
    }

    /**
     * An interface for print
     */
    fun output(output: String, hasNewLine: Boolean = true) {
        print("$output ${if(hasNewLine) "\n" else ""}" )
    }
}