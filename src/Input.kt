import java.io.File
import java.io.IOException
import java.net.URI

object Input {
    private val cache: Array<List<String>> = Array(30) { listOf() }

    fun day(day: Int): List<String> {
        if (cache[day].isNotEmpty()) return cache[day]

        val file = File("resources\\day_$day.txt")
        if (file.exists()) {
            val lines = file.readLines()
            cache[day] = lines
            if (lines.isNotEmpty()) return lines
        }

        file.parentFile.mkdirs()
        file.createNewFile()

        try {
            val source = URI("https://adventofcode.com/2023/day/$day/input")
            val lines = source.toURL().openStream().bufferedReader().useLines(Sequence<String>::toList)
            file.writeText(lines.joinToString(separator = System.lineSeparator()))
            cache[day] = lines
            return lines
        } catch (e: IOException) {
            throw Exception("Grab content manually: https://adventofcode.com/2023/day/$day/input", e)
        }
    }
}