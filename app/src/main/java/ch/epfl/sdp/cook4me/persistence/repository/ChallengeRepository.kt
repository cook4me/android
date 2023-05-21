package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import com.google.firebase.firestore.FirebaseFirestore

private const val COLLECTION_PATH = "challenges"

class ChallengeRepository(private val store: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    ObjectRepository(store, COLLECTION_PATH) {

    suspend fun add(challenge: Challenge) =
        store.addObjectToCollection(challenge, COLLECTION_PATH)
}
