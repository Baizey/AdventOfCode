import java.io.File
import java.time.LocalDate
import java.time.Month
import kotlin.math.min

fun main() {
    Generator.generate()
}

object Generator {
    fun generate() {
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
                fun part3() {
                
                }
                part1()
                part2()
                part3()
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
        for (year in 2015..currentYear) {
            println(year)
            val maxDay =
                if (currentYear == year && today.month == Month.DECEMBER)
                    min(today.dayOfMonth, 25)
                else if (currentYear == year) 0
                else 25
            createEmptyYear(year, maxDay)
        }
    }
}