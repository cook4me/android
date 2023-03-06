package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

open class BaseRepository<T : Any>(
    protected val store: FirebaseFirestore = Firebase.firestore,
    protected val collectionPath: String,
    private val modelClass: Class<T>
) {

    suspend fun add(value: T) {
        store.collection(collectionPath).add(value).await()
    }

    suspend fun getAll(): Map<String, T> {
        val result = store.collection(collectionPath).get().await()
        return result.map { it.id }.zip(result.toObjects(modelClass)).toMap()
    }

    suspend fun getById(id: String): T? {
        return store.collection(collectionPath).document(id).get().await()
            .toObject(modelClass)
    }

    suspend fun update(id: String, value: T) {
        store.collection(collectionPath).document(id).set(value).await()
    }
}