package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.FirestoreTupperware
import ch.epfl.sdp.cook4me.persistence.model.TupperwareWithImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import android.net.Uri

private const val COLLECTION_PATH = "tupperwares"
private const val BASE_PATH = "/images/tupperwares"
private const val ONE_MEGABYTE: Long = 1024 * 1024

class TupperwareRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) :
    ObjectRepository(store, COLLECTION_PATH) {

    suspend fun add(title: String, description: String, image: Uri?) {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val tupperwareId = super.addAndGetId(FirestoreTupperware(title, description, email))
        val storageRef = storage.reference
        image?.let {
            val ref =
                storageRef.child("$BASE_PATH/$tupperwareId")
            ref.putFile(it).await()
        }
    }

    suspend fun getWithImageById(id: String): TupperwareWithImage? {
        val tupperwareInfo = super.getById<FirestoreTupperware>(id)
        return tupperwareInfo?.let {
            val storageRef = storage.reference
            val ref = storageRef.child("$BASE_PATH/$id")
            val bytes = ref.getBytes(ONE_MEGABYTE).await()
            TupperwareWithImage(
                title = it.title,
                description = it.description,
                user = it.user,
                image = bytes
            )
        }
    }

    suspend fun getAllIdsByUser(email: String): Set<String> {
        val result = store.collection(COLLECTION_PATH).whereEqualTo("user", email).get().await()
        return result.map { it.id }.toSet()
    }

    suspend fun getAllIdsNotByUser(email: String): Set<String> {
        val result = store.collection(COLLECTION_PATH).whereNotEqualTo("user", email).get().await()
        return result.map { it.id }.toSet()
    }

    override suspend fun delete(id: String) {
        super.delete(id)
        auth.currentUser?.email?.let { email ->
            val images = storage.reference
                .child("/images/$email/tupperwares/$id")
                .listAll()
                .await()
            images.items.forEach {
                it.delete().await()
            }
        }
    }
}
