package utils

import utils.grid.Direction.none
import utils.Input.submit
import utils.grid.Direction
import utils.grid.Nav

object Helpers {
    fun Any.println(level: Int? = null) = if (level != null) submit(level) else println(this)

    fun <T> List<List<T>>.clone() = this.map { line -> line.map { it }.toMutableList() }

    fun <T> List<List<T>>.surround(symbol: T): List<List<T>> {
        val top = listOf(List(this[0].size + 2) { symbol })
        val middle = map { line -> listOf(symbol) + line + listOf(symbol) }
        val bottom = listOf(List(this[0].size + 2) { symbol })
        return top + middle + bottom
    }

    fun <T> List<List<T>>.findExact(filter: (T) -> Boolean): Nav? {
        val maxY = this.lastIndex
        val maxX = this[0].lastIndex
        for (y in 0..maxY)
            for (x in 0..maxX)
                if (filter(this[y][x])) return Nav(x, y)
        return null
    }

    fun <T> List<List<T>>.findMatches(filter: (T) -> Boolean): List<Nav> {
        val result = mutableListOf<Nav>()
        val maxY = this.lastIndex
        val maxX = this[0].lastIndex
        for (y in 0..maxY)
            for (x in 0..maxX)
                if (filter(this[y][x]))
                    result.add(Nav(x, y))
        return result
    }

    fun <T> List<List<T>>.groupMatches(filter: (T) -> Boolean = { false }): Map<T, List<Nav>> {
        val result = mutableMapOf<T, MutableList<Nav>>()
        val maxY = this.lastIndex
        val maxX = this[0].lastIndex
        for (y in 0..maxY)
            for (x in 0..maxX)
                if (filter(this[y][x]))
                    result.computeIfAbsent(this[y][x]) { mutableListOf() }.add(Nav(x, y))
        return result
    }
}