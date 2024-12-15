package utils.grid

class GridCube<T>(
    val at: GridNavigator,
    val movable: Boolean,
    val representation: List<List<T>>,
) {
    val ySize = representation.size
    val xSize = representation[0].size

    fun move(
        direction: Direction = at.direction,
        distance: Long = 1L,
    ) {
        at.move(direction, distance)
    }

    fun set(v: T, grid: List<MutableList<T>>) {
        val t = at.clone()
        for (y in 0..<ySize)
            for (x in 0..<xSize) {
                t.x = at.x + x
                t.y = at.y + y
                t.setValue(v, grid)
            }
    }

    fun show(grid: List<MutableList<T>>) {
        val t = at.clone()
        t.x--
        t.y--
        (0..<ySize).forEach { y ->
            t.y++
            (0..<xSize).forEach { x ->
                t.x++
                t.setValue(representation[y][x], grid)
            }
        }
    }

}