package ch.epfl.sdp.cook4me.ui.challengedetailed

import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import ch.epfl.sdp.cook4me.ui.map.Locations
import java.util.Calendar

val challengeTest = Challenge(
    name = "Mountain Climbing",
    description = "Climb the highest peak of the city!",
    dateTime = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) },
    latLng = Pair(Locations.EPFL.latitude, Locations.EPFL.longitude),
    participants = mapOf("John" to 1, "Jane" to 2),
    creator = "Admin",
    type = "Spanish"
)
