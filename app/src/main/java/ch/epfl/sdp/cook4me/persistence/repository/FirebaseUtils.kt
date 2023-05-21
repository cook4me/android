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

suspend inline fun <reified A : Any> FirebaseFirestore.getFirstObjectByFieldValueFromCollection(
    field: String,
    value: String,
    collectionPath: String
): A {
    val result = collection(collectionPath).whereEqualTo(field, value).get().await()
    return result.map { it.toObject(A::class.java) }.first()
}

suspend inline fun <reified A : Any> FirebaseFirestore.getObjectByIdFromCollection(id: String, collectionPath: String) =
    collection(collectionPath).document(id).get().await()
        .toObject(A::class.java)

suspend fun FirebaseFirestore.deleteByIdFromCollection(id: String, collectionPath: String) {
    collection(collectionPath).document(id).delete().await()
}

suspend fun FirebaseFirestore.deleteDocumentsFromCollection(collectionPath: String) {
    val querySnapshot = collection(collectionPath).get().await()
    for (documentSnapshot in querySnapshot.documents) {
        deleteByIdFromCollection(documentSnapshot.id, collectionPath)
    }
}

suspend fun <A : Any> FirebaseFirestore.updateObjectInCollection(id: String, value: A, collectionPath: String) {
    collection(collectionPath).document(id).set(value).await()
}
