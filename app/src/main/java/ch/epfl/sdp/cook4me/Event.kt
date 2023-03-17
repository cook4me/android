package ch.epfl.sdp.cook4me

import java.util.Calendar
import java.util.Locale

/**
 * Class representing an event where people can meet to eat together
 */
class Event {
    var name: String = ""
    var description: String = ""
    var dateTime: Calendar = Calendar.getInstance()
    var location: String = ""
    var maxParticipants: Int = 0
    var participants: List<String> = listOf()
    var creator: String = ""
    var id: String = ""
    var isPrivate: Boolean = false

    /**
     * @return a boolean indicating if the event is valid
     */
    fun isValidEvent(): Boolean = eventProblem().isEmpty()

    // TODO: get strings from resources or return strings id?
    /**
     * @return a string describing the problem with the creation of the event
     *         if the event is valid, return an empty string
     */
    fun eventProblem(): String {
        var errorMsg = ""
        if (name.isEmpty()) errorMsg = "Name is empty"
        if (description.isEmpty()) errorMsg = "Description is empty"
        if (location.isEmpty()) errorMsg = "Location is empty"
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
     * @return a string representing the date and time of the event
     */
    private fun showDate(): String {
        // make that there is always 2 digits
        val month = getTwoDigits(dateTime.get(Calendar.MONTH) + 1)
        val day = getTwoDigits(dateTime.get(Calendar.DAY_OF_MONTH))
        val hour = getTwoDigits(dateTime.get(Calendar.HOUR_OF_DAY))
        val minute = getTwoDigits(dateTime.get(Calendar.MINUTE))
        val date = "$day/$month/${dateTime.get(Calendar.YEAR)}"
        val time = "$hour:$minute"
        return "$date at $time"
    }

    /**
     * @return a string representing the event information
     */
    fun showEventInformation(): String =
        "Name: $name\nDescription: $description\nDate: ${showDate()}\nLocation: $location\n" +
            "Max participants: $maxParticipants\nParticipants: $participants\n" +
            "Creator: $creator\nId: $id\nIs private: $isPrivate"
}
