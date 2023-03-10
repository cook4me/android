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

    fun isValidEvent(): Boolean {
        return name.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && maxParticipants > 1
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
        return ""
    }

    private fun showDate(): String{
        val date = "${dateTime.get(Calendar.DAY_OF_MONTH)}/${dateTime.get(Calendar.MONTH)}/${dateTime.get(Calendar.YEAR)}"
        val time = "${dateTime.get(Calendar.HOUR_OF_DAY)}:${dateTime.get(Calendar.MINUTE)}"
        return "$date at $time"
    }

    fun showEventInformation(): String{
        return "Name: $name\nDescription: $description\nDate: ${showDate()}\nLocation: $location\nMax participants: $maxParticipants\nParticipants: $participants\nCreator: $creator\nId: $id\nIs private: $isPrivate"
    }

}
