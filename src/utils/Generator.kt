package utils

import java.io.File

fun main() {
    fun createEmptyYear(year: Int) {
        for (day in 1..24) {
            val content = """
            package year$year
            import utils.*
            
            fun main() {
                val lines = Input.day($year, $day)
            }
        """.trimIndent()
            val dayFile = File("src\\year$year\\Day$day.kt")
            if (dayFile.exists()) continue
            dayFile.parentFile.mkdirs()
            dayFile.createNewFile()
            dayFile.writeText(content)
            try {
                Input.get(year, day)
            } catch (e: Exception) {
                // ignored
            }
        }
    }

    for (year in 2015..2023) {
        println(year)
        createEmptyYear(year)
    }
}