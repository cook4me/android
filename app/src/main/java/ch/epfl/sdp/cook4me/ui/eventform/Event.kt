package ch.epfl.sdp.cook4me.ui.eventform

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
}

private fun getTwoDigits(number: Int): String = String.format(Locale.ENGLISH, "%02d", number)
