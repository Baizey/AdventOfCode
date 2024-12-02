package year2023

import utils.*

fun main() {
    val lines = Input.get(2023, 3)

    fun Char.isSymbol() = !(this.isDigit() || this == '.')

    fun Char.isStar(): Boolean = this == '*'

    fun get(i: Int, j: Int) = try {
        lines[i][j]
    } catch (e: Exception) {
        '.'
    }

    fun getNum(i: Int, j: Int): Int? {
        if (!get(i, j).isDigit()) return null
        val line = lines[i]
        val sb = StringBuilder()

        var start = j
        while (get(i, start - 1).isDigit()) start--

        while (get(i, start).isDigit()) {
            sb.append(get(i, start))
            start++
        }

        return sb.toString().toInt()
    }

    fun part1() {
        var result = 0
        lines.forEachIndexed { lineIndex, line ->
            var start = 0
            while (start < line.length) {
                val char = line[start]
                if (char.isDigit()) {
                    val sb = StringBuilder()
                    var end = start
                    for (i in start..line.lastIndex) {
                        if (!line[i].isDigit()) break
                        sb.append(line[i])
                        end = i
                    }
                    val number = sb.toString().toInt()
                    var isSymbol = get(lineIndex, start - 1).isSymbol()
                            || get(lineIndex - 1, start - 1).isSymbol()
                            || get(lineIndex + 1, start - 1).isSymbol()
                            || get(lineIndex, end + 1).isSymbol()
                            || get(lineIndex - 1, end + 1).isSymbol()
                            || get(lineIndex + 1, end + 1).isSymbol()
                    for (i in start..end) {
                        isSymbol = isSymbol
                                || get(lineIndex - 1, i).isSymbol()
                                || get(lineIndex + 1, i).isSymbol()
                    }
                    if (isSymbol) result += number
                    start = end + 1
                } else start++
            }
        }
        println(result)
    }
    //part1()

    fun part2() {
        var result = 0
        lines.forEachIndexed { index, line ->
            var c = 0
            while (c < line.length) {
                val char = line[c]
                if (char.isStar()) {
                    var nums = ArrayList<Int>()
                    var n = getNum(index, c - 1)
                    if (n != null) nums.add(n)
                    n = getNum(index, c + 1)
                    if (n != null) nums.add(n)

                    n = getNum(index - 1, c)
                    if (n != null) nums.add(n)
                    else {
                        n = getNum(index - 1, c + 1)
                        if (n != null) nums.add(n)
                        n = getNum(index - 1, c - 1)
                        if (n != null) nums.add(n)
                    }
                    n = getNum(index + 1, c)
                    if (n != null) nums.add(n)
                    else {
                        n = getNum(index + 1, c + 1)
                        if (n != null) nums.add(n)
                        n = getNum(index + 1, c - 1)
                        if (n != null) nums.add(n)
                    }
                    if (nums.size == 2) {
                        result += nums.reduce { a, b -> a * b }
                    }
                }
                c++
            }
        }
        println(result)
    }
    part2()
}