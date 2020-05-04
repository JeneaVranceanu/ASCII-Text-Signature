import java.lang.RuntimeException
import java.util.Scanner
import kotlin.math.max
import kotlin.random.Random

lateinit var random: Random

val alphabet = ('a'..'z').toList()
val alphabetUpperCased = ('A'..'Z').toList()
val digits = ('0'..'9').toList()

fun main() {
    val scanner = Scanner(System.`in`)
    val upperCaseLettersCount = scanner.nextInt()
    val lowerCaseLettersCount = scanner.nextInt()
    val digitsCount = scanner.nextInt()
    val totalCount = scanner.nextInt()
    val randomCharsCount = totalCount - digitsCount - lowerCaseLettersCount - upperCaseLettersCount
    val seed = (upperCaseLettersCount / max(1, lowerCaseLettersCount) * digitsCount % totalCount).toLong()

    random = Random(seed + System.currentTimeMillis())

    val passwordPattern = buildString {
        append("d".repeat(digitsCount),
                "l".repeat(lowerCaseLettersCount),
                "L".repeat(upperCaseLettersCount),
                "dlL".random(random, randomCharsCount))
    }.randomize()

    val password = generatePassword(passwordPattern)
    println(password)
}

fun generatePassword(passwordPattern: String) =
        buildString {
            var passwordIsGenerated = false
            while (!passwordIsGenerated) {
                val charToInsert = randomCharacter(passwordPattern[length])
                if (lastOrNull() != charToInsert) {
                    append(charToInsert)
                } else if (random.nextBoolean()) {
                    newRandom()
                }

                passwordIsGenerated = length == passwordPattern.length
            }
        }

fun newRandom() {
    random = Random(random.nextLong() + System.currentTimeMillis())
}

fun randomCharacter(charType: Char) =
        when (charType) {
            'd' -> digits.random(random)
            'l' -> alphabet.random(random)
            'L' -> alphabetUpperCased.random(random)
            else -> throw RuntimeException("Character type is not supported")
        }

fun String.random(random: Random, times: Int): String {
    val str = this
    return buildString {
        repeat(max(0, times)) {
            append(str.random(random))
        }
    }
}

fun String.randomize(): String {
    val str = this
    val limitedIndices = indices.toMutableList()

    return buildString {
        while (limitedIndices.isNotEmpty()) {
            val index = limitedIndices.random(random)
            append(str[index])
            limitedIndices.remove(index)
        }
    }
}
