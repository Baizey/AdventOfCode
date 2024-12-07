import java.io.File
import java.time.LocalDate
import java.time.Month
import kotlin.math.min

fun main() {
    TemplateGenerator.generate(2024)
}

object TemplateGenerator {
    fun generate(usedYear: Int? = null) {
        fun createEmptyYear(year: Int, maxDay: Int) {

            for (day in 1..maxDay) {
                migrate1(year, day)
                val dayWithStart = String.format("%02d", day)
                val content = """
            package year$year
            import utils.*
            
            fun main() {
                val input = Input.get($year, $day)
                
                fun part1() {
                    
                }
                fun part2() {
                    
                }
                part1()
                part2()
            }
        """.trimIndent()
                val dayFile = File("src\\year$year\\Day$dayWithStart.kt")
                if (dayFile.exists()) continue
                dayFile.parentFile.mkdirs()
                dayFile.createNewFile()
                dayFile.writeText(content)
            }
        }

        val today = LocalDate.now()
        val currentYear = today.year
        val year = usedYear ?: currentYear
        val maxDay = 25
        createEmptyYear(year, maxDay)
    }

    private fun migrate1(year: Int, day: Int) {
        val oldFile = File("src\\year$year\\Day$day.kt")
        if (oldFile.exists()) {
            val content = oldFile.readText()
            val dayWithStart = String.format("%02d", day)
            val newFile = File("src\\year$year\\Day$dayWithStart.kt")
            newFile.createNewFile()
            newFile.writeText(content)
            oldFile.delete()
        }
    }
}