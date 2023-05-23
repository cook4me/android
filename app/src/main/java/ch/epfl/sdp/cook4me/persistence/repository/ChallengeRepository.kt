package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.ui.challenge.Challenge
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "challenges"

class ChallengeRepository(private val store: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun add(challenge: Challenge) =
        store.collection(COLLECTION_PATH).add(challenge.toMap()).await().id

    suspend fun getById(id: String) =
        store.getObjectByIdFromCollection(id, COLLECTION_PATH) {
            Challenge(
                it.data ?: emptyMap()
            )
        }

    suspend fun update(id: String, challenge: Challenge) =
        store.updateObjectInCollection(id, challenge, COLLECTION_PATH)

    suspend fun getAll() = store.getAllObjectsFromCollection<Challenge>(COLLECTION_PATH)

    suspend fun deleteAll() = store.deleteAllDocumentsFromCollection(COLLECTION_PATH)
}
