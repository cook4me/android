package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Tupperware
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val COLLECTION_PATH = "tupperwares"

class TupperwareRepository(
    store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) :
    ObjectRepository(store, COLLECTION_PATH) {
    suspend fun add(title: String, description: String, images: List<Uri>) {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val tupperwareId = super.addAndGetId(Tupperware(title, description, email))
        val storageRef = storage.reference
        images.forEach { path ->
            val ref =
                storageRef.child("/images/$email/tupperwares/$tupperwareId/${UUID.randomUUID()}")
            ref.putFile(path).await()
        }
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
