package year2024

import utils.*
import utils.Helpers.println

fun main() {
    val edges = Input.get(2024, 23).asWordLines()
        .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }
        .groupBy { it.first }.values
        .associate { it.first().first to it.map { it.second }.toSet() }

    fun String.edges() = edges[this]!!

    fun part1() {
        edges.keys
            .asSequence()
            .filter { it.startsWith("t") }
            .flatMap { first ->
                val seconds = first.edges()
                seconds.mapNotNull { second ->
                    val otherConnects = second.edges()
                    val thirds = seconds.intersect(otherConnects)
                    if (thirds.isEmpty()) null else thirds.map { third -> listOf(first, second, third) }
                }.flatten()
            }
            .map { it.sorted().joinToString(separator = ",") }
            .distinct()
            .count()
            .println()
    }

    fun part2() {
        var biggest = setOf<String>()
        var cliques = edges.keys.flatMap { first -> first.edges().map { second -> setOf(first, second) } }
            .distinctBy { it.sorted().joinToString(separator = ",") }
        while (cliques.isNotEmpty()) {
            biggest = cliques.first().toSet()
            cliques = cliques
                .map { clique ->
                    val newCandidates = clique.first().edges().filter { !clique.contains(it) }
                    newCandidates.filter { n -> clique.all { n.edges().contains(it) } }.map { clique + setOf(it) }
                }
                .flatten()
                .distinctBy { it.sorted().joinToString(separator = ",") }
        }
        biggest.sorted().joinToString(separator = ",").println()
    }
    part1()
    part2()
}