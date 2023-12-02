import java.io.File
import java.net.URL

object Input {
    private val cache: Array<List<String>> = Array(30) { listOf() }

    fun day(day: Int): List<String> {
        if (cache[day].isNotEmpty()) return cache[day]

        val fileName = "day_$day.txt"
        val resource: URL? = object {}.javaClass.classLoader.getResource(fileName)
        if (resource != null) {
            val lines = resource.openStream().bufferedReader().readLines()
            val content = lines.joinToString(separator = "\n")
            cache[day] = lines
            if (content.isNotEmpty()) return lines
        }

        val fileCache = File("D:\\Repositories\\AdventOfCode2023\\resources\\$fileName")
        fileCache.parentFile.mkdirs()
        fileCache.createNewFile()

        /* Error 400: auth :(
        val source = URI("https://adventofcode.com/2023/day/$day/input")
        val content = source.toURL().openStream().bufferedReader().readLines().joinToString(separator = "\n")
        val writer = file.bufferedWriter()
        writer.write(content)
        writer.flush()
        writer.close
        cache[day] = content
        return content
        / */

        TODO("Fill file with content: https://adventofcode.com/2023/day/$day/input")
    }
}