import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.concurrent.thread

// Converts an image into ascii

/* --== How I want it to work ==-- */ /**
 *
 * Ask for file reference to directory where all images reside.
 * Open a scanner on said file reference.
 * Then using a designated amount of threads, do the following steps in parallel
 * -take in a file reference to the image(until empty scanner)
 * -ask user for how big each chunk of the image should be (need to be multiples of the image file size)
 * --should give a list of possible multiples to select from
 * -chunk image into those pieces, each chunk since there could a lot of them should be processed in a coroutine
 * --each chunk should have a corresponding color and ascii character that it will return
 * -now draw each character with its color to the final image and write it to a directory given
 * --name should be based off of the file that was passed in.
 *
 */

const val threadCount = 8
fun main() {
    val user = UserInterface() //input and output object
    user.output("> Welcome!", color = Color.GREEN)
    val imageDirectory = user.askForDirectory("> Please enter a directory containing all the image files you would like processed.", messageColor = Color.GREEN, inputColor = Color.CYAN)
    val outputDirectory = user.askForDirectory("> Please enter a directory where you would like images to be output to.", messageColor = Color.GREEN, inputColor = Color.CYAN)
    val fr = FileResponder(imageDirectory)



    user.output("> Input ${fr.elementsLeft} files.")
    user.output("")

    /* settings */
    val px = user.askForInt("> Please input a valid px (needs to be a multiple of width and height of all images.", messageColor = Color.GREEN, inputColor = Color.CYAN)
    val colorMatching = user.askForBool("> Do you want the images to be in color? (true/false)", messageColor = Color.GREEN, inputColor = Color.CYAN)

    /* creates threadCount threads */
    repeat(threadCount) {
        thread {
            user.output("> Thread $it has started")
            var count = 0
            while(fr.hasAnotherElement) {
                count++
                user.output("> Thread $it has started image $count")

                val file = fr.popFirst() ?: break //needed null because of threading, now lets handle.
                user.output("> ($it, $count) has gotten file reference.")

                val image = ImageIO.read(file)
                user.output("> ($it, $count) has converted the file to an image.")

                val ac = AsciiConverter()
                val asciiImage = ac.convertImage(image, px, colorMatching)
                user.output("> ($it, $count) has converted the image to an ascii image.")

                ImageIO.write(asciiImage, "png", File("${outputDirectory.absolutePath}\\${file.name}"))
                user.output("> ($it, $count) has been written!", color = Color.YELLOW)
            }
        }
    }
}