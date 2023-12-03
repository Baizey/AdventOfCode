package year2015

import utils.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

fun main() {
    val assign = "__ASSIGN__"
    val and = "AND"
    val or = "OR"
    val lshift = "LSHIFT"
    val rshift = "RSHIFT"
    val not = "NOT"

    data class Action(val type: String, val left: List<String>, val right: String) {
        fun get(
            variables: HashMap<String, Action>,
            resolved: HashMap<String, Int>,
            name: String
        ): Int = resolved[name] ?: variables[name]?.handle(variables, resolved) ?: name.toInt()

        fun handle(variables: HashMap<String, Action>, resolved: HashMap<String, Int>): Int {
            val result = when (type) {
                assign -> get(variables, resolved, left.first())
                not -> get(variables, resolved, left.first()).inv()
                or -> get(variables, resolved, left.first()) or get(variables, resolved, left.last())
                and -> get(variables, resolved, left.first()) and get(variables, resolved, left.last())
                lshift -> (get(variables, resolved, left.first()) shl get(variables, resolved, left.last()))
                rshift -> (get(variables, resolved, left.first()) shr get(variables, resolved, left.last()))
                else -> throw Exception()
            } and 65535
            resolved[right] = result
            return result
        }
    }

    val actions = Input.get(2015, 7).map {
        val type = if (it.contains(and)) and
        else if (it.contains(or)) or
        else if (it.contains(lshift)) lshift
        else if (it.contains(rshift)) rshift
        else if (it.contains(not)) not
        else assign
        val values = it.replace(type, "").replace("->", "").trim().split(Regex(" +"))
        Action(type, values.subList(0, values.size - 1), values.last())
    }

    fun part1() {
        val variables = HashMap<String, Action>()
        actions.forEach { variables[it.right] = it }
        println(variables["a"]?.get(variables, HashMap(), "a"))
    }

    fun part2() {
        val variables = HashMap<String, Action>()
        actions.forEach { variables[it.right] = it }
        val originalValueA = variables["a"]?.get(variables, HashMap(), "a").toString()
        variables["b"] = Action(assign, listOf(originalValueA), "b")
        println(variables["a"]?.get(variables, HashMap(), "a"))
    }

    part1()
    part2()
}