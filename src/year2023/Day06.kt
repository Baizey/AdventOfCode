package year2023

import utils.*

fun main() {
    data class Race(val time: Long, val distance: Long)

    val lines = Input.get(2023, 6).asLines()

    fun part1() {
        val times = lines.first().split(Regex(" +")).stream().skip(1).map { it.toLong() }.toList()
        val distances = lines.last().split(Regex(" +")).stream().skip(1).map { it.toLong() }.toList()
        val races = times.zip(distances) { time, distance -> Race(time, distance) }
        var result = 1L
        races.forEach { race ->
            var chances = 0L
            for (speed in 1..<race.time) {
                val distance = speed * (race.time - speed)
                if (distance > race.distance) chances++
            }
            result *= chances
        }
        println(result)
    }
    part1()

    fun part2(){
        val time = lines.first().split(":").last().replace(" ", "").toLong()
        val distance = lines.last().split(":").last().replace(" ", "").toLong()
        val race = Race(time, distance)
        var chances = 0L
        for (speed in 1..<race.time) {
            val distance = speed * (race.time - speed)
            if (distance > race.distance) chances++
        }
        println(chances)
    }
    part2()

}