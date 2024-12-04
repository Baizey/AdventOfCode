package utils

import java.io.File
import java.io.IOException
import java.net.URI

object Input {

    data class InputData(private val rawText: List<String>) {
        fun asIntLines(separator: String = " ") = rawText.map { line -> line.split(separator).filter { it.isNotEmpty() }.map { it.toInt() } }
        fun asCharGrid() = rawText.map { line -> line.chars().mapToObj { it.toChar() }.toList() }
        fun asString() = rawText.joinToString(separator = "\n")
        fun asLines() = rawText
    }

    fun get(year: Int, day: Int): InputData {
        migrate1(year, day)

        println("Challenge: https://adventofcode.com/$year/day/$day")

        val fileDay = String.format("%02d", day)
        val file = File("resources\\$year\\day_$fileDay.txt")
        if (file.exists()) {
            val lines = file.readLines()
            if (lines.isNotEmpty()) return InputData(lines)
        }

        file.parentFile.mkdirs()
        file.createNewFile()

        try {
            val source = URI("https://adventofcode.com/$year/day/$day/input")
            val lines = source.toURL().openStream().bufferedReader().useLines(Sequence<String>::toList)
            file.writeText(lines.joinToString(separator = System.lineSeparator()))
            return InputData(lines)
        } catch (e: IOException) {
            println(
                """
                Grab input manually
                Input:     https://adventofcode.com/$year/day/$day/input
                File :     file:///${file.absolutePath.replace(Regex("\\\\"), "/")}
            """.trimIndent()
            )
            throw Exception()
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