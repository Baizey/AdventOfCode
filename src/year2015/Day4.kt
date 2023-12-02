package year2015

import utils.Input
import java.security.MessageDigest

fun main() {
    val hashKey = Input.get(2015, 4).first()

    fun part1() {
        for (i in 1..Int.MAX_VALUE) {
            val key = hashKey + i
            val digest = MessageDigest.getInstance("MD5")
            val hash: ByteArray = digest.digest(key.toByteArray(charset("UTF-8")))
            val sb = StringBuilder(2 * hash.size)
            for (b in hash) sb.append(String.format("%02x", b.toInt() and 0xff))
            val result = sb.toString()

            if (result.startsWith("0".repeat(5))) {
                println("$i $hash")
                return
            }

        }
    }
    fun part2() {
        for (i in 1..Int.MAX_VALUE) {
            val key = hashKey + i
            val digest = MessageDigest.getInstance("MD5")
            val hash: ByteArray = digest.digest(key.toByteArray(charset("UTF-8")))
            val sb = StringBuilder(2 * hash.size)
            for (b in hash) sb.append(String.format("%02x", b.toInt() and 0xff))
            val result = sb.toString()

            if (result.startsWith("0".repeat(6))) {
                println("$i $hash")
                return
            }
        }
    }

    part1()
    part2()
}