package utils

import utils.Direction.*
import utils.Helpers.Jump

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

class GridNavigator(
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

    fun moveBackward() = turnAround().move(direction, 1L).turnAround()
    fun moveForward() = move(direction, 1L)

    fun move(jumpGrid: List<List<Jump>>): GridNavigator = valueOf(jumpGrid).move(this)
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
        north ->
            isInBound(0, grid[0].size, 1, grid.size)

        northeast ->
            isInBound(0, grid[0].size - 1, 1, grid.size)

        east ->
            isInBound(0, grid[0].size - 1, 0, grid.size)

        southeast ->
            isInBound(0, grid[0].size - 1, 0, grid.size - 1)

        south ->
            isInBound(0, grid[0].size, 0, grid.size - 1)

        southwest ->
            isInBound(1, grid[0].size, 0, grid.size - 1)

        west ->
            isInBound(1, grid[0].size, 0, grid.size)

        northwest ->
            isInBound(1, grid[0].size, 1, grid.size)
    }

    fun <T> isNotInBound(grid: List<List<T>>) = !isInBound(grid)
    fun <T> isInBound(grid: List<List<T>>) = isInBound(0, grid[0].size, 0, grid.size)

    fun isNotInBound(minX: Int, maxX: Int, minY: Int, maxY: Int) = !isInBound(minX, maxX, minY, maxY)
    fun isNotInBound(minX: Long, maxX: Long, minY: Long, maxY: Long) = !isInBound(minX, maxX, minY, maxY)
    fun isInBound(minX: Int, maxX: Int, minY: Int, maxY: Int) = x in minX..<maxX && y in minY..<maxY
    fun isInBound(minX: Long, maxX: Long, minY: Long, maxY: Long) = x in minX..<maxX && y in minY..<maxY

    fun <T> valueOf(grid: List<List<T>>): T = valueOrNullOf(grid)!!
    fun <T> valueOrNullOf(grid: List<List<T>>): T? = if (isInBound(grid)) grid[y.toInt()][x.toInt()] else null
    fun <T> setValue(c: T, grid: List<MutableList<T>>): GridNavigator {
        grid[y.toInt()][x.toInt()] = c
        return this
    }

    override fun hashCode(): Int = x.toInt().shl(16) + y.toInt()
    override fun equals(other: Any?) = other is GridNavigator && sameWithDir(other)
    fun sameLocation(other: GridNavigator) = x == other.x && y == other.y
    fun sameWithDir(other: GridNavigator) = sameLocation(other) && direction == other.direction

    fun copy() = clone()
    fun clone() = GridNavigator(x, y, direction)

    /**
     * Limited to 32-bit integers
     */
    fun hash(): Long = x.shl(32) + y

    /**
     * Limited to 28-bit integers
     */
    fun hashWithDir(): Long = x.shl(34) + y.shl(4) + direction.ordinal
}