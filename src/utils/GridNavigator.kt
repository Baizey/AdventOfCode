package utils

import utils.Direction.*

@Suppress("EnumEntryName")
enum class Direction {
    unknown,
    north,
    south,
    west,
    east,
    northeast,
    northwest,
    southeast,
    southwest;

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

data class GridNavigator(var x: Long, var y: Long, var direction: Direction = unknown) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    constructor(x: Int, y: Int, direction: Direction) : this(x.toLong(), y.toLong(), direction)

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

    fun isNotInBound(maxX: Int, maxY: Int) = !isInBound(maxX, maxY)
    fun isNotInBound(maxX: Long, maxY: Long) = !isInBound(maxX, maxY)
    fun isInBound(maxX: Int, maxY: Int) = x in 0..<maxX && y in 0..<maxY
    fun isInBound(maxX: Long, maxY: Long) = x in 0..<maxX && y in 0..<maxY

    fun copy() = clone()

    fun clone() = GridNavigator(x, y, direction)

    override fun equals(other: Any?): Boolean {
        if (other !is GridNavigator) return false
        return other.x == x && other.y == y
    }

    fun valueOf(grid: List<IntArray>): Int = grid[y.toInt()][x.toInt()]
    fun valueOf(grid: Array<IntArray>): Int = grid[y.toInt()][x.toInt()]
    fun valueOf(grid: List<CharArray>): Char = grid[y.toInt()][x.toInt()]
    fun valueOf(grid: Array<CharArray>): Char = grid[y.toInt()][x.toInt()]
    fun <T> valueOf(grid: List<List<T>>): T = grid[y.toInt()][x.toInt()]

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + direction.hashCode()
        return result.toInt()
    }

}