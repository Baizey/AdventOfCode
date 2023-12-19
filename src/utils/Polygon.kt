package utils

import utils.Direction.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Polygon(points: List<GridNavigator>) {
    data class Side(var first: GridNavigator, var second: GridNavigator) {
        fun clone(): Side = Side(first.clone(), second.clone())

        fun direction() =
            if (first.x != second.x && first.y != second.y) throw Error()
            else if (first.x == second.x && first.y == second.y) throw Error()
            else if (first.y < second.y) south
            else if (first.y > second.y) north
            else if (first.x < second.x) east
            else west

        fun length() = abs(first.x - second.x) + abs(first.y - second.y) + 1L
    }

    val sides: List<Side>
    val corners: List<GridNavigator>

    init {
        corners = points.map(GridNavigator::clone)
        sides = corners.mapIndexed { i, start ->
            val end = if (i == corners.lastIndex) corners[0] else corners[i + 1]
            Side(start, end)
        }


        val minX = corners.minOf { it.x }
        val leftMost = sides.first { it.first.x == minX && it.second.x == minX }

        val isAntiClockwise = leftMost.first.y < leftMost.second.y
        if (isAntiClockwise) sides.forEach {
            val t = it.first
            it.first = it.second
            it.second = t
        }
    }

    fun border(): Long = sides.sumOf { it.length() - 1L }

    fun area(): Long {
        var sum = 0L
        var temps = sides.map(Side::clone).toMutableList()
        while (temps.size >= 4) {
            var smallest = Long.MAX_VALUE
            var index = -1
            for (i in temps.indices) {
                val side1 = temps[(i + 1) % temps.size]
                val flat = temps[(i + 2) % temps.size]
                val side2 = temps[(i + 3) % temps.size]

                val side1Dir = side1.direction()
                val side2Dir = side2.direction()
                val flatDir = flat.direction()

                if (side1Dir.turnRight() != flatDir) continue
                if (flatDir.turnRight() != side2Dir) continue

                if (flat.length() < smallest){
                    smallest = flat.length()
                    index = i
                }
            }

            val i = index
            val side1 = temps[(i + 1) % temps.size]
            val flat = temps[(i + 2) % temps.size]
            val side2 = temps[(i + 3) % temps.size]

            val side1Dir = side1.direction()
            val side2Dir = side2.direction()

            val conn1 = temps[i]
            val conn2 = temps[(i + 4) % temps.size]

            if (conn1 == conn2) {
                sum += (flat.length() - 2) * (side1.length() - 2)
                temps = mutableListOf()
            } else {
                val side1Length = side1.length()
                val side2Length = side2.length()

                sum += (min(side1.length(), side2.length()) - 1) * (flat.length() - 2)

                // Handle that some of the clean area removed may actually be walled
                if (conn1.direction().turnRight() == side1Dir && side1Length <= side2Length) sum -= (conn1.length() - 1)
                else if (side2Dir.turnRight() == conn2.direction() && side2Length <= side1Length) sum -= (conn2.length() - 1)

                // Restructure how our sides look after removing the cube
                if (side1Length == side2Length) {
                    conn1.second = conn2.second
                    temps.removeIf { it == conn2 || it == side1 || it == side2 || it == flat }
                } else if (side1Length > side2Length) {
                    side1.second = side1.first.clone().move(side1Dir, side1Length - side2Length)
                    conn2.first = side1.second
                    temps.removeIf { it == side2 || it == flat }
                } else {
                    side2.first = side2.first.clone().move(side2Dir, side1Length - 1)
                    conn1.second = side2.first
                    temps.removeIf { it == side1 || it == flat }
                }
            }
        }
        return sum
    }

}