package ch.epfl.sdp.cook4me.ui.eventform

import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Data class representing an event where people can meet to eat together
 */
data class Event(
    val name: String = "",
    val description: String = "",
    val dateTime: Calendar = Calendar.getInstance(),
    val location: String = "",
    val maxParticipants: Int = 0,
    val participants: List<String> = listOf(),
    val creator: String = "",
    val id: String = "",
    val isPrivate: Boolean = false
) {
    private val dateAsFormattingDate: String
        get() { // make that there is always 2 digits
            val month = getTwoDigits(dateTime.get(Calendar.MONTH) + 1)
            val day = getTwoDigits(dateTime.get(Calendar.DAY_OF_MONTH))
            val hour = getTwoDigits(dateTime.get(Calendar.HOUR_OF_DAY))
            val minute = getTwoDigits(dateTime.get(Calendar.MINUTE))
            val date = "$day/$month/${dateTime.get(Calendar.YEAR)}"
            val time = "$hour:$minute"
            return "$date at $time"
        }

    val eventProblem: String?
        get() {
            var errorMsg: String? = null
            if (name.isBlank()) errorMsg = "Name is empty"
            if (description.isBlank()) errorMsg = "Description is empty"
            if (location.isBlank()) errorMsg = "Location is empty"
            if (maxParticipants < 2) errorMsg = "Max participants is less than 2"
            if (dateTime.before(Calendar.getInstance())) errorMsg = "Date is in the past"
            return errorMsg
        }

    val isValidEvent: Boolean
        get() = eventProblem == null

    val eventInformation: String
        get() = "Name: $name\nDescription: $description\nDate: $dateAsFormattingDate\n" +
            "Location: $location\n Max participants: $maxParticipants\nParticipants: $participants\n" +
            "Creator: $creator\nId: $id\nIs private: $isPrivate"

    val eventDate: String
        get() = "$dateAsFormattingDate"

    constructor(map: Map<String, Any>) : this(
        name = map["name"] as? String ?: "",
        description = map["description"] as? String ?: "",
        dateTime = (map["dateTime"] as? Map<String, Any>)
            ?.let { it["time"] as? com.google.firebase.Timestamp }
            ?.toDate()
            ?.let { calendarFromTime(it) }
            ?: Calendar.getInstance(),
        location = map["location"] as? String ?: "",
        /*
        * map["maxParticipants"]: get the value of the key "maxParticipants" in the map
        * map["maxParticipants"] as? Long: cast the value to a Long, if it is not possible, return null
        * (map["maxParticipants"] as? Long)?.toInt(): if the value is not null, cast it to an Int
        * (map["maxParticipants"] as? Long)?.toInt() ?: 0 : provide a default value if the value is null (0)
        * */
        maxParticipants = (map["maxParticipants"] as? Long)?.toInt() ?: 0,
        participants = map["participants"] as? List<String> ?: listOf(),
        creator = map["creator"] as? String ?: "",
        id = map["id"] as? String ?: "",
        isPrivate = map["isPrivate"] as? Boolean ?: false
    )
}

/**
 * Adds a participant to the event (if the event is not full)
 * @param event the event to add the participant to
 * @param participant the participant to add
 * @return the event with the participant added
 */
fun addParticipant(event: Event, participant: String): Event =
    if (event.participants.size < event.maxParticipants) {
        if (!event.participants.contains(participant)) {
            event.copy(participants = event.participants + participant)
        } else {
            event
        }
    } else {
        event
    }

private fun calendarFromTime(date: Date): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}
private fun getTwoDigits(number: Int): String = String.format(Locale.ENGLISH, "%02d", number)
