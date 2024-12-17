package utils.grid

@Suppress("EnumEntryName")
enum class Direction(val deltaX: Int, val deltaY: Int) {
    none(0, 0),
    north(0, -1),
    northeast(+1, -1),
    east(+1, 0),
    southeast(+1, +1),
    south(0, +1),
    southwest(-1, +1),
    west(-1, 0),
    northwest(-1, -1);

    companion object {
        val xyDirs = listOf(north, east, south, west)
        val diagonalDirs = listOf(northeast, southeast, southwest, northwest)
        val allDirs = listOf(north, northeast, east, southeast, south, southwest, west, northwest)
    }

    fun turnAround() = when (this) {
        north -> south
        west -> east
        south -> north
        east -> west
        northeast -> southwest
        northwest -> southeast
        southeast -> northwest
        southwest -> northeast
        none -> none
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
        none -> none
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
        none -> none
    }

    operator fun times(value: Int): Nav = Nav(deltaX * value, deltaY * value, this)
    operator fun times(value: Long): Nav = Nav(deltaX * value, deltaY * value, this)

    operator fun minus(value: Direction): Direction {
        val newX = (deltaX - value.deltaX).coerceIn(-1, 1)
        val newY = (deltaY - value.deltaY).coerceIn(-1, 1)
        return entries.first { it.deltaX == newX && it.deltaY == newY }
    }

    operator fun plus(value: Direction): Direction {
        val newX = (deltaX + value.deltaX).coerceIn(-1, 1)
        val newY = (deltaY + value.deltaY).coerceIn(-1, 1)
        return entries.first { it.deltaX == newX && it.deltaY == newY }
    }
}