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

data class GridNavigator(var x: Int, var y: Int, var direction: Direction = unknown) {
    fun moveForward() = move(direction)

    fun fourDir(maxX: Int, maxY: Int): List<GridNavigator> = listOf(
        clone().move(north),
        clone().move(east),
        clone().move(south),
        clone().move(west),
    ).filter { it.isInBound(maxX, maxY) }

    fun eightDir(maxX: Int, maxY: Int): List<GridNavigator> = listOf(
        clone().move(north),
        clone().move(north).move(east),
        clone().move(east),
        clone().move(east).move(south),
        clone().move(south),
        clone().move(south).move(west),
        clone().move(west),
        clone().move(west).move(north)
    ).filter { it.isInBound(maxX, maxY) }

    fun turnAround() = turn(direction.turnAround())

    fun turnLeft() = turn(direction.turnLeft())

    fun turnRight() = turn(direction.turnRight())

    fun move(dir: Direction): GridNavigator {
        when (dir) {
            north -> y--
            south -> y++
            west -> x--
            east -> x++
            unknown -> throw Error("Unknown direction")
        }
        return this
    }

    fun isInBound(maxX: Int, maxY: Int) = x in 0..<maxX && y in 0..<maxY
    fun turn(dir: Direction): GridNavigator {
        this.direction = dir
        return this
    }

    fun clone() = GridNavigator(x, y, direction)

    override fun equals(other: Any?): Boolean {
        if (other !is GridNavigator) return false
        return other.x == x && other.y == y && (direction == other.direction.turnAround() || direction == other.direction)
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + direction.hashCode()
        return result
    }

}