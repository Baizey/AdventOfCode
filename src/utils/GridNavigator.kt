package utils

import utils.Direction.*

@Suppress("EnumEntryName")
enum class Direction {
    unknown,
    north,
    northeast,
    east,
    southeast,
    south,
    southwest,
    west,
    northwest;

    fun turnAround() = when (this) {
        north -> south
        west -> east
        south -> north
        east -> west
        northeast -> southwest
        northwest -> southeast
        southeast -> northwest
        southwest -> northeast
        unknown -> throw Error("Unknown direction")
    }

    fun turnLeft() = when (this) {
        north -> west
        west -> south
        south -> east
        east -> north
        northeast -> northwest
        northwest -> southwest
        southeast -> northeast
        southwest -> southeast
        unknown -> throw Error("Unknown direction")
    }

    fun turnRight() = when (this) {
        north -> east
        east -> south
        south -> west
        west -> north
        northeast -> southeast
        northwest -> northeast
        southeast -> southwest
        southwest -> northwest
        unknown -> throw Error("Unknown direction")
    }
}

data class GridNavigator(
    var x: Long,
    var y: Long,
    var direction: Direction = unknown,
) {
    constructor(x: Int, y: Int, direction: Direction = unknown) : this(x.toLong(), y.toLong(), direction)

    fun xyDirs(): List<GridNavigator> = listOf(
        clone().turn(north).moveForward(),
        clone().turn(east).moveForward(),
        clone().turn(south).moveForward(),
        clone().turn(west).moveForward(),
    )

    fun diagonalDirs(): List<GridNavigator> = listOf(
        clone().turn(northeast).moveForward(),
        clone().turn(southeast).moveForward(),
        clone().turn(southwest).moveForward(),
        clone().turn(northwest).moveForward(),
    )

    fun allDirs(): List<GridNavigator> = listOf(
        clone().turn(north).moveForward(),
        clone().turn(northeast).moveForward(),
        clone().turn(east).moveForward(),
        clone().turn(southeast).moveForward(),
        clone().turn(south).moveForward(),
        clone().turn(southwest).moveForward(),
        clone().turn(west).moveForward(),
        clone().turn(northwest).moveForward()
    )

    fun moveForward() = move(direction, 1L)

    fun move(dir: Direction, amount: Long): GridNavigator {
        when (dir) {
            north -> y -= amount
            south -> y += amount
            west -> x -= amount
            east -> x += amount
            northeast -> move(north, amount).move(east, amount)
            northwest -> move(north, amount).move(west, amount)
            southeast -> move(south, amount).move(east, amount)
            southwest -> move(south, amount).move(west, amount)
            unknown -> throw Error("Unknown direction")
        }
        return this
    }

    fun turnAround() = turn(direction.turnAround())

    fun turnLeft() = turn(direction.turnLeft())

    fun turnRight() = turn(direction.turnRight())

    fun turn(dir: Direction): GridNavigator {
        this.direction = dir
        return this
    }

    fun <T> isNextNotInBound(grid: List<List<T>>) = !isNextInBound(grid)
    fun <T> isNextInBound(grid: List<List<T>>) = when (direction) {
        unknown -> throw Error("Unknown direction")
        north -> isInBound(x, y - 1)
        northeast -> isInBound(x + 1, y - 1)
        east -> isInBound(x + 1, y)
        southeast -> isInBound(x + 1, y + 1)
        south -> isInBound(x, y + 1)
        southwest -> isInBound(x - 1, y + 1)
        west -> isInBound(x - 1, y)
        northwest -> isInBound(x - 1, y - 1)
    }

    fun <T> isNotInBound(grid: List<List<T>>) = !isInBound(grid)
    fun <T> isInBound(grid: List<List<T>>) = isInBound(grid[0].size, grid.size)

    private fun isNotInBound(maxX: Int, maxY: Int) = !isInBound(maxX, maxY)
    private fun isNotInBound(maxX: Long, maxY: Long) = !isInBound(maxX, maxY)
    private fun isInBound(maxX: Int, maxY: Int) = x in 0..<maxX && y in 0..<maxY
    private fun isInBound(maxX: Long, maxY: Long) = x in 0..<maxX && y in 0..<maxY

    fun <T> valueOf(grid: List<List<T>>): T = grid[y.toInt()][x.toInt()]
    fun <T> setValue(c: T, grid: List<MutableList<T>>): GridNavigator {
        grid[y.toInt()][x.toInt()] = c
        return this
    }

    fun copy() = clone()

    fun clone() = GridNavigator(x, y, direction)

    override fun equals(other: Any?): Boolean = if (other !is GridNavigator) false else other.x == x && other.y == y
    override fun hashCode(): Int = x.toInt().shl(16) + y.toInt()

    fun hash(): Long = x.shl(32) + y

    fun hashWithDirection(): Long = x.shl(34) + y.shl(4) + direction.ordinal
}