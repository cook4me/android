package ch.epfl.sdp.cook4me.ui.event

import ch.epfl.sdp.cook4me.ui.event.form.Event
import com.google.firebase.firestore.GeoPoint
import java.util.Calendar

val testEventDate: Calendar = Calendar.getInstance().apply {
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
    maxParticipants = 4,
    creator = "peter griffin",
    id = "harry.potter@epfl.ch",
    latLng = GeoPoint(46.52298091481133, 6.5657859621449335)
)
