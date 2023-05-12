package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Swipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val SWIPE_COLLECTION_PATH = "swipes"

class SwipeRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun add(tupperwareId: String, liked: Boolean) {
        val email = auth.currentUser?.email
        checkNotNull(email)
        store.collection(SWIPE_COLLECTION_PATH).document(email).collection(
            SWIPE_COLLECTION_PATH
        ).document(tupperwareId).set(Swipe(liked)).await()
    }

    suspend fun getAllPositiveIdsByUser(email: String): Set<String> {
        val result = store.collection(SWIPE_COLLECTION_PATH).document(email).collection(
            SWIPE_COLLECTION_PATH).whereEqualTo("liked", true).get().await()
        return result.map { it.id }.toSet()
    }

    suspend fun getAllIdsByUser(email: String): Set<String> {
        val result = store.collection(SWIPE_COLLECTION_PATH).document(email).collection(
            SWIPE_COLLECTION_PATH).get().await()
        return result.map { it.id }.toSet()
    }
}