package signature

import java.io.File
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

val mediumFont = mutableMapOf<Char, String>()
val romanFont = mutableMapOf<Char, String>()

enum class BorderConfig(val top: String,
                        val left: String,
                        val right: String,
                        val bottom: String) {

    Default("_", "|", "|", "¯"),
    StarBorder("*", "*", "*", "*") {
        override fun getTopBorderOfLength(length: Int) = top.repeat(length + 2)
        override fun getBottomBorderOfLength(length: Int) = bottom.repeat(length + 2)
    },
    EightsBorder("8", "88", "88", "8") {
        override fun getTopBorderOfLength(length: Int) = top.repeat(length + 4)
        override fun getBottomBorderOfLength(length: Int) = bottom.repeat(length + 4)
    };

    open fun getTopBorderOfLength(length: Int) = " ${top.repeat(length)}"
    open fun getBottomBorderOfLength(length: Int) = " ${bottom.repeat(length)}"
}

fun main() {
    readFont(mediumFont, Scanner(File("medium.txt")))
    readFont(romanFont, Scanner(File("roman.txt")))

    val scanner = Scanner(System.`in`)
    println("Enter name and surname: ")
    val fullName = scanner.nextLine().trim()
    println("Enter person's status: ")
    val status = scanner.nextLine().trim()

    val titleRoman = applyFont(romanFont, fullName)
    val subtitleAscii = applyFont(mediumFont, status)

    val badge = appendSubtitle(titleRoman, subtitleAscii)

    println(wrapWithBorders(badge, BorderConfig.EightsBorder))
}

fun applyFont(romanFont: MutableMap<Char, String>, string: String): String {
    val modifiedString =
            string.map {
                romanFont[it]!!
            }.reduce { acc, s -> combine(acc, s) }

    val singleSpace = romanFont['-']!!
    return combine(singleSpace, modifiedString, singleSpace)
}

fun readFont(fontMap: MutableMap<Char, String>, scanner: Scanner) {
    val info = scanner.nextLine().split(" ")
    val linesPerChar = info[0].toInt()
    val charsToRead = info[1].toInt()
    repeat(charsToRead) {
        val char = scanner.nextLine().split(" ")[0][0]
        fontMap[char] = buildString {
            repeat(linesPerChar) {
                append(scanner.nextLine())
                append("\n")
            }
        }.dropLast(1)
    }
    fontMap[' '] = (" ".repeat(fontMap['a']!!.lines()[0].length) + "\n")
            .repeat(linesPerChar).dropLast(1)
    fontMap['-'] = ("  \n").repeat(linesPerChar).dropLast(1)
}

fun appendSubtitle(title: String, subtitle: String): String {
    val titleLength = title.lines().maxBy { it.length }!!.length
    val subtitleLength = subtitle.lines().maxBy { it.length }!!.length

    val totalEmptySpaceRequired = abs(titleLength - subtitleLength)
    val leftEmptySpace = floor(totalEmptySpaceRequired / 2.0).toInt()

    val leftEmptySpaceLine =
            " ".repeat(leftEmptySpace)
    val rightEmptySpaceLine =
            " ".repeat(totalEmptySpaceRequired - leftEmptySpace)

    return buildString {
        when {
            titleLength == subtitleLength -> {
                append(title)
                append("\n")
                append(subtitle)
            }
            titleLength > subtitleLength -> {
                append(title)
                append("\n")
                append(extendOnSides(subtitle, leftEmptySpaceLine, rightEmptySpaceLine))
            }
            else -> {
                append(extendOnSides(title, leftEmptySpaceLine, rightEmptySpaceLine))
                append("\n")
                append(subtitle)
            }
        }
    }
}

fun extendOnSides(content: String, stringToAddFromLeft: String, stringToAddFromRight: String) =
        buildString {
            for (line in content.lines()) {
                append(stringToAddFromLeft)
                append(line)
                append(stringToAddFromRight)
                append("\n")
            }
        }.dropLast(1)

fun combine(vararg multilineStrings: String): String {
    val maxLinesCount = multilineStrings.map { it.lines().size }.max()!!
    val multilineStringsLines = multilineStrings.map { it.lines() }
    return buildString {
        for (i in 0..maxLinesCount - 1) {
            for (lines in multilineStringsLines) {
                if (lines.size - 1 >= i) {
                    append(lines[i])
                } else {
                    break
                }
            }

            if (i != maxLinesCount - 1) append("\n")
        }
    }
}

fun wrapWithBorders(stringToWrap: String, borders: BorderConfig): String {
    val lines = stringToWrap.lines()
    val width = lines.map { it.length }.max()!!

    val preparedTopBorder = borders.getTopBorderOfLength(width)
    val preparedBottomBorder = borders.getBottomBorderOfLength(width)

    val sideBorderedString = buildString {
        for (line in lines.indices) {
            append("${borders.left}${lines[line]}${borders.right}")
            if (line != lines.size - 1) append("\n")
        }
    }

    return "$preparedTopBorder\n$sideBorderedString\n$preparedBottomBorder"
}