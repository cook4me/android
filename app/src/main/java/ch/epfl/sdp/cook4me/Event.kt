package ch.epfl.sdp.cook4me

import java.util.*

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
    fun isValidEvent(): Boolean {
        return eventProblem().isEmpty()
    }

    /**
     * @return a string describing the problem with the creation of the event
     *         if the event is valid, return an empty string
     */
    fun eventProblem(): String{
        if (name.isEmpty()) return "Name is empty"
        if (description.isEmpty()) return "Description is empty"
        if (location.isEmpty()) return "Location is empty"
        if (maxParticipants < 2) return "Max participants is less than 2"
        if (dateTime.before(Calendar.getInstance())) return "Date is in the past"
        return ""
    }

    /**
     * @return a string representing the date and time of the event
     */
    private fun showDate(): String{
        // make that there is always 2 digits
        val month = String.format("%02d", dateTime.get(Calendar.MONTH)+1)
        val day = String.format("%02d", dateTime.get(Calendar.DAY_OF_MONTH))
        val hour = String.format("%02d", dateTime.get(Calendar.HOUR_OF_DAY))
        val minute = String.format("%02d", dateTime.get(Calendar.MINUTE))
        val date = "${day}/${month}/${dateTime.get(Calendar.YEAR)}"
        val time = "${hour}:${minute}"
        return "$date at $time"
    }

    /**
     * @return a string representing the event information
     */
    fun showEventInformation(): String{
        return "Name: $name\nDescription: $description\nDate: ${showDate()}\nLocation: $location\nMax participants: $maxParticipants\nParticipants: $participants\nCreator: $creator\nId: $id\nIs private: $isPrivate"
    }

}
