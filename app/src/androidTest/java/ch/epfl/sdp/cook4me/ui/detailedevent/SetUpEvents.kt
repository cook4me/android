package ch.epfl.sdp.cook4me.ui.detailedevent

private const val FIREBASE_PORT = 8080
private const val AUTH_PORT = 9099

// fun setUpEvents(path: String): Triple<FirebaseAuth, FirebaseFirestore, String> {
//    val auth = setupFirebaseAuth()
//    val firestore = setupFirebaseFirestore()
//
//    var eventId = ""
//    val eventMap = if (path == "events") createEventMap(testEvent) else createChallengeMap(challengeTest)
//
//    runBlocking {
//        try {
//            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//        } catch (e: FirebaseException) {
//            // If signInWithEmailAndPassword fails, try to create the user
//            try {
//                auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//                // Sign in after creating the user
//                auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//            } catch (e: FirebaseException) {
//                Log.e("FirebaseAuth", "Error: ${e.message}")
//            }
//        }
//    }
//
//    runBlocking {
//        try {
//            val documentReference = firestore.collection(path).add(eventMap).await()
//            eventId = documentReference.id
//        } catch (e: FirebaseException) {
//            Log.e("Firestore", "Error: ${e.message}")
//        }
//    }
//
//    return Triple(auth, firestore, eventId)
// }
//
// fun cleanUpEvents(auth: FirebaseAuth, firestore: FirebaseFirestore, eventId: String, path: String) {
//    runBlocking {
//        firestore.collection(path).document(eventId).delete().await()
//        auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
//        auth.currentUser?.delete()?.await()
//    }
// }
//
//
// private fun createEventMap(event: Event): Map<String, Any> =
//    mapOf(
//        "name" to event.name,
//        "description" to event.description,
//        "dateTime" to event.dateTime,
//        "location" to event.location,
//        "maxParticipants" to event.maxParticipants,
//        "participants" to event.participants,
//        "creator" to event.creator,
//        "id" to event.id,
//        "isPrivate" to event.isPrivate,
//        "latLng" to event.latLng
//    )
