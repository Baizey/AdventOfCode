import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL

object Input {
    private val cache: Array<List<String>> = Array(30) { listOf() }

    fun day(day: Int): List<String> {
        if (cache[day].isNotEmpty()) return cache[day]

        val fileName = "day_$day.txt"
        val resource: URL? = object {}.javaClass.classLoader.getResource(fileName)
        if (resource != null) {
            val lines = resource.openStream().bufferedReader().useLines(Sequence<String>::toList)
            val content = lines.joinToString(separator = "\n")
            cache[day] = lines
            if (content.isNotEmpty()) return lines
        }

        val a = File("").absoluteFile.path
        val fileCache = File("${File("").absoluteFile.path}\\resources\\$fileName")
        fileCache.parentFile.mkdirs()
        fileCache.createNewFile()

        try {
            val source = URI("https://adventofcode.com/2023/day/$day/input")
            val lines = source.toURL().openStream().bufferedReader().useLines(Sequence<String>::toList)
            val content = lines.joinToString(separator = "\n")
            val writer = fileCache.bufferedWriter()
            writer.write(content)
            writer.flush()
            writer.close()
            cache[day] = lines
            return lines
        } catch (e: IOException) {
            throw Exception("Grab content manually: https://adventofcode.com/2023/day/$day/input", e)
        }
    }
}