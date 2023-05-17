package ch.epfl.sdp.cook4me

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

fun setupFirestore(): FirebaseFirestore {
    val store = FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder()
        .setHost("10.0.2.2:8080") // connect to local firestore emulator
        .setSslEnabled(false)
        .setPersistenceEnabled(false)
        .build()
    store.firestoreSettings = settings
    return store
}

fun setupFirebaseAuth(): FirebaseAuth {
    Firebase.auth.useEmulator("10.0.2.2", 9099)
    return FirebaseAuth.getInstance()
}

fun setupFirebaseStorage(): FirebaseStorage {
    val storage = FirebaseStorage.getInstance()
    storage.useEmulator("10.0.2.2", 9199)
    return storage
}


