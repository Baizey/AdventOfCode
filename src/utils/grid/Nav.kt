package utils.grid

import utils.grid.Direction.unknown

class Nav(var x: Long, var y: Long, var dir: Direction = unknown) {
    constructor(x: Int, y: Int, direction: Direction = unknown) : this(x.toLong(), y.toLong(), direction)

    fun xyDirs(): List<Nav> = Direction.xyDirs.map { this + it }
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

    fun <T> isInBound(grid: List<List<T>>, atX: Long = x, atY: Long = y): Boolean = isInBound(0L, grid[0].size.toLong(), 0L, grid.size.toLong(), atX, atY)
    fun <T> isNotInBound(grid: List<List<T>>, atX: Long = x, atY: Long = y): Boolean = !isInBound(grid, atX, atY)

    fun clone() = Nav(x, y, dir)
    fun cloneForward(steps: Long = 1L) = clone().moveForward(steps)
    fun cloneBackward(steps: Long = 1L) = clone().moveBackward(steps)
    fun cloneLeft(steps: Long = 1L) = clone().moveLeft(steps)
    fun cloneRight(steps: Long = 1L) = clone().moveRight(steps)
    override fun hashCode(): Int = x.toInt().shl(16) + y.toInt()
    override fun equals(other: Any?) = other is Nav && hashWithDir() == other.hashWithDir()

    /**
     * Limited to 32-bit integers
     */
    fun hash(): Long = x.shl(32) + y

    /**
     * Limited to 30-bit integers
     */
    fun hashWithDir(): Long = x.shl(34) + y.shl(4) + dir.ordinal

    companion object {
        fun isInBound(minX: Long, maxX: Long, minY: Long, maxY: Long, atX: Long, atY: Long): Boolean = atX in minX..<maxX && atY in minY..<maxY
    }

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