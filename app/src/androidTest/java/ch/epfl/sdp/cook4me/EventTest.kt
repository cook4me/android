package ch.epfl.sdp.cook4me

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class EventTest {

    private val event = Event()

    @Before
    fun init() {
        event.name = "name"
        event.description = "description"
        event.dateTime = Calendar.getInstance()
        // to ensure event is in the future
        event.dateTime.set(Calendar.YEAR, event.dateTime.get(Calendar.YEAR) + 1)
        event.location = "location"
        event.maxParticipants = 10
        event.participants = listOf("participant1", "participant2")
        event.id = "id"
        event.isPrivate = true
        event.creator = "creator"
    }

    @Test
    fun eventWithValidInformationIsValid() {
        assert(event.isValidEvent())
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
        val actual = event.showEventInformation().replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }

    @Test
    fun eventWithEmptyNameIsInvalid() {
        event.name = ""
        assert(!event.isValidEvent())
        val errorMsg = "Name is empty"
        assertEquals(errorMsg, event.eventProblem())
    }

    @Test
    fun eventWithEmptyDescriptionIsInvalid() {
        event.description = ""
        assert(!event.isValidEvent())
        val errorMsg = "Description is empty"
        assertEquals(errorMsg, event.eventProblem())
    }

    @Test
    fun eventWithEmptyLocationIsInvalid() {
        event.location = ""
        assert(!event.isValidEvent())
        val errorMsg = "Location is empty"
        assertEquals(errorMsg, event.eventProblem())
    }

    @Test
    fun eventWithMaxParticipantsLessThan2IsInvalid() {
        event.maxParticipants = 1
        assert(!event.isValidEvent())
        val errorMsg = "Max participants is less than 2"
        assertEquals(errorMsg, event.eventProblem())
    }

    @Test
    fun eventWithDateInThePastIsInvalid() {
        event.dateTime = Calendar.getInstance()
        event.dateTime.add(Calendar.HOUR_OF_DAY, -1)
        assert(!event.isValidEvent())
        val errorMsg = "Date is in the past"
        assertEquals(errorMsg, event.eventProblem())
    }
}
