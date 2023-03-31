package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import com.google.firebase.firestore.FirebaseFirestore

private const val COLLECTION_PATH = "tupperwares"

class TupperwareRepository(store: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    ObjectRepository(store, COLLECTION_PATH) {

    suspend fun add(value: Tupperware) {
        super.add(value)
    }

    suspend fun update(id: String, value: Tupperware) {
        super.update(id, value)
    }
}
