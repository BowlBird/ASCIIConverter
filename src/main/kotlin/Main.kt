import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

// Converts an image into ascii

fun main() {
    val user = UserInterface() //input and output object
    user.output("Welcome!")
    user.output("Asking for input...")

    val imageList = user.askForImageList()

    user.output("Selected ${imageList.size} images successfully.")
    val outputDirectory = user.askForDirectory("Please select Output Directory.")

    val px = user.askForInt("\nPlease enter px (needs to be a multiple of width and height of the image(s).")

    val asciiConverter = AsciiConverter()
    user.output("Converting to ascii")


    //now process each image
    val asciiImageList = asciiConverter.convertImages(imageList as List<BufferedImage>, px = px)

    user.output("Writing final images.")
    //now write images
    for((index, image) in asciiImageList.withIndex()) {
        println(" writing image ${index + 1}...")
        ImageIO.write(image, "png", File("${outputDirectory.absolutePath}\\${index + 180}.png"))
        println(" image ${index + 1} written.")
    }
    println("completed!")
}