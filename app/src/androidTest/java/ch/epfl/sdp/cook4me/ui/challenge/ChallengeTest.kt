package ch.epfl.sdp.cook4me.ui.challenge

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ChallengeTest {

    private var challenge = Challenge()

    @Before
    fun init() {
        val dateTime = Calendar.getInstance()
        // to ensure event is in the future
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        challenge = Challenge(
            name = "name",
            description = "description",
            dateTime = dateTime,
            participants = mapOf("participant1" to 0, "participant2" to 0),
            participantIsVoted = mapOf("participant1" to false, "participant2" to false),
            creator = "darth.vader@epfl.ch",
            type = "French"
        )
    }

    @Test
    fun challengeWithValidInformationIsValid() {
        assert(challenge.isValidChallenge)
    }

    @Test
    fun showChallengeInformationDisplaysCorrectInformation() {
        // 01/01/2000 at 00:00
        challenge.dateTime.set(2000, 0, 1, 0, 0)
        var expected = """
            Name: name
            Description: description
            Date: 01/01/2000 at 00:00
            Participants: {participant1=0,participant2=0}
            ParticipantVoted: {participant1=false,participant2=false}
            Type: French
            Creator: darth.vader@epfl.ch
            Latitude-Longitude: GeoPoint{latitude=0.0,longitude=0.0}"""
        // remove all the spaces
        expected = expected.replace("\\s".toRegex(), "")
        val actual = challenge.challengeInformation.replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }

    @Test
    fun challengeWithEmptyNameIsInvalid() {
        challenge = challenge.copy(name = "")
        assert(!challenge.isValidChallenge)
        val errorMsg = "Name is empty"
        assertEquals(errorMsg, challenge.challengeProblem)
    }

    @Test
    fun challengeWithEmptyDescriptionIsInvalid() {
        challenge = challenge.copy(description = "")
        assert(!challenge.isValidChallenge)
        val errorMsg = "Description is empty"
        assertEquals(errorMsg, challenge.challengeProblem)
    }

    @Test
    fun challengeWithDateInThePastIsInvalid() {
        val pastDateTime = Calendar.getInstance()
        pastDateTime.add(Calendar.HOUR_OF_DAY, -1)
        challenge = challenge.copy(dateTime = pastDateTime)
        assert(!challenge.isValidChallenge)
        val errorMsg = "Date is in the past"
        assertEquals(errorMsg, challenge.challengeProblem)
    }

    @Test
    fun addNewParticipantInNonFullChallengeAddsParticipant() {
        val participant = "participant3"
        val expected = challenge.copy(
            participants = challenge.participants + (participant to 0),
            participantIsVoted = challenge.participantIsVoted + (participant to false)
        )
        val actual = addParticipant(challenge, participant)
        assertEquals(expected, actual)
    }

    @Test
    fun addExistingParticipantInNonFullChallengeDoesNotAddParticipant() {
        val participant = "participant1"
        val expected = challenge
        val actual = addParticipant(challenge, participant)
        assertEquals(expected, actual)
    }

    @Test
    fun changeScoreOfExistingParticipantUpdatesScore() {
        val participant = "participant1"
        val scoreChange = 5
        val updatedScore = challenge.participants[participant]!! + scoreChange
        val expected = challenge.copy(participants = challenge.participants.toMutableMap().apply { this[participant] = updatedScore })
        val actual = changeParticipantScore(challenge, participant, scoreChange)
        assertEquals(expected, actual)
    }

    @Test
    fun changeScoreOfNonExistingParticipantDoesNotUpdateScore() {
        val participant = "participant3"
        val scoreChange = 5
        val expected = challenge
        val actual = changeParticipantScore(challenge, participant, scoreChange)
        assertEquals(expected, actual)
    }

    @Test
    fun changeIsVotedOfExistingParticipantUpdatesIsVoted() {
        val participant = "participant1"
        val isVoted = true
        val expected = challenge.copy(participantIsVoted = challenge.participantIsVoted.toMutableMap().apply { this[participant] = isVoted })
        val actual = changeParticipantIsVoted(challenge, participant, isVoted)
        assertEquals(expected, actual)
    }

    @Test
    fun changeIsVotedOfNonExistingParticipantDoesNotUpdateIsVoted() {
        val participant = "participant3"
        val isVoted = true
        val expected = challenge
        val actual = changeParticipantIsVoted(challenge, participant, isVoted)
        assertEquals(expected, actual)
    }

    @Test
    fun challengeDateReturnsFormattedDate() {
        val dateTime = Calendar.getInstance()
        dateTime.set(2923, Calendar.MAY, 10, 15, 30)
        challenge = challenge.copy(dateTime = dateTime)

        val expected = "10/05/2923 at 15:30"
        val actual = challenge.challengeDate

        assertEquals(expected, actual)
    }

    @Test
    fun toMapConvertsChallengeToMap() {
        val dateTime = Calendar.getInstance()
        dateTime.set(2923, Calendar.MAY, 10, 15, 30)
        challenge = challenge.copy(dateTime = dateTime)

        val expected = mapOf(
            "name" to "name",
            "description" to "description",
            "dateTime" to dateTime,
            "participants" to mapOf("participant1" to 0, "participant2" to 0),
            "participantIsVoted" to mapOf("participant1" to false, "participant2" to false),
            "creator" to "darth.vader@epfl.ch",
            "latLng" to GeoPoint(0.0, 0.0),
            "type" to "French",
        )
        val actual = challenge.toMap()
        actual.forEach {
            assertEquals(it.value, expected[it.key])
        }
    }

    @Test
    fun challengeConstructorFromMapWithGeoPointCreatesChallenge() {
        val dateTime = Calendar.getInstance()
        dateTime.set(2923, Calendar.MAY, 10, 15, 30)
        challenge = challenge.copy(dateTime = dateTime)
        val geoPoint = GeoPoint(46.519962, 6.633597)

        val dataMap = mapOf(
            "name" to "name",
            "description" to "description",
            "dateTime" to mapOf("time" to com.google.firebase.Timestamp(dateTime.time)),
            "participants" to mapOf("participant1" to 0, "participant2" to 0),
            "participantIsVoted" to mapOf("participant1" to false, "participant2" to false),
            "creator" to "darth.vader@epfl.ch",
            "latLng" to geoPoint,
            "type" to "French"
        )

        val expected = challenge.copy(latLng = geoPoint)
        val actual = Challenge(dataMap)

        assertEquals(expected, actual)
    }

    @Test
    fun testParseEmail() {
        val email = "darth.vader@epfl.ch"
        val expected = "darth vader"
        val actual = parseParticipantEmailToName(email)
        assertEquals(expected, actual)
    }
}
