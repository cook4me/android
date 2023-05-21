package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import ch.epfl.sdp.cook4me.persistence.model.TupperwareWithImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "tupperwares"
private const val STORAGE_BASE_PATH = "/images/tupperwares"
private const val ONE_MEGABYTE: Long = 1024 * 1024

class TupperwareRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun add(title: String, description: String, image: Uri): String {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val id = store.addObjectToCollection(Tupperware(title, description, email), COLLECTION_PATH)
        getImageReference(id).putFile(image).await()
        return id
    }

    suspend fun getById(id: String): Tupperware? =
        store.getObjectByIdFromCollection(id, COLLECTION_PATH)

    suspend fun getWithImageById(id: String): TupperwareWithImage? {
        val tupperwareInfo = getById(id)
        return tupperwareInfo?.let {
            val bytes = getImageReference(id).getBytes(ONE_MEGABYTE).await()
            TupperwareWithImage(
                title = it.title,
                description = it.description,
                user = it.user,
                image = bytes
            )
        }
    }

    suspend fun getAllTupperwareIdsAddedByUser(email: String): Set<String> {
        val result = store.collection(COLLECTION_PATH).whereEqualTo("user", email).get().await()
        return result.map { it.id }.toSet()
    }

    suspend fun getAllTupperwareIdsNotAddedByUser(email: String): Set<String> {
        val result = store.collection(COLLECTION_PATH).whereNotEqualTo("user", email).get().await()
        return result.map { it.id }.toSet()
    }

    suspend fun delete(id: String) {
        store.deleteByIdFromCollection(id, COLLECTION_PATH)
        getImageReference(id).delete().await()
    }

    suspend fun deleteAll() {
        val ids = store.getAllObjectsFromCollection<Tupperware>(COLLECTION_PATH).keys
        ids.forEach {
            delete(it)
        }
    }

    private fun getImageReference(id: String) = storage.reference
        .child("$STORAGE_BASE_PATH/$id")
}
