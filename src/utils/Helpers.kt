package utils

import utils.Input.submit

object Helpers {
    fun Any.println(level: Int? = null) = if (level != null) submit(level) else println(this)

    fun <T> List<List<T>>.clone() = this.map { line -> line.map { it }.toMutableList() }

    fun <T> List<List<T>>.surround(symbol: T): List<List<T>> {
        val top = listOf(List(this[0].size + 2) { symbol })
        val middle = map { line -> listOf(symbol) + line + listOf(symbol) }
        val bottom = listOf(List(this[0].size + 2) { symbol })
        return top + middle + bottom
    }

    fun <T> List<List<T>>.findExact(filter: (T) -> Boolean): GridNavigator? {
        val maxY = this.lastIndex
        val maxX = this[0].lastIndex
        for (y in 0..maxY)
            for (x in 0..maxX)
                if (filter(this[y][x])) return GridNavigator(x, y)
        return null
    }

    fun <T> List<List<T>>.findMatches(filter: (T) -> Boolean): List<GridNavigator> {
        val result = mutableListOf<GridNavigator>()
        val maxY = this.lastIndex
        val maxX = this[0].lastIndex
        for (y in 0..maxY)
            for (x in 0..maxX)
                if (filter(this[y][x]))
                    result.add(GridNavigator(x, y))
        return result
    }

    fun <T> List<List<T>>.groupMatches(filter: (T) -> Boolean = { false }): Map<T, List<GridNavigator>> {
        val result = mutableMapOf<T, MutableList<GridNavigator>>()
        val maxY = this.lastIndex
        val maxX = this[0].lastIndex
        for (y in 0..maxY)
            for (x in 0..maxX)
                if (filter(this[y][x]))
                    result.computeIfAbsent(this[y][x]) { mutableListOf() }.add(GridNavigator(x, y))
        return result
    }

    class Jump {
        val distances = Array(Direction.entries.size) { GridNavigator(-1, -1) }
        fun move(navigator: GridNavigator) {
            val moves = get(navigator.direction)
            navigator.x = moves.x
            navigator.y = moves.y
        }

        fun get(direction: Direction) = distances[direction.ordinal]
    }

    fun <T> List<List<T>>.createJumpMap(
        isBlocker: (T) -> Boolean,
        dirs: (GridNavigator) -> List<GridNavigator> = { it.allDirs() },
    ): List<List<Jump>> {
        val result = this.map { line -> line.map { Jump() } }
        val startPoints = this.findMatches(isBlocker)
        startPoints.forEach { blocker ->
            dirs(blocker).forEach { start ->
                val dir = start.clone()
                val backDir = dir.direction.turnAround().ordinal
                while (dir.isInBound(this) && !isBlocker(dir.valueOf(this))) {
                    val jump = dir.valueOf(result)
                    jump.distances[backDir] = start
                    dir.moveForward()
                }
            }
        }
        return result
    }

}