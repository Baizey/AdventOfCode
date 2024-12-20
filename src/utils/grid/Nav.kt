package utils.grid

import utils.grid.Direction.none

class Nav(var x: Long, var y: Long, var dir: Direction = none) {
    constructor(x: Int, y: Int, direction: Direction = none) : this(x.toLong(), y.toLong(), direction)

    fun xyDirs(stepsAway: Long = 1L): List<Nav> {
        return if (stepsAway == 1L)
            Direction.xyDirs.map { this + it }
        else {
            val r = mutableListOf<Nav>()
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

    fun diagonalDirs(): List<Nav> = Direction.diagonalDirs.map { this + it }
    fun allDirs(): List<Nav> = Direction.allDirs.map { this + it }

    fun moveLeft(steps: Long = 1L) = move(dir.turnLeft(), steps)
    fun moveRight(steps: Long = 1L) = move(dir.turnRight(), steps)
    fun moveBackward(steps: Long = 1L) = move(dir.turnAround(), steps)
    fun moveForward(steps: Long = 1L) = move(dir, steps)
    fun move(inputDir: Direction, amount: Long = 1L): Nav {
        x += inputDir.deltaX * amount
        y += inputDir.deltaY * amount
        return this
    }

    fun turnAround() = turn(dir.turnAround())
    fun turnLeft() = turn(dir.turnLeft())
    fun turnRight() = turn(dir.turnRight())
    fun turn(inputDir: Direction): Nav {
        this.dir = inputDir
        return this
    }

    fun <T> value(grid: List<List<T>>, inputDir: Direction = dir, steps: Long = 0L): T = valueOrNull(grid, inputDir, steps)!!
    fun <T> valueOrNull(grid: List<List<T>>, inputDir: Direction = dir, steps: Long = 0L): T? {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        return if (isInBound(grid, dx, dy)) grid[dy.toInt()][dx.toInt()] else null
    }

    fun <T> setValue(c: T, grid: List<MutableList<T>>, inputDir: Direction = dir, steps: Long = 0L): Nav {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        grid[dy.toInt()][dx.toInt()] = c
        return this
    }

    fun <T> isInBound(grid: List<List<T>>, atX: Long = x, atY: Long = y): Boolean = isInBound(0L, grid[0].lastIndex.toLong(), 0L, grid.lastIndex.toLong(), atX, atY)
    fun <T> isNotInBound(grid: List<List<T>>, atX: Long = x, atY: Long = y): Boolean = !isInBound(grid, atX, atY)

    fun clone(inputDir: Direction = dir, steps: Long = 0L): Nav {
        val dx = x + inputDir.deltaX * steps
        val dy = y + inputDir.deltaY * steps
        return Nav(dx, dy, inputDir)
    }

    override fun hashCode(): Int = x.toInt().shl(16) + y.toInt()
    override fun equals(other: Any?) = other is Nav && hashWithDir() == other.hashWithDir()

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
    }

    override fun toString(): String = "($x, $y) $dir"

    operator fun component1(): Long = x
    operator fun component2(): Long = y
    operator fun component3(): Direction = dir

    operator fun times(value: Int): Nav = Nav(x * value, y * value, dir)
    operator fun times(value: Long): Nav = Nav(x * value, y * value, dir)

    operator fun minus(value: Int): Nav = Nav(x - value, y - value, dir)
    operator fun minus(value: Long): Nav = Nav(x - value, y - value, dir)
    operator fun minus(value: Direction): Nav = Nav(x - value.deltaX, y - value.deltaY, value)
    operator fun minus(value: Nav): Nav = Nav(x - value.x, y - value.y)

    operator fun plus(value: Int): Nav = Nav(x + value, y + value, dir)
    operator fun plus(value: Long): Nav = Nav(x + value, y + value, dir)
    operator fun plus(value: Direction): Nav = Nav(x + value.deltaX, y + value.deltaY, value)
    operator fun plus(value: Nav): Nav = Nav(x + value.x, y + value.y)
}