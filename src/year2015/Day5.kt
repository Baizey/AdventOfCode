package year2015

import utils.*

fun main() {
    val lines = Input.get(2015, 5)

    fun part1() {
        val result = lines.filter { line ->
            val vowels = line.count { "aeiou".contains(it) }
            if (vowels < 3) return@filter false

            var hasDouble = false
            for (i in line.indices)
                if (i + 1 < line.length && line[i] == line[i + 1]) hasDouble = true
            if (!hasDouble) return@filter false

            if (line.contains("ab") || line.contains("cd") || line.contains("pq") || line.contains("xy"))
                return@filter false

            true
        }.count()

        println(result)
    }

    fun part2() {
        val result = lines.filter { line ->
            var hasDoublePair = false
            for (i in line.indices) {
                if (i + 2 >= line.length) continue
                val part = line.substring(i, i + 2)
                if (line.substring(i + 2).contains(part)) hasDoublePair = true
            }
            if (!hasDoublePair) return@filter false

            var hasDouble = false
            for (i in line.indices)
                if (i + 2 < line.length && line[i] == line[i + 2]) hasDouble = true
            if (!hasDouble) return@filter false

            true
        }.count()

        println(result)
    }

    part1()
    part2()
}