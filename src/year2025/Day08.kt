package year2025

import utils.*

fun main() {
    val input = Input.get(2025, 8).asLongLines()

    fun distance(a: List<Long>, b: List<Long>): Long {
        val dx = b[0] - a[0]
        val dy = b[1] - a[1]
        val dz = b[2] - a[2]
        return dx * dx + dy * dy + dz * dz
    }

    data class Dist(val a: Int, val b: Int, val dist: Long)

    val list = mutableListOf<Dist>()
    for (i in 0..<input.size)
        for (j in i + 1..<input.size)
            list.add(Dist(i, j, distance(input[i], input[j])))
    val sortedList = list.sortedBy { it.dist }

    fun part1(): Any {
        val clusters = mutableListOf<MutableSet<Int>>()
        var at = 0
        while (at < 1000) {
            val dist = sortedList[at]
            val aCluster = clusters.firstOrNull { it.contains(dist.a) }
            val bCluster = clusters.firstOrNull { it.contains(dist.b) }
            if (aCluster != null) clusters.remove(aCluster)
            if (bCluster != null) clusters.remove(bCluster)
            val cluster = aCluster ?: mutableSetOf(dist.a)
            cluster.add(dist.b)
            bCluster?.let { cluster.addAll(it) }
            clusters.add(cluster)
            at++
        }
        val clustersBySize = clusters.sortedBy { -it.size }
        return clustersBySize[0].size * clustersBySize[1].size * clustersBySize[2].size
    }

    fun part2(): Any {
        val list = mutableListOf<Dist>()
        for (i in 0..<input.size)
            for (j in i + 1..<input.size)
                list.add(Dist(i, j, distance(input[i], input[j])))
        val sortedList = list.sortedBy { it.dist }
        val clusters = mutableListOf<MutableSet<Int>>()
        var at = 0
        while (true) {
            val dist = sortedList[at]
            val aCluster = clusters.firstOrNull { it.contains(dist.a) }
            val bCluster = clusters.firstOrNull { it.contains(dist.b) }
            if (aCluster != null) clusters.remove(aCluster)
            if (bCluster != null) clusters.remove(bCluster)
            val cluster = aCluster ?: mutableSetOf(dist.a)
            cluster.add(dist.b)
            bCluster?.let { cluster.addAll(it) }
            clusters.add(cluster)

            at++
            if (clusters[0].size == input.size) {
                return input[dist.a][0] * input[dist.b][0]
            }
        }
    }

    println("Part 1: " + part1())
    println("Part 2: " + part2())
}