package ch.epfl.sdp.cook4me


import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*


class EventTest {

    val mockEvent = Event()

    @Before
    fun init() {
        mockEvent.name = "name"
        mockEvent.description = "description"
        mockEvent.dateTime = Calendar.getInstance()
        // to ensure event is in the future
        mockEvent.dateTime.set(Calendar.YEAR, mockEvent.dateTime.get(Calendar.YEAR) + 1)
        mockEvent.location = "location"
        mockEvent.maxParticipants = 10
        mockEvent.participants = listOf("participant1", "participant2")
        mockEvent.id = "id"
        mockEvent.isPrivate = true
        mockEvent.creator = "creator"
    }

    @Test
    fun eventWithValidInformationIsValid() {
        assert(mockEvent.isValidEvent())
    }

    @Test
    fun showEventInformationDisplaysCorrectInformation() {
        // 01/01/2000 at 00:00
        mockEvent.dateTime.set(2000, 0, 1, 0, 0)
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
        val actual = mockEvent.showEventInformation().replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }

    @Test
    fun eventWithEmptyNameIsInvalid() {
        mockEvent.name = ""
        assert(!mockEvent.isValidEvent())
        val errorMsg = "Name is empty"
        assertEquals(errorMsg, mockEvent.eventProblem())
    }

    @Test
    fun eventWithEmptyDescriptionIsInvalid() {
        mockEvent.description = ""
        assert(!mockEvent.isValidEvent())
        val errorMsg = "Description is empty"
        assertEquals(errorMsg, mockEvent.eventProblem())
    }

    @Test
    fun eventWithEmptyLocationIsInvalid() {
        mockEvent.location = ""
        assert(!mockEvent.isValidEvent())
        val errorMsg = "Location is empty"
        assertEquals(errorMsg, mockEvent.eventProblem())
    }

    @Test
    fun eventWithMaxParticipantsLessThan2IsInvalid() {
        mockEvent.maxParticipants = 1
        assert(!mockEvent.isValidEvent())
        val errorMsg = "Max participants is less than 2"
        assertEquals(errorMsg, mockEvent.eventProblem())
    }

    @Test
    fun eventWithDateInThePastIsInvalid() {
        mockEvent.dateTime = Calendar.getInstance()
        mockEvent.dateTime.set(Calendar.YEAR, mockEvent.dateTime.get(Calendar.YEAR) - 1)
        assert(!mockEvent.isValidEvent())
        val errorMsg = "Date is in the past"
        assertEquals(errorMsg, mockEvent.eventProblem())
    }
}