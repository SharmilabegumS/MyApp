package Entity


class Event(
    val eventId: String, var startDate: Long?, var endDate: Long?, val location: String, val guests: List<String>,
    val description: String
)
