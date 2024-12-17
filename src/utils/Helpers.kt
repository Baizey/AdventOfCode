package utils

import utils.grid.Direction.unknown
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

    class Jump {
        val distances = Array(Direction.entries.size) { Nav(-1, -1) }
        fun move(navigator: Nav): Nav {
            val moves = get(navigator.dir)
            navigator.x = moves.x
            navigator.y = moves.y
            return navigator
        }

        fun get(direction: Direction) =
            if (direction == unknown) throw Error("Unknown direction")
            else distances[direction.ordinal]
    }

    fun <T> List<List<T>>.createJumpMap(
        isBlocker: (T) -> Boolean,
        dirs: (Nav) -> List<Nav> = { it.allDirs() },
    ): List<List<Jump>> {
        val result = this.map { line -> line.map { Jump() } }
        val startPoints = this.findMatches(isBlocker)
        startPoints.forEach { blocker ->
            dirs(blocker).forEach { start ->
                val dir = start.clone()
                val backDir = dir.dir.turnAround().ordinal
                while (dir.isInBound(this) && !isBlocker(dir.value(this))) {
                    val jump = dir.value(result)
                    jump.distances[backDir] = start
                    dir.moveForward()
                }
            }
        }
        return result
    }

}