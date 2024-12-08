package utils

import utils.Input.submit

object Helpers {
    fun Any.println(level: Int? = null) = if (level != null) submit(level) else println(this)

    fun <T> List<List<T>>.clone() = this.map { line -> line.map { it }.toMutableList() }

    fun <T> List<List<T>>.groupSymbols(filter: (T) -> Boolean = { false }): MutableMap<T, MutableList<GridNavigator>> {
        val lookup = mutableMapOf<T, MutableList<GridNavigator>>()
        val input = this
        for (y in 0..input.lastIndex) {
            for (x in 0..input[0].lastIndex) {
                val key = input[y][x]
                if (!filter(key)) continue
                val value = GridNavigator(x, y)
                lookup.computeIfAbsent(key) { mutableListOf() }.add(value)
            }
        }
        return lookup
    }
}