package ch.epfl.sdp.cook4me.persistence.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

open class ObjectRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val objectPath: String = ""
) {

    suspend fun <A : Any> add(value: A) {
        store.collection(objectPath).add(value).await()
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

    /*
    * A function to query from firestore with given attribute.
    * @param field: the field to query. e.g.: "id", "name", "description"
    * @param query: the query to search for. e.g. "darth.vadar" (for the field of name)
    * returns a list of document snapshots.
    *
    * I didn't return a Map because it does not apply to the
    * Event class (reason see below) and I don't want to introduce anything about Event class here,
    * since this repository is supposed to be generic.
    * */

    /*
    * Notes: Firebase could not serialize to java.untl.Calender, I will add an constructor in Event.kt
    * to construct an Event object from a map.
    * */
    suspend fun <A : Any> getWithGivenField(field: String, query: Any): List<DocumentSnapshot> {
        val result = store.collection(objectPath).whereEqualTo(field, query).get().await()
        return result.documents
    }
}
