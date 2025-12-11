package leetcode.challenge_3463

import leetcode.challenge_3463.Solution.Companion.calculateBottomRow
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    println(calculateBottomRow(3, 3))
    val s = Solution()
    timeAction { s.hasSameDigits(Files.readString(Path.of("src/leetcode/challenge_3463/test_667.txt")).trim()) }
}

class Solution {
    fun hasSameDigits(s: String) = hasSameDigits(s.toCharArray().map { it.digitToInt() }.toTypedArray())

    companion object {

        fun hasSameDigits(arr: Array<Int>): Boolean {
            var size = arr.size
            while (size > 2) {
                for (i in 0 until size - 1)
                    arr[i] = (arr[i] + arr[i + 1]) % 10
                size--
            }
            return arr[0] == arr[1]
        }

        fun calculateBottomRow(n: Int, m: Int): List<Int> {
            return (0..m).map { binomialMod10(n + it, it) }
        }

        fun binomialMod10(n: Int, kk: Int): Int {
            var k = kk
            if (k == 0 || k == n) return 1
            if (k > n - k) k = n - k
            var result = 1
            for (i in 0..<k) {
                result = (result * (n - i)) % 10
                result = (result * modInverse(i + 1, 10)) % 10
            }
            return result
        }

        fun modInverse(a: Int, mod: Int): Int {
            for (x in 1..<mod) {
                if ((a * x) % mod == 1) {
                    return x
                }
            }
            throw Error()
        }
    }
}

fun timeAction(action: () -> Any) {
    val start = System.currentTimeMillis()
    println(action())
    val end = System.currentTimeMillis()
    println("Time taken: ${end - start}ms")
}

class SemVersion(val major: Int, val minor: Int, val patch: Int, val label: String?, val build: String?) {
    companion object {
        private val regex = Regex("(\\d+)(\\.\\d+)?(\\.\\d+)?(?:-(\\w+))?(?:\\+(\\w+))?")
        fun from(s: String): SemVersion {
            val match = regex.matchEntire(s)
            return SemVersion(
                Integer.parseInt(match?.groups[1]?.value),
                Integer.parseInt(match?.groups[2]?.value),
                Integer.parseInt(match?.groups[3]?.value),
                match?.groups[4]?.value,
                match?.groups[5]?.value
            )
        }
    }
}