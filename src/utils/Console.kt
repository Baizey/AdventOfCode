package utils

import utils.ConsoleColor.*

@Suppress("EnumEntryName")
enum class ConsoleColor {
    reset,
    black,
    red,
    green,
    yellow,
    blue,
    purple,
    cyan,
    white
}

@Suppress("MemberVisibilityCanBePrivate", "ConstPropertyName")
object Console {
    const val reset_colors = "\u001B[0m"
    const val black_background = "\u001B[40m"
    const val red_background = "\u001B[41m"
    const val green_background = "\u001B[42m"
    const val yellow_background = "\u001B[43m"
    const val blue_background = "\u001B[44m"
    const val purple_background = "\u001B[45m"
    const val cyan_background = "\u001B[46m"
    const val white_background = "\u001B[47m"
    const val black_text = "\u001B[30m"
    const val red_text = "\u001B[31m"
    const val green_text = "\u001B[32m"
    const val yellow_text = "\u001B[33m"
    const val blue_text = "\u001B[34m"
    const val purple_text = "\u001B[35m"
    const val cyan_text = "\u001B[36m"
    const val white_text = "\u001B[37m"

    fun reset() {
        print(reset_colors)
    }

    fun textColor(color: ConsoleColor): Console {
        val str = when (color) {
            reset -> reset_colors
            black -> black_text
            red -> red_text
            green -> green_text
            yellow -> yellow_text
            blue -> blue_text
            purple -> purple_text
            cyan -> cyan_text
            white -> white_text
        }
        print(str)
        return this
    }

    fun backgroundColor(color: ConsoleColor): Console {
        val str = when (color) {
            reset -> reset_colors
            black -> black_background
            red -> red_background
            green -> green_background
            yellow -> yellow_background
            blue -> blue_background
            purple -> purple_background
            cyan -> cyan_background
            white -> white_background
        }
        print(str)
        return this
    }

    fun write(str: String): Console {
        print(str)
        return this
    }

    fun line(str: String): Console {
        println(str)
        return this
    }
}