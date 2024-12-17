package year2024

import utils.*
import utils.grid.Direction.*
import utils.Helpers.clone
import utils.Helpers.findExact
import utils.Helpers.findMatches
import utils.Helpers.println
import utils.grid.Nav

fun main() {
    val box = 'O'
    val box1 = '['
    val box2 = ']'
    val open = '.'
    val wall = '#'
    val bot = '@'

    val input = Input.get(2024, 15).asLines()
    val startGrid = input.subList(0, input.indexOf("")).map { it.toCharArray().toMutableList() }
    val actions = input.subList(input.indexOf("") + 1, input.size).joinToString(separator = "").toCharArray()

    fun Nav.isWall(grid: List<MutableList<Char>>): Boolean = this.value(grid) == wall
    fun Nav.isOpen(grid: List<MutableList<Char>>): Boolean = this.value(grid) == open
    fun Nav.isBox1(grid: List<MutableList<Char>>): Boolean = this.value(grid) == box1
    fun Nav.isBox2(grid: List<MutableList<Char>>): Boolean = this.value(grid) == box2
    fun Nav.isBox(grid: List<MutableList<Char>>): Boolean = when (this.value(grid)) {
        box -> true
        box1 -> true
        box2 -> true
        else -> false
    }

    fun part1() {
        val grid = startGrid.clone()
        val at = grid.findExact { it == bot }!!
        at.setValue(open, grid)
        actions.forEach { action ->
            when (action) {
                '^' -> at.turn(north)
                '>' -> at.turn(east)
                '<' -> at.turn(west)
                'v' -> at.turn(south)
            }
            val tmp = at.clone().moveForward()
            if (tmp.isWall(grid)) {
                // do nothing
            } else if (tmp.isOpen(grid)) {
                at.moveForward()
            } else if (tmp.isBox(grid)) {
                while (tmp.isBox(grid)) tmp.moveForward()
                if (tmp.isOpen(grid)) {
                    tmp.setValue(box, grid)
                    at.moveForward()
                    at.setValue(open, grid)
                }
            }
        }
        grid.findMatches { it == box }.sumOf { it.x + 100L * it.y }.println()
    }

    fun part2() {
        val grid = startGrid.clone().map { line ->
            line.joinToString(separator = "") {
                when (it) {
                    wall -> "##"
                    box -> "[]"
                    bot -> "@."
                    open -> ".."
                    else -> throw Error()
                }
            }.toCharArray().toMutableList()
        }
        val at = grid.findExact { it == bot }!!
        at.setValue(open, grid)

        fun canMove(pos: Nav): Boolean {
            return if (pos.isOpen(grid)) true
            else if (pos.isWall(grid)) false
            else when (pos.dir) {
                north, south -> {
                    val canMoveDirect = canMove(pos.clone().moveForward())
                    val canMoveSide =
                        if (pos.isBox1(grid)) canMove(pos.clone().move(east, 1L).moveForward())
                        else canMove(pos.clone().move(west, 1L).moveForward())
                    canMoveSide && canMoveDirect
                }

                west, east -> canMove(pos.moveForward(2))
                else -> false
            }
        }

        fun move(pos: Nav) {
            if (pos.isOpen(grid)) return
            when (pos.dir) {
                north, south -> {
                    val other = if (pos.isBox1(grid)) pos.clone().move(east, 1L) else pos.clone().move(west, 1L)
                    move(pos.clone().moveForward())
                    move(other.clone().moveForward())

                    var c = pos.value(grid)
                    pos.setValue(open, grid)
                    pos.moveForward().setValue(c, grid)

                    c = other.value(grid)
                    other.setValue(open, grid)
                    other.moveForward().setValue(c, grid)

                }

                east -> {
                    move(pos.clone().moveForward(2))
                    pos.setValue(open, grid)
                    pos.moveForward()
                    pos.setValue(box1, grid)
                    pos.moveForward()
                    pos.setValue(box2, grid)

                }

                west -> {
                    move(pos.clone().moveForward(2))
                    pos.setValue(open, grid)
                    pos.moveForward()
                    pos.setValue(box2, grid)
                    pos.moveForward()
                    pos.setValue(box1, grid)
                }

                else -> throw Error()
            }
        }

        actions.forEachIndexed { i, action ->
            when (action) {
                '^' -> at.turn(north)
                '>' -> at.turn(east)
                '<' -> at.turn(west)
                'v' -> at.turn(south)
            }
            val tmp = at.clone().moveForward()
            if (tmp.isOpen(grid)) {
                at.moveForward()
            } else if (tmp.isBox(grid) && canMove(tmp.clone())) {
                move(tmp.clone())
                at.moveForward()
            }
        }
        grid.findMatches { it == box1 }.sumOf { it.x + 100L * it.y }.println()
    }

    part1()
    part2()
}