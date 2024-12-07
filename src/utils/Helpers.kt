package utils

object Helpers {
    fun Any.println() = println(this)

    fun <T> List<List<T>>.clone() = this.map { line -> line.map { it }.toMutableList() }
}