package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun <T : Any> FirebaseFirestore.add(collectionPath: String, value: T) {
    collection(collectionPath).add(value).await()
}

suspend inline fun <reified T : Any> FirebaseFirestore.getAll(collectionPath: String): Map<String, T> {
    val result = collection(collectionPath).get().await()
    return result.map { it.id }.zip(result.toObjects(T::class.java)).toMap()
}

suspend inline fun <reified T : Any> FirebaseFirestore.getById(
    collectionPath: String,
    id: String
): T? =
    collection(collectionPath).document(id).get().await()
        .toObject(T::class.java)

suspend fun <T : Any> FirebaseFirestore.update(collectionPath: String, id: String, value: T) {
    collection(collectionPath).document(id).set(value).await()
}