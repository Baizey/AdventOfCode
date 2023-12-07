package utils

import java.io.File
import java.io.IOException
import java.net.URI

object Input {
    fun get(year: Int, day: Int): List<String> {
        println("Challenge: https://adventofcode.com/$year/day/$day")

        val file = File("resources\\$year\\day_$day.txt")
        if (file.exists()) {
            val lines = file.readLines()
            if (lines.isNotEmpty()) return lines
        }

        file.parentFile.mkdirs()
        file.createNewFile()

        try {
            val source = URI("https://adventofcode.com/$year/day/$day/input")
            val lines = source.toURL().openStream().bufferedReader().useLines(Sequence<String>::toList)
            file.writeText(lines.joinToString(separator = System.lineSeparator()))
            return lines
        } catch (e: IOException) {
            throw Exception(
                """
                Grab content manually
                Input:     https://adventofcode.com/$year/day/$day/input,
                File :    ${file.absolutePath},
            """.trimIndent(),
                e
            )
        }
    }
}