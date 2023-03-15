package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_PATH = "recipes"

class RecipeRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun add(value: Recipe) {
        store.collection(COLLECTION_PATH).add(value).await()
    }

    suspend fun getAll(): Map<String, Recipe> {
        val result = store.collection(COLLECTION_PATH).get().await()
        return result.map { it.id }.zip(result.toObjects(Recipe::class.java)).toMap()
    }

    suspend fun getById(id: String) =
        store.collection(COLLECTION_PATH).document(id).get().await()
            .toObject(Recipe::class.java)

    suspend fun update(id: String, value: Recipe) {
        store.collection(COLLECTION_PATH).document(id).set(value).await()
    }
}
