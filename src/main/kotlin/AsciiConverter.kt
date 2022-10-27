import kotlinx.coroutines.*
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.round
import kotlin.system.measureTimeMillis

/**
 * takes in an image
 * source is the original image in full,
 * px is the pixel size that each character will represent
 */

const val charDensityList: String = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'."
class AsciiConverter(densityChar: String = charDensityList) {
    data class CharPixel(var color: Color = Color.BLACK, var char: Char = '0') //used to hold information when processing

    /**
     * Converts one image
     */
    fun convertImage(source: BufferedImage, px: Int, withColor: Boolean = true): BufferedImage {
        //cannot convert if source height and width are not evenly divisible by px
        if (source.height % px != 0 || source.width % px != 0)
            throw ExceptionInInitializerError("px needs to be able to evenly divide source image! (there was a remainder when dividing source width or height by px)")

        val numOfCols = source.width / px
        val numOfRows = source.height / px

        //2d CharPixel array
        val charPixel = Array(numOfRows) { Array(numOfCols) {CharPixel()} }

        //now pass each piece to convertChunk and get character, place character in 2d array
        runBlocking {
            for (i in 0 until numOfRows) {
                for (j in 0 until numOfCols) {
                    launch {
                        val pixel: CharPixel = convertChunk(source.getSubimage(j * px, i * px, px, px))
                        charPixel[i][j] = if(withColor) pixel else pixel.copy(color = Color.WHITE)
                    }
                }
            }
        }

        return createImage(charPixelArray = charPixel)
    }

    /**
     * Look at each pixel and average the values, then index the charDensityList based on that value and return the character
     */
    private fun convertChunk(source: BufferedImage): CharPixel {

        val width = source.width
        val height = source.height
        val pxCount = width * height

        val pixels = source.getRGB(0,0,width, height, null, 0, width).map {Color(it)}
        val value = pixels.sumOf {(.5) * ((maximum(it.red, it.green, it.blue) / 255.0) + (minimum(it.red, it.green, it.blue) / 255.0))} / pxCount
        val color = Color(pixels.sumOf { it.red }/pxCount, pixels.sumOf { it.green }/pxCount, pixels.sumOf { it.blue }/pxCount)
        /*
     * now finds character to return, since value is going to be from 0-1, we can multiply by length of the charString
     * then round to an integer since we will be indexing said charString, finally since low numbers have higher value
     * in the char string, we will subtract by the length so that it inverts the value.
     * also each of the charDensityList.length needs to be - 1 in order to not generate index out of bound error
     */
        val returnChar = charDensityList[((charDensityList.length - 1) - round(x = value * (charDensityList.length - 1))).toInt()]
        return CharPixel(color, returnChar)
    }

    /**
     * Creates then return an image based on the char array
     */
    private fun createImage(charPixelArray: Array<Array<CharPixel>>): BufferedImage {
        val charPX: Int = 16 //how big each character will be drawn to be

        val bufferedImage =
            BufferedImage(charPixelArray[0].size * charPX, charPixelArray.size * charPX, BufferedImage.TYPE_INT_RGB)
        val graphicsImage = bufferedImage.graphics

        for ((i, row) in charPixelArray.withIndex()) {
            for ((j, entry) in row.withIndex()) {
                graphicsImage.font = Font("Courier", Font.PLAIN, charPX)
                graphicsImage.color = entry.color
                graphicsImage.drawString(entry.char.toString(), j * charPX, i * charPX + charPX)
            }
        }
        val jPanel = JPanel()
        jPanel.printAll(graphicsImage)
        return bufferedImage
    }

    private fun minimum(vararg ints: Int) = ints.min()
    private fun maximum(vararg ints: Int) = ints.max()

}
