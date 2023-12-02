fun main() {
    data class Play(val amount: Long, val color: String)
    data class Game(val id: Long, val plays: List<Play>)

    val lines = Input.day(2)
    val green = "green"
    val red = "red"
    val blue = "blue"

    fun parseLine(line: String): Game {
        val meta = line.split(":")
        val plays = meta[1].replace(';', ',').split(",").map { it.trim() }
        val playsList: List<Play> = plays.map { play ->
            val playMeta: List<String> = play.split(" ").map { it.trim() }
            Play(playMeta[0].toLong(), playMeta[1])
        }
        val id = meta[0].trim().split(" ").last().trim().toLong()
        return Game(id, playsList)
    }

    val games = lines.map { parseLine(it) }

    fun part1() {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        val result = games.filter { game ->
            game.plays.all {
                when (it.color) {
                    green -> it.amount <= maxGreen
                    red -> it.amount <= maxRed
                    blue -> it.amount <= maxBlue
                    else -> throw Exception()
                }
            }
        }.sumOf { it.id }
        println(result)
    }

    fun part2() {
        val result = games.sumOf { game ->
            val maxRed = game.plays.filter { it.color == red }.maxOfOrNull { it.amount } ?: 0
            val maxBlue = game.plays.filter { it.color == blue }.maxOfOrNull { it.amount } ?: 0
            val maxGreen = game.plays.filter { it.color == green }.maxOfOrNull { it.amount } ?: 0
            maxRed * maxBlue * maxGreen
        }
        println(result)
    }

    part1()
    part2()
}