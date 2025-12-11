package utils.grid

open class NavBound<T>(
    var x: Long,
    var y: Long,
    val grid: List<MutableList<T>>,
    var dir: Direction = Direction.none,
) {
    constructor(
        x: Int, y: Int,
        grid: List<MutableList<T>>,
        direction: Direction = Direction.none,
    ) : this(x.toLong(), y.toLong(), grid, direction)

    open fun xyDirs(stepsAway: Long = 1L): List<NavBound<T>> {
        return if (stepsAway == 1L)
            Direction.xyDirs.map { this + it }
        else {
            val r = mutableListOf<NavBound<T>>()
            Direction.xyDirs.forEach { dir ->
                for (i in 1L..stepsAway) {
                    val tmp = clone().turn(dir).moveForward(i)
                    r.add(tmp)
                    for (j in stepsAway - i downTo 1L) {
                        r.add(tmp.clone().turnRight().moveForward(j))
                    }
                }
            }
            r
        }
    }

    fun diagonalDirs(): List<NavBound<T>> = Direction.diagonalDirs.map { this + it }
    fun allDirs(): List<NavBound<T>> = Direction.allDirs.map { this + it }

    fun moveLeft(steps: Long = 1L) = move(dir.turnLeft(), steps)
    fun moveRight(steps: Long = 1L) = move(dir.turnRight(), steps)
    fun moveBackward(steps: Long = 1L) = move(dir.turnAround(), steps)
    fun moveForward(steps: Long = 1L) = move(dir, steps)
    fun move(inputDir: Direction, amount: Long = 1L): NavBound<T> {
        x += inputDir.deltaX * amount
        y += inputDir.deltaY * amount
        return this
    }

    fun turnAround() = turn(dir.turnAround())
    fun turnLeft() = turn(dir.turnLeft())
    fun turnRight() = turn(dir.turnRight())
    fun turn(inputDir: Direction): NavBound<T> {
        this.dir = inputDir
        return this
    }

    fun value(inputDir: Direction = dir, steps: Long = 0L): T = valueOrNull(inputDir, steps)!!
    fun valueOrNull(inputDir: Direction = dir, steps: Long = 0L): T? {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        return if (isInBound(dx, dy)) grid[dy.toInt()][dx.toInt()] else null
    }

    fun setValue(c: T, inputDir: Direction = dir, steps: Long = 0L): NavBound<T> {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        grid[dy.toInt()][dx.toInt()] = c
        return this
    }

    fun isInBound(atX: Long = x, atY: Long = y): Boolean = isInBound(0L, grid[0].lastIndex.toLong(), 0L, grid.lastIndex.toLong(), atX, atY)
    fun isNotInBound(atX: Long = x, atY: Long = y): Boolean = !isInBound(atX, atY)

    fun clone(inputDir: Direction = dir, steps: Long = 0L): NavBound<T> {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        return NavBound(dx, dy, grid, inputDir)
    }

    override fun hashCode(): Int = x.toInt().shl(16) + y.toInt()
    override fun equals(other: Any?) = other is NavBound<*> && hashWithDir() == other.hashWithDir()

    fun xyIntersects(other: NavBound<T>): List<NavBound<T>> = listOf(NavBound(other.x, y, grid), NavBound(x, other.y, grid))

    /**
     * Limited to 32-bit integers
     */
    fun hash(inputDir: Direction = dir, steps: Long = 0L): Long {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        return dx.shl(32) + dy
    }

    /**
     * Limited to 30-bit integers
     */
    fun hashWithDir(inputDir: Direction = dir, steps: Long = 0L): Long {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        return dx.shl(34) + dy.shl(4) + inputDir.ordinal
    }

    companion object {
        fun isInBound(minX: Long, maxX: Long, minY: Long, maxY: Long, atX: Long, atY: Long): Boolean = atX in minX..maxX && atY in minY..maxY

        fun <T> each(grid: List<MutableList<T>>): Sequence<NavBound<T>> =
            grid.asSequence().flatMapIndexed { y, row -> row.indices.map { x -> NavBound(x.toLong(), y.toLong(), grid) } }

        fun <T> List<MutableList<T>>.navBoundEach(): Sequence<NavBound<T>> = each(this)
        fun <T> find(needle: T, grid: List<MutableList<T>>): NavBound<T> = each(grid).first { it.value() == needle }
    }

    override fun toString(): String = "($x, $y) $dir"

    operator fun component1(): Long = x
    operator fun component2(): Long = y
    operator fun component3(): Direction = dir

    operator fun times(value: Int): NavBound<T> = NavBound(x * value, y * value, grid, dir)
    operator fun times(value: Long): NavBound<T> = NavBound(x * value, y * value, grid, dir)

    operator fun minus(value: Int): NavBound<T> = NavBound(x - value, y - value, grid, dir)
    operator fun minus(value: Long): NavBound<T> = NavBound(x - value, y - value, grid, dir)
    operator fun minus(value: Direction): NavBound<T> = NavBound(x - value.deltaX, y - value.deltaY, grid, value)
    operator fun minus(value: NavBound<T>): NavBound<T> = NavBound(x - value.x, y - value.y, grid)

    operator fun plus(value: Int): NavBound<T> = NavBound(x + value, y + value, grid, dir)
    operator fun plus(value: Long): NavBound<T> = NavBound(x + value, y + value, grid, dir)
    operator fun plus(value: Direction): NavBound<T> = NavBound(x + value.deltaX, y + value.deltaY, grid, value)
    operator fun plus(value: NavBound<T>): NavBound<T> = NavBound(x + value.x, y + value.y, grid)
}