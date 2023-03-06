package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val COLLECTION_PATH = "recipes"

class RecipeRepository(
    private val store: FirebaseFirestore = Firebase.firestore,
) {
    suspend fun add(value: Recipe) = store.add(COLLECTION_PATH, value)

    suspend fun getAll() = store.getAll<Recipe>(COLLECTION_PATH)

    suspend fun getById(id: String) = store.getById<Recipe>(COLLECTION_PATH, id)

    suspend fun update(id: String, value: Recipe) = store.update(COLLECTION_PATH, id, value)
}
