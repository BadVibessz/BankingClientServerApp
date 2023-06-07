package mine.requests

import java.io.Serializable

data class Request(
    val service: String,
    val command: String,
    val content: MutableMap<String, Any> = mutableMapOf()
) : Serializable
