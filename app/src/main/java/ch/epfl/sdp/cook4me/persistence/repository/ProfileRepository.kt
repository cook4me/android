package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "profiles"

class ProfileRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun add(value: Profile) {
        store.collection(COLLECTION_PATH).add(value).await()
    }

    suspend fun getById(id: String) =
        store.collection(COLLECTION_PATH).whereEqualTo("email", id).get().await()
            .first()?.toObject(Profile::class.java)

    suspend fun update(id: String, value: Profile) {
        store.collection(COLLECTION_PATH).document(id).set(value).await()
    }
}
