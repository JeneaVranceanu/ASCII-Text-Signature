import kotlin.math.ceil

fun main() {
    val vowels = arrayOf('a', 'e', 'i', 'o', 'u', 'y')

    val word = readLine()!!
    val charPairs: MutableList<Pair<Boolean, Int>> = mutableListOf()

    for (c in word) {
        val lastPair = charPairs.lastOrNull()
        val isVowel = vowels.contains(c)

        when {
            lastPair == null || lastPair.first != isVowel -> charPairs.add(Pair(isVowel, 1))
            lastPair.first && isVowel || !lastPair.first && !isVowel -> {
                charPairs.removeAt(charPairs.lastIndex)
                charPairs.add(Pair(isVowel, lastPair.second + 1))
            }
        }
    }

    val result = charPairs.map {
        (ceil(it.second / 2.0) - 1).toInt()
    }.reduce { acc, i ->
        acc + i
    }
    
    println(result)    
}
