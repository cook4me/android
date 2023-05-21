package ch.epfl.sdp.cook4me.persistence.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

open class ObjectRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val objectPath: String = ""
) {
    suspend fun <A : Any> add(value: A): String {
        val documentRef = store.collection(objectPath).add(value).await()
        return documentRef.id
    }

    open suspend fun delete(id: String) {
        store.collection(objectPath).document(id).delete().await()
    }

    open suspend fun deleteAll() {
        val querySnapshot = store.collection(objectPath).get().await()
        for (documentSnapshot in querySnapshot.documents) {
            delete(documentSnapshot.id)
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

    /*
    * Usage: In your service:
    * Initialize the repository;
    * suspend fun getWithGivenField(field: String, query: Any): Map<String, DataClass> {
        val result = objectRepository.getWithGivenField<DataClass>(field, query)
        return result.map { it.id to it.toObject(DataClass::class.java) }.toMap()
      }
     * This will return a map of id to DataClass object.
     * If your dataclass sadly is not able to be serialized by firebase, add
         a secondary constructor to your dataclass to construct it from a map.
     * Want a demo? See EventFormService.kt and Event.kt
    * */
    suspend fun <A : Any> getWithGivenField(field: String, query: Any): List<DocumentSnapshot> =
        try {
            val result = store.collection(objectPath).whereEqualTo(field, query).get().await()
            result.documents
        } catch (e: FirebaseFirestoreException) {
            // handling the exception, if anything goes wrong, return an empty list
            Log.e("ObjectRepo", "Error querying documents: ${e.message}")
            emptyList()
        }

    /*
    * IMPORTANT: Needed as long as the event class uses a Calendar
    * This function is designed with cope with the Event data class, though it's still generic.
    * Instead of calling .toObject(), it simply returns the DocumentSnapshot, and we deal with
    * it later.
    * If nothing is found, it returns null.
    * */
    suspend fun <A : Any> getWithId(id: String): DocumentSnapshot? =
        try {
            val result = store.collection(objectPath).document(id).get().await()
            result
        } catch (e: FirebaseFirestoreException) {
            Log.e("ObjectRepo", "Error querying documents: ${e.message}")
            null
        }
}

private const val MAX_RETRIEVED_DOCS = 20.toLong()

open class ObjectCollectionRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val objectPath: String = ""
) {
    /*
    * Retrieves all documents in a collection
    * If nothing is found, it returns empty list
    * */
    suspend fun <A : Any> retrieveAllDocuments(): List<DocumentSnapshot> =
        try {
            val result = store.collection(objectPath).limit(MAX_RETRIEVED_DOCS).get().await()
            result.documents
        } catch (e: FirebaseFirestoreException) {
            Log.e("ObjectRepo", "Error querying documents: ${e.message}")
            emptyList<DocumentSnapshot>()
        }
}
