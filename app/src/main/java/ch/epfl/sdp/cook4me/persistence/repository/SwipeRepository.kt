package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Swipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "swipes"

class SwipeRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ObjectRepository(store, COLLECTION_PATH) {

    suspend fun add(tupperwareId: String, liked: Boolean) {
        val email = auth.currentUser?.email
        checkNotNull(email)
        store.collection(COLLECTION_PATH).document(email).collection(
            COLLECTION_PATH
        ).document(tupperwareId).set(Swipe(liked)).await()
    }

    suspend fun getAllPositiveIdsByUser(email: String): Set<String> {
        val result = store.collection(COLLECTION_PATH).document(email).collection(
            COLLECTION_PATH
        ).whereEqualTo("liked", true).get().await()
        return result.map { it.id }.toSet()
    }

    suspend fun getAllIdsByUser(email: String): Set<String> {
        val result = store.collection(COLLECTION_PATH).document(email).collection(
            COLLECTION_PATH
        ).get().await()
        return result.map { it.id }.toSet()
    }

    suspend fun deleteAllByUser(email: String) {
        val allDocumentsOfUser = store.collection(COLLECTION_PATH).document(email).collection(
            COLLECTION_PATH
        ).get().await()
        for (documentSnapshot in allDocumentsOfUser.documents) {
            store.collection(COLLECTION_PATH).document(email).collection(
                COLLECTION_PATH
            ).document(documentSnapshot.id).delete().await()
        }
        store.collection(COLLECTION_PATH).document(email).delete()
    }
}
