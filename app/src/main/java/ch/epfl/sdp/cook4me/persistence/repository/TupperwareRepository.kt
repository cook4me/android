package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore

private const val COLLECTION_PATH = "tupperwares"

class TupperwareRepository(store: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    BaseRepository(store, COLLECTION_PATH)
