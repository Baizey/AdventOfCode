package utils.grid

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