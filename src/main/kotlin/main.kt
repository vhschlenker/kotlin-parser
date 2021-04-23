import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No projectdir given")
        return
    }
    val projectDir = args[0]
    val projectDirPath = Paths.get(projectDir)
    if (Files.notExists(projectDirPath)) {
        println("Given projectdir does not exist")
        return
    }
    if (!Files.isDirectory(projectDirPath)) {
        println("Given projectdir is not a directory")
        return
    }
    val visitor = FileVisitor(KtParser())
    Files.walkFileTree(projectDirPath, visitor)
    val list = visitor.getKotlinFileList()
    println("Found ${list.size} kt files")
    list.forEach {
        printSeperator()
        println("file: ${it.name}")

        printSeperator()
        val imports = it.importList?.imports
        println("imports:")
        imports?.forEach { importDirective ->
            println(importDirective.importPath)
        } ?: println("none")

        printSeperator()

        val children = it.children
        println("children:")
        children.forEach { child ->
            when (child) {
                is KtClass -> println("class: ${child.name}")
                is KtNamedFunction -> println("function: ${child.name}")
            }
        }

        printSeperator()
        println()
    }
}

private fun printSeperator() {
    println("--------------------")
}