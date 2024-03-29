package ch.epfl.sdp.cook4me.ui.challenge

import com.google.firebase.firestore.GeoPoint
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Challenge(
    val name: String = "",
    val description: String = "",
    val dateTime: Calendar = Calendar.getInstance(),
    val latLng: GeoPoint = GeoPoint(0.0, 0.0),
    val participants: Map<String, Int> = mapOf(), // keep track of the participant vote score
    val participantIsVoted: Map<String, Boolean> = mapOf(), // keep track of the if participant has voted
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
            "Participants: $participants\nParticipantVoted: $participantIsVoted\nType: $type\n" +
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
        participants = (map["participants"] as? Map<String, Any>)?.mapValues {
            when (val value = it.value) {
                is Int -> value
                is Long -> value.toInt()
                else -> 0
            }
        } ?: mapOf(),
        participantIsVoted = map["participantIsVoted"] as? Map<String, Boolean> ?: mapOf(),
        creator = map["creator"] as? String ?: "",
        latLng = map["latLng"] as? GeoPoint ?: GeoPoint(0.0, 0.0),
        type = map["type"] as? String ?: "",
    )
    fun toMap(): Map<String, Any> =
        mapOf(
            "name" to name,
            "description" to description,
            "dateTime" to dateTime,
            "participants" to participants,
            "participantIsVoted" to participantIsVoted,
            "creator" to creator,
            "latLng" to latLng,
            "type" to type
        )
}

private fun getTwoDigits(number: Int): String = String.format(Locale.ENGLISH, "%02d", number)
private fun calendarFromTime(date: Date): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}

/**
* Add a participant to the challenge, initialize the score of participant to 0
 * and the isVoted status to false
* @param challenge the challenge to add the participant to
* @param participant the participant to add
 * @return the updated challenge
 * @sample updatedChallenge = addParticipant(challenge, "newParticipantName")
*/
fun addParticipant(challenge: Challenge, participant: String): Challenge =
    if (!challenge.participants.containsKey(participant)) {
        challenge.copy(
            participants = challenge.participants + (participant to 0),
            participantIsVoted = challenge.participantIsVoted + (participant to false)
        )
    } else {
        challenge
    }

/**
 * Change the score of a participant in the challenge.
 * @param challenge the challenge to add the participant to
 * @param participant the participant to add
 * @param scoreChange the score to change to the participant
 * @sample updatedChallenge = changeParticipantScore(challenge, "participantName", 5)
 */
fun changeParticipantScore(challenge: Challenge, participant: String, scoreChange: Int): Challenge {
    if (challenge.participants.containsKey(participant)) {
        val currentScore = challenge.participants[participant] ?: 0
        val newScore = currentScore + scoreChange
        val updatedParticipants = challenge.participants.toMutableMap()
        updatedParticipants[participant] = newScore
        return challenge.copy(participants = updatedParticipants)
    } else {
        return challenge
    }
}

/**
 * Change the is voted of a participant in the challenge.
 * @param challenge the challenge to add the participant to
 * @param participant the participant to add
 * @param isvoted the updated isVoted status
 * @sample updatedChallenge = changeParticipantScore(challenge, "participantName", true)
 */
fun changeParticipantIsVoted(challenge: Challenge, participant: String, isVoted: Boolean): Challenge {
    if (challenge.participantIsVoted.containsKey(participant)) {
        val updatedParticipantIsVoted = challenge.participantIsVoted.toMutableMap()
        updatedParticipantIsVoted[participant] = isVoted
        return challenge.copy(participantIsVoted = updatedParticipantIsVoted)
    } else {
        return challenge
    }
}

/**
 * Parse the email of a participant to a name
 * @param email the email of the participant, e.g. could be retrieved from account service
 * @return the name of the participant
 * @sample parseParticipantEmailToName("darth.vader@epfl.ch") = "darth vader"
 */
fun parseParticipantEmailToName(email: String): String {
    val name = email.split("@")[0]
    return name.replace(".", " ")
}
