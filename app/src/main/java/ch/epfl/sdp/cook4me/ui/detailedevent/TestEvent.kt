package ch.epfl.sdp.cook4me.ui.detailedevent

import ch.epfl.sdp.cook4me.ui.eventform.Event
import java.util.Calendar

val testEventDate = Calendar.getInstance().apply {
    set(Calendar.YEAR, 2200)
    set(Calendar.MONTH, Calendar.MARCH)
    set(Calendar.DAY_OF_MONTH, 27)
    set(Calendar.HOUR_OF_DAY, 14)
    set(Calendar.MINUTE, 12)
}

val testEvent = Event(
    name = "test event name",
    description = "test description",
    dateTime = testEventDate,
    location = "mondstadt",
    maxParticipants = 4,
    participants = listOf("obi.wang", "harry.potter"),
    creator = "peter griffin",
    id = "harry.potter@epfl.ch",
    isPrivate = false
)
