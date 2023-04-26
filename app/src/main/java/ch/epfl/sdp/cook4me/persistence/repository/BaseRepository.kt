package ch.epfl.sdp.cook4me.persistence.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

@Suppress("UnnecessaryAbstractClass")
// This class shouldn't be used directly. We enforce this with the abstract keyword
abstract class BaseRepository(
    private val store: FirebaseFirestore,
    private val objectPath: String
) {

    suspend fun <A : Any> add(value: A) {
        try {
            store.collection(objectPath).add(value).await()
        } catch (e: FirebaseFirestoreException) {
            Log.e("BaseRepository", "Error adding element: ${e.message}")
        }
    }

    suspend fun <A : Any> getAll(ofClass: Class<A>): Map<String, A> {
        val result = store.collection(objectPath).get().await()
        return result.map { it.id }.zip(result.toObjects(ofClass)).toMap()
    }

    suspend inline fun <reified A : Any> getAll(): Map<String, A> = getAll(A::class.java)

    suspend inline fun <reified A : Any> getById(id: String) =
        getById(id, A::class.java)

    suspend fun <A : Any> getById(id: String, ofClass: Class<A>) =
        store.collection(objectPath).document(id).get().await()
            .toObject(ofClass)

    suspend fun <A : Any> update(id: String, value: A) {
        store.collection(objectPath).document(id).set(value).await()
    }
}
