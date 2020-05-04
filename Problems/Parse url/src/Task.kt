import java.net.URLDecoder
import kotlin.text.Charsets.UTF_8

fun main() {
    val uri = URLDecoder.decode(readLine()!!, UTF_8.toString())
    var password = ""
    val queryParameters = uri
            .substringAfter("?")
            .split("&")
            .filter { it.isNotBlank() }
            .map {
                val queryKeyValue =
                        it.split("=")
                val key = queryKeyValue[0]
                val value = if (queryKeyValue.get(1).isBlank()) "not found" else queryKeyValue.get(1)

                if (key.startsWith("pass")) {
                    password = value
                }

                Pair(key, value)
            }

    queryParameters.forEach {
        println("${it.first} : ${it.second}")
    }

    if (password.isNotBlank()) {
        println("password : $password")
    }
}