package ch.epfl.sdp.cook4me

import java.util.Calendar
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
    val dateAsFormattingDate: String
        get() { // make that there is always 2 digits
            val month = getTwoDigits(dateTime.get(Calendar.MONTH) + 1)
            val day = getTwoDigits(dateTime.get(Calendar.DAY_OF_MONTH))
            val hour = getTwoDigits(dateTime.get(Calendar.HOUR_OF_DAY))
            val minute = getTwoDigits(dateTime.get(Calendar.MINUTE))
            val date = "$day/$month/${dateTime.get(Calendar.YEAR)}"
            val time = "$hour:$minute"
            return "$date at $time"
        }
}

/**
 * @return a boolean indicating if the event is valid
 */
fun Event.isValidEvent(): Boolean = eventProblem() == null

/**
 * @return a string describing the problem with the creation of the event
 *         if the event is valid, return an empty string
 */
fun Event.eventProblem(): String? {
    var errorMsg: String? = null
    if (name.isBlank()) errorMsg = "Name is empty"
    if (description.isBlank()) errorMsg = "Description is empty"
    if (location.isBlank()) errorMsg = "Location is empty"
    if (maxParticipants < 2) errorMsg = "Max participants is less than 2"
    if (dateTime.before(Calendar.getInstance())) errorMsg = "Date is in the past"
    return errorMsg
}

/**
 * @param number the number to format
 * @return a string representing the number with 2 digits
 */
private fun getTwoDigits(number: Int): String = String.format(Locale.ENGLISH, "%02d", number)

/**
 * @return a string representing the event information
 */
fun Event.showEventInformation(): String =
    "Name: $name\nDescription: $description\nDate: $dateAsFormattingDate\nLocation: $location\n" +
        "Max participants: $maxParticipants\nParticipants: $participants\n" +
        "Creator: $creator\nId: $id\nIs private: $isPrivate"
