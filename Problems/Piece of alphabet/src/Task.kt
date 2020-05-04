fun main() {
    val result = ('a'..'z')
            .joinToString(separator = "")
            .contains(readLine()!!.toLowerCase())
    println(result)
}