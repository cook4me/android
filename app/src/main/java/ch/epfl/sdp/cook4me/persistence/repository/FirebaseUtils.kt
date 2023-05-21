package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun <A : Any> FirebaseFirestore.addObjectToCollection(
    value: A,
    collectionPath: String
): String {
    val documentRef = collection(collectionPath).add(value).await()
    return documentRef.id
}

suspend inline fun <reified A : Any> FirebaseFirestore.getAllObjectsFromCollection(
    collectionPath: String
): Map<String, A> {
    val result = collection(collectionPath).get().await()
    return result.map { it.id }.zip(result.toObjects(A::class.java)).toMap()
}

suspend inline fun <reified A : Any> FirebaseFirestore.getFirstObjectByFieldValue(
    field: String,
    value: String,
    collectionPath: String
): A {
    val result = collection(collectionPath).whereEqualTo(field, value).get().await()
    return result.map { it.toObject(A::class.java) }.first()
}
