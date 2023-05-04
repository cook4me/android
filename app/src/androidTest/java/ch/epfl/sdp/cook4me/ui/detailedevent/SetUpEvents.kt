package ch.epfl.sdp.cook4me.ui.detailedevent

import android.util.Log
import ch.epfl.sdp.cook4me.ui.eventform.Event
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

private const val EVENT_PATH = "events"
private const val FIREBASE_PORT = 8080
private const val AUTH_PORT = 9099

fun setUpEvents(): Triple<FirebaseAuth, FirebaseFirestore, String> {
    val auth = setupFirebaseAuth()
    val firestore = setupFirebaseFirestore()

    var eventId = ""
    val eventMap = createEventMap(testEvent)

    runBlocking {
        try {
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        } catch (e: FirebaseException) {
            // If signInWithEmailAndPassword fails, try to create the user
            try {
                auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
                // Sign in after creating the user
                auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            } catch (e: FirebaseException) {
                Log.e("FirebaseAuth", "Error: ${e.message}")
            }
        }
    }

    runBlocking {
        try {
            val documentReference = firestore.collection(EVENT_PATH).add(eventMap).await()
            eventId = documentReference.id
        } catch (e: FirebaseException) {
            Log.e("Firestore", "Error: ${e.message}")
        }
    }

    return Triple(auth, firestore, eventId)
}

fun cleanUpEvents(auth: FirebaseAuth, firestore: FirebaseFirestore, eventId: String) {
    runBlocking {
        firestore.collection(EVENT_PATH).document(eventId).delete().await()
        auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        auth.currentUser?.delete()?.await()
    }
}

private fun setupFirebaseAuth(): FirebaseAuth {
    val auth = FirebaseAuth.getInstance()
    try {
        auth.useEmulator("10.0.2.2", AUTH_PORT)
    } catch (e: IllegalStateException) {
        Log.e("Emulator exception", e.message.toString())
    }
    return auth
}

private fun setupFirebaseFirestore(): FirebaseFirestore {
    val firestore = FirebaseFirestore.getInstance()
    try {
        firestore.useEmulator("10.0.2.2", FIREBASE_PORT)
    } catch (e: IllegalStateException) {
        Log.e("Emulator exception", e.message.toString())
    }
    return firestore
}

private fun createEventMap(event: Event): Map<String, Any> =
    mapOf(
        "name" to event.name,
        "description" to event.description,
        "dateTime" to event.dateTime,
        "location" to event.location,
        "maxParticipants" to event.maxParticipants,
        "participants" to event.participants,
        "creator" to event.creator,
        "id" to event.id,
        "isPrivate" to event.isPrivate,
        "latLng" to GeoPoint(event.latLng.first, event.latLng.second)
    )
