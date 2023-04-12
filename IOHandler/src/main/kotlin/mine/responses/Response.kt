package mine.responses

import mine.requests.Request
import mine.statuses.StatusCode
import java.io.Serializable

data class Response(
    val status: StatusCode,
    val message: String,
    val content: Map<String, Any>?,
    val request: Request
) : Serializable