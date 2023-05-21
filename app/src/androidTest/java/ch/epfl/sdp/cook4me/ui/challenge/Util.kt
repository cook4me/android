package ch.epfl.sdp.cook4me.ui.challenge

import ch.epfl.sdp.cook4me.ui.map.Locations
import com.google.firebase.firestore.GeoPoint
import java.util.Calendar

val testChallenge = Challenge(
    name = "Mountain Climbing",
    description = "Climb the highest peak of the city!",
    dateTime = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) },
    latLng = GeoPoint(Locations.EPFL.latitude, Locations.EPFL.longitude),
    participants = mapOf("John" to 1, "Jane" to 2),
    creator = "Admin",
    type = "Spanish"
)

fun createChallengeMap(challenge: Challenge): Map<String, Any> =
    mapOf(
        "name" to challenge.name,
        "description" to challenge.description,
        "dateTime" to challenge.dateTime,
        "latLng" to challenge.latLng,
        "participants" to challenge.participants,
        "creator" to challenge.creator,
        "type" to challenge.type
    )
