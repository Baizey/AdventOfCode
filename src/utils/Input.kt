package utils

import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.net.URLEncoder.encode
import kotlin.text.Charsets.UTF_8

data class InputData(private val rawText: List<String>) {
    fun asIntLines() = rawText.map { line -> "\\d+".toRegex().findAll(line).map { it.value.toInt() }.toList() }
    fun asLongLines() = rawText.map { line -> "\\d+".toRegex().findAll(line).map { it.value.toLong() }.toList() }
    fun asCharGrid() = rawText.map { line -> line.toCharArray().toList() }
    fun asString() = rawText.joinToString(separator = "\n")
    fun asLines() = rawText
}

object Input {
    private var year: Int = 0
    private var day: Int = 0

    fun get(year: Int, day: Int): InputData {
        this.year = year
        this.day = day
        migrate1(year, day)

        val fileDay = String.format("%02d", day)
        val file = File("resources\\$year\\day_$fileDay.txt")
        printInfo(
            listOf(
                "Info  : https://adventofcode.com/$year/day/$day",
                "Input : https://adventofcode.com/$year/day/$day/input",
                "File  : file:///${file.absolutePath.replace(Regex("\\\\"), "/")}",
            )
        )

        if (file.exists()) {
            val lines = file.readLines()
            if (lines.isNotEmpty()) return InputData(lines)
        }

        file.parentFile.mkdirs()
        file.createNewFile()

        val source = URI("https://adventofcode.com/$year/day/$day/input").toURL().openConnection() as HttpURLConnection
        try {
            val session = Config.get()["session"]
            source.setRequestProperty("Cookie", "session=$session")
            val lines = BufferedReader(InputStreamReader(source.inputStream)).use { it.lines().toList() }
            file.writeText(lines.joinToString(separator = System.lineSeparator()))
            return InputData(lines)
        } catch (e: IOException) {
            throw Exception("Grab input from link and put in file")
        } finally {
            source.disconnect()
        }
    }

    fun Any?.submit(level: Int = 1) {
        println("Submit level $level: $this")
        val source = URI("https://adventofcode.com/$year/day/$day/answer").toURL().openConnection() as HttpURLConnection
        try {
            val session = Config.get()["session"]
            val contentValues = listOf("level" to level.toString(), "answer" to this.toString())
            val content = contentValues.joinToString("&") { "${encode(it.first, UTF_8)}=${encode(it.second, UTF_8)}" }
            source.requestMethod = "POST"
            source.setRequestProperty("Cookie", "session=$session")
            source.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            source.doOutput = true
            source.outputStream.use { it.write(content.toByteArray(UTF_8)) }
            val responseMessage = source.inputStream.bufferedReader().use { it.readText() }
            val message = responseMessage.substring(
                responseMessage.indexOf("<main>"),
                responseMessage.lastIndexOf("</main>")
            ).replace(Regex("[<\\[]/?[\\w/\"= ]+[>\\]_:]"), "").trim()
            println("Response: $message")

        } catch (e: IOException) {
            throw Exception("Grab session token form site and put in config.properties : session property")
        } finally {
            source.disconnect()
        }
    }

    private fun printInfo(lines: List<String>) {
        val max = lines.maxOf { it.length }
        val br = "+-" + "-".repeat(max) + "-+"
        println(br)
        for (line in lines) {
            println("| $line${" ".repeat(max - line.length)} |")
            println(br)
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