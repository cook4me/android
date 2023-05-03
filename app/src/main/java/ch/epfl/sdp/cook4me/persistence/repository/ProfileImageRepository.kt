package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

private const val COLLECTION_PATH = "profileImage"

class ProfileImageRepository(
    store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) :
    ObjectRepository(store, COLLECTION_PATH) {
    suspend fun add(image: Uri) {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val storageRef = storage.reference

        val ref = storageRef.child("/images/$email/profileImage/${UUID.randomUUID()}")
        ref.putFile(image).await()
    }

    suspend fun get(): Uri {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val storageRef = storage.reference
        val images = storageRef.child("/images/$email/profileImage").listAll().await()

        // Check if the items list is not empty
        if (images.items.isNotEmpty()) {
            return images.items[0].downloadUrl.await()
        } else {
            // Handle the case when there are no items in the list
            return Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/ic_user")
        }
    }

    suspend fun delete() {
        auth.currentUser?.email?.let { email ->
            val images = storage.reference
                .child("/images/$email/profileImage")
                .listAll()
                .await()
            images.items.forEach {
                it.delete().await()
            }
        }
    }
}
