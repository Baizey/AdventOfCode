package utils

import java.io.File
import java.io.IOException
import java.net.URI

object Input {
    fun get(year: Int, day: Int): List<String> {
        migrate1(year, day)

        println("Challenge: https://adventofcode.com/$year/day/$day")

        val fileDay = String.format("%02d", day)
        val file = File("resources\\$year\\day_$fileDay.txt")
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

    private fun migrate1(year: Int, day: Int) {
        val oldFile = File("resources\\$year\\day_$day.txt")
        if (oldFile.exists()) {
            val content = oldFile.readText()
            val dayWithStart = String.format("%02d", day)
            val newFile = File("resources\\$year\\day_$dayWithStart.txt")
            newFile.createNewFile()
            newFile.writeText(content)
            oldFile.delete()
        }
    }
}