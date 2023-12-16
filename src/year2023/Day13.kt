package year2023

import utils.*

fun main() {
    data class Graph(val lines: List<BooleanArray>) {
        val height = lines.indices
        val width = lines[0].indices
    }

    val rawInput = Input.get(2023, 13).map { line -> line.map { it == '#' }.toBooleanArray() }
    val graphs = mutableListOf<Graph>()
    var from = 0
    for (i in rawInput.indices) {
        if (rawInput[i].isEmpty()) {
            graphs.add(Graph(rawInput.subList(from, i)))
            from = i + 1
        }
    }
    graphs.add(Graph(rawInput.subList(from, rawInput.size)))

    fun reflectsRow(graph: Graph, row: Int, exactDiffs: Int): Boolean {
        val lines = graph.lines
        var top = row
        var bottom = row + 1
        var diffs = 0
        while (top >= 0 && bottom < lines.size) {
            for (i in graph.width) {
                if (lines[top][i] != lines[bottom][i]) diffs++
            }
            if (diffs > exactDiffs) return false
            top--
            bottom++
        }
        return diffs == exactDiffs
    }

    fun reflectsColumn(graph: Graph, column: Int, exactDiffs: Int): Boolean {
        val lines = graph.lines
        var left = column
        var right = column + 1
        var diffs = 0
        while (left >= 0 && right < lines[0].size) {
            for (line in lines) if (line[left] != line[right]) diffs++
            if (diffs > exactDiffs) return false
            left--
            right++
        }
        return diffs == exactDiffs
    }

    fun part1() {
        println(graphs.map { graph ->
            for (row in 0..<graph.lines.lastIndex)
                if (reflectsRow(graph, row, 0))
                    return@map (row + 1L) * 100L
            for (column in 0..<graph.lines[0].lastIndex)
                if (reflectsColumn(graph, column, 0))
                    return@map (column + 1L)
            throw Error(graph.lines.joinToString(separator = "\n"))
        }.sum())
    }

    fun part2() {
        println(graphs.map { graph ->
            for (row in 0..<graph.lines.lastIndex)
                if (reflectsRow(graph, row, 1))
                    return@map (row + 1L) * 100L
            for (column in 0..<graph.lines[0].lastIndex)
                if (reflectsColumn(graph, column, 1))
                    return@map (column + 1L)
            throw Error(graph.lines.joinToString(separator = "\n"))
        }.sum())
    }
    part1()
    part2()
}