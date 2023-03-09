package ch.epfl.sdp.cook4me

class Event {
    var name: String = ""
    var description: String = ""
    var date: String = ""
    var time: String = ""
    var location: String = ""
    var maxParticipants: Int = 0
    var participants: List<String> = listOf()
    var creator: String = ""
    var id: String = ""
    var isPrivate: Boolean = false

    constructor()

    fun isValidEvent(): Boolean {
        return name.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && location.isNotEmpty() && maxParticipants > 1
    }

    /**
     * @return a string describing the problem with the creation of the event
     *         if the event is valid, return an empty string
     */
    fun eventProblem(): String{
        if (name.isEmpty()) return "Name is empty"
        if (description.isEmpty()) return "Description is empty"
        if (date.isEmpty()) return "Date is empty"
        if (time.isEmpty()) return "Time is empty"
        if (location.isEmpty()) return "Location is empty"
        if (maxParticipants < 2) return "Max participants is less than 2"
        return ""
    }

}
