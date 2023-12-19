package utils

import utils.Direction.*

@Suppress("EnumEntryName")
enum class Direction {
    unknown,
    north,
    south,
    west,
    east;

    fun turnAround() = when (this) {
        north -> south
        west -> east
        south -> north
        east -> west
        unknown -> throw Error("Unknown direction")
    }

    fun turnLeft() = when (this) {
        north -> west
        west -> south
        south -> east
        east -> north
        unknown -> throw Error("Unknown direction")
    }

    fun turnRight() = when (this) {
        north -> east
        east -> south
        south -> west
        west -> north
        unknown -> throw Error("Unknown direction")
    }
}

data class GridNavigator(var x: Long, var y: Long, var direction: Direction = unknown) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    constructor(x: Int, y: Int, direction: Direction) : this(x.toLong(), y.toLong(), direction)

    fun moveForward() = move(direction, 1L)
    fun fourDir(): List<GridNavigator> = listOf(
        clone().move(north, 1L),
        clone().move(east, 1L),
        clone().move(south, 1L),
        clone().move(west, 1L),
    )

    fun eightDir(): List<GridNavigator> = listOf(
        clone().move(north, 1L),
        clone().move(north, 1L).move(east, 1L),
        clone().move(east, 1L),
        clone().move(east, 1L).move(south, 1L),
        clone().move(south, 1L),
        clone().move(south, 1L).move(west, 1L),
        clone().move(west, 1L),
        clone().move(west, 1L).move(north, 1L)
    )

    fun turnAround() = turn(direction.turnAround())

    fun turnLeft() = turn(direction.turnLeft())

    fun turnRight() = turn(direction.turnRight())

    fun move(dir: Direction, amount: Long): GridNavigator {
        when (dir) {
            north -> y -= amount
            south -> y += amount
            west -> x -= amount
            east -> x += amount
            unknown -> throw Error("Unknown direction")
        }
        return this
    }

    fun isInBound(maxX: Int, maxY: Int) = x in 0..<maxX && y in 0..<maxY
    fun isInBound(maxX: Long, maxY: Long) = x in 0..<maxX && y in 0..<maxY
    fun turn(dir: Direction): GridNavigator {
        this.direction = dir
        return this
    }

    fun copy() = clone()

    fun clone() = GridNavigator(x, y, direction)

    override fun equals(other: Any?): Boolean {
        if (other !is GridNavigator) return false
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + direction.hashCode()
        return result.toInt()
    }

}