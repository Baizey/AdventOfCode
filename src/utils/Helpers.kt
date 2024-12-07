package utils

import utils.Input.submit

object Helpers {
    fun Any.println(level: Int? = null) = if (level != null) submit(level) else println(this)

    fun <T> List<List<T>>.clone() = this.map { line -> line.map { it }.toMutableList() }
}