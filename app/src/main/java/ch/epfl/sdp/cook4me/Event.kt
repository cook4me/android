package ch.epfl.sdp.cook4me

import java.util.*

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

    fun showEventInformation(): String{
        return "Name: $name\nDescription: $description\nDate: ${dateTime}\nLocation: $location\nMax participants: $maxParticipants\nParticipants: $participants\nCreator: $creator\nId: $id\nIs private: $isPrivate"
    }

}
