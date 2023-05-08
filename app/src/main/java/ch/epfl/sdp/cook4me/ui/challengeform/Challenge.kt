package ch.epfl.sdp.cook4me.ui.challengeform

import com.google.firebase.firestore.GeoPoint
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Challenge(
    val name: String = "",
    val description: String = "",
    val dateTime: Calendar = Calendar.getInstance(),
    val latLng: Pair<Double, Double> = Pair(0.0, 0.0),
    val participants: List<String> = listOf(),
    val creator: String = "",
    val type: String = "",
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

    val challengeProblem: String?
        get() {
            var errorMsg: String? = null
            if (name.isBlank()) errorMsg = "Name is empty"
            if (description.isBlank()) errorMsg = "Description is empty"
            if (dateTime.before(Calendar.getInstance())) errorMsg = "Date is in the past"
            if (type.isBlank()) errorMsg = "Type is empty"
            return errorMsg
        }

    val isValidChallenge: Boolean
        get() = challengeProblem == null

    val challengeInformation: String
        get() = "Name: $name\nDescription: $description\nDate: $dateAsFormattingDate\n" +
            "Participants: $participants\n Type: $type\n" +
            "Creator: $creator\n Latitude-Longitude: $latLng"

    val challengeDate: String
        get() = "$dateAsFormattingDate"

    constructor(map: Map<String, Any>) : this(
        name = map["name"] as? String ?: "",
        description = map["description"] as? String ?: "",
        dateTime = (map["dateTime"] as? Map<String, Any>)
            ?.let { it["time"] as? com.google.firebase.Timestamp }
            ?.toDate()
            ?.let { calendarFromTime(it) }
            ?: Calendar.getInstance(),
        participants = map["participants"] as? List<String> ?: listOf(),
        creator = map["creator"] as? String ?: "",
        latLng = (map["latLng"] as? GeoPoint)?.let { Pair(it.latitude, it.longitude) } ?: Pair(0.0, 0.0),
        type = map["type"] as? String ?: "",
    )
}
private fun getTwoDigits(number: Int): String = String.format(Locale.ENGLISH, "%02d", number)
private fun calendarFromTime(date: Date): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}
