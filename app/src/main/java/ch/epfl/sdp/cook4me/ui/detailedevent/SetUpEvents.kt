package ch.epfl.sdp.cook4me.ui.detailedevent

import android.content.Context
import android.util.Log
import ch.epfl.sdp.cook4me.ui.eventform.Event
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

const val EVENT_PATH = "events"

data class SetupResult(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val eventId: String,
    val context: Context
)

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
        auth.useEmulator("10.0.2.2", 9099)
    } catch (e: IllegalStateException) {
        // emulator already set, do nothing
    }
    return auth
}

private fun setupFirebaseFirestore(): FirebaseFirestore {
    val firestore = FirebaseFirestore.getInstance()
    try {
        firestore.useEmulator("10.0.2.2", 8080)
    } catch (e: IllegalStateException) {
        // emulator already set, do nothing
    }
    return firestore
}

private fun createEventMap(event: Event): Map<String, Any> {
    return mapOf(
        "name" to event.name,
        "description" to event.description,
        "dateTime" to event.dateTime,
        "location" to event.location,
        "maxParticipants" to event.maxParticipants,
        "participants" to event.participants,
        "creator" to event.creator,
        "id" to event.id,
        "isPrivate" to event.isPrivate
    )
}