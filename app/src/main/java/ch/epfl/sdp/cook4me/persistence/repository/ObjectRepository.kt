package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

open class ObjectRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val objectPath: String = ""
) {

    open suspend fun delete(id: String) {
        store.collection(objectPath).document(id).delete().await()
    }

    open suspend fun deleteAll() {
        val querySnapshot = store.collection(objectPath).get().await()
        for (documentSnapshot in querySnapshot.documents) {
            delete(documentSnapshot.id)
        }
    }

    suspend fun <A : Any> update(id: String, value: A) {
        store.collection(objectPath).document(id).set(value).await()
    }
}
