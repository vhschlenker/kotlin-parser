import org.jetbrains.kotlin.psi.KtFile
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes


class FileVisitor(private val parser: KtParser) : SimpleFileVisitor<Path>() {
    private val kotlinFileList: MutableList<KtFile> = mutableListOf()
    private val matcher = FileSystems.getDefault().getPathMatcher("glob:*.kt")

    private fun parse(file: Path) {
        val name = file.fileName.toString()
        val content = Files.readString(file)
        val ktFile = parser.parse(name, content)
        kotlinFileList.add(ktFile)
    }

    private fun find(file: Path) {
        val name = file.fileName
        if (name != null && matcher.matches(name)) {
            parse(file)
            println("found $name")
        }
    }

    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        file?.let { find(it) }
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
        System.err.println(exc)
        return FileVisitResult.CONTINUE
    }

    fun getKotlinFileList(): List<KtFile> {
        return kotlinFileList.toList()
    }
}