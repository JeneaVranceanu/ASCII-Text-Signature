fun main() {
    val line = readLine()!!.toLowerCase()
    val gcCount = line.filter {
        it == 'g' || it == 'c'
    }.length

    println(gcCount / line.length.toDouble() * 100)
}