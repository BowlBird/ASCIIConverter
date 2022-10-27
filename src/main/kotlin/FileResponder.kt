import java.io.File

class FileResponder(directory: File) {
    private val fileList = mutableListOf<File>()
    val hasAnotherElement: Boolean
        get() { return fileList.isNotEmpty()}
    val elementsLeft: Int
        get() {return fileList.count()}


    init {
        val children = directory.listFiles() ?: arrayOf<File>() //elvis operator that makes a null array into an empty one
        children.forEach {
            fileList.add(it)
        }
    }

    fun popFirst() = if(hasAnotherElement) fileList.removeFirst() else null
}