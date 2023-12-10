import java.io.File
import java.time.LocalDate
import java.time.Month
import kotlin.math.min

fun main() {
    Generator.generate(2023)
}

object Generator {
    fun generate(usedYear: Int? = null) {
        fun createEmptyYear(year: Int, maxDay: Int) {
            for (day in 1..maxDay) {
                val content = """
            package year$year
            import utils.*
            
            fun main() {
                val lines = Input.get($year, $day)
                
                fun part1() {
                
                }
                fun part2() {
                
                }
                part1()
                part2()
            }
        """.trimIndent()
                val dayFile = File("src\\year$year\\Day$day.kt")
                if (dayFile.exists()) continue
                dayFile.parentFile.mkdirs()
                dayFile.createNewFile()
                dayFile.writeText(content)
            }
        }

        val today = LocalDate.now()
        val currentYear = today.year
        val year = usedYear ?: currentYear
        val maxDay =
            if (currentYear == year && today.month == Month.DECEMBER)
                min(today.dayOfMonth, 25)
            else if (currentYear == year) 0
            else 25
        createEmptyYear(year, maxDay)
    }
}