package mine.statuses

enum class StatusCode(val code: Int, val description: String) {

    OK(200, "All OK"),
    BadRequest(400, "Something went wrong"),
    Unauthorized(401, "todo"),
    Forbidden(403, "todo"),
    NotFound(404, "todo"),


    Unknown(0, "Unknown error")
}