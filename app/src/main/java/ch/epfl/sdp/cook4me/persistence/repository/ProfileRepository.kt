package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "profiles"

class ProfileRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun add(value: Profile) {
        store.collection(COLLECTION_PATH).add(value).await()
    }

    suspend fun getAll(): Map<String, Profile> {
        val result = store.collection(COLLECTION_PATH).get().await()
        return result.map { it.id }.zip(result.toObjects(Profile::class.java)).toMap()
    }

    suspend fun getById(id: String) =
        store.collection(COLLECTION_PATH).document(id).get().await()
            .toObject(Profile::class.java)

    suspend fun update(id: String, value: Profile) {
        store.collection(COLLECTION_PATH).document(id).set(value).await()
    }
}
