package ch.epfl.sdp.cook4me.ui.event.form

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class EventTest {

    private var event = Event()

    @Before
    fun init() {
        val dateTime = Calendar.getInstance()
        // to ensure event is in the future
        dateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR) + 1)
        event = Event(
            name = "name",
            description = "description",
            dateTime = dateTime,
            location = "location",
            maxParticipants = 10,
            participants = listOf("participant1", "participant2"),
            id = "id",
            isPrivate = true,
            creator = "creator"
        )
    }

    @Test
    fun eventWithValidInformationIsValid() {
        assert(event.isValidEvent)
    }

    @Test
    fun showEventInformationDisplaysCorrectInformation() {
        // 01/01/2000 at 00:00
        event.dateTime.set(2000, 0, 1, 0, 0)
        var expected = """
            Name: name
            Description: description
            Date: 01/01/2000 at 00:00
            Location: location
            Max participants: 10
            Participants: [participant1, participant2]
            Creator: creator
            Id: id
            Is private: true"""
        // remove all the spaces
        expected = expected.replace("\\s".toRegex(), "")
        val actual = event.eventInformation.replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }

    @Test
    fun eventWithEmptyNameIsInvalid() {
        event = event.copy(name = "")
        assert(!event.isValidEvent)
        val errorMsg = "Name is empty"
        assertEquals(errorMsg, event.eventProblem)
    }

    @Test
    fun eventWithEmptyDescriptionIsInvalid() {
        event = event.copy(description = "")
        assert(!event.isValidEvent)
        val errorMsg = "Description is empty"
        assertEquals(errorMsg, event.eventProblem)
    }

    @Test
    fun eventWithEmptyLocationIsInvalid() {
        event = event.copy(location = "")
        assert(!event.isValidEvent)
        val errorMsg = "Location is empty"
        assertEquals(errorMsg, event.eventProblem)
    }

    @Test
    fun eventWithMaxParticipantsLessThan2IsInvalid() {
        event = event.copy(maxParticipants = 1)
        assert(!event.isValidEvent)
        val errorMsg = "Max participants is less than 2"
        assertEquals(errorMsg, event.eventProblem)
    }

    @Test
    fun eventWithDateInThePastIsInvalid() {
        val pastDateTime = Calendar.getInstance()
        pastDateTime.add(Calendar.HOUR_OF_DAY, -1)
        event = event.copy(dateTime = pastDateTime)
        assert(!event.isValidEvent)
        val errorMsg = "Date is in the past"
        assertEquals(errorMsg, event.eventProblem)
    }

    @Test
    fun addNewParticipantInNonFullEventAddsParticipant() {
        val participant = "participant3"
        val expected = event.copy(participants = event.participants + participant)
        val actual = addParticipant(event, participant)
        assertEquals(expected, actual)
    }

    @Test
    fun addExistingParticipantInNonFullEventDoesNotAddParticipant() {
        val participant = "participant1"
        val expected = event
        val actual = addParticipant(event, participant)
        assertEquals(expected, actual)
    }

    @Test
    fun addNewParticipantInFullEventDoesNotAddParticipant() {
        val participant = "participant3"
        val expected = event
        val actual = addParticipant(event.copy(maxParticipants = 2), participant)
        assertEquals(expected.copy(maxParticipants = 2), actual)
    }
}
