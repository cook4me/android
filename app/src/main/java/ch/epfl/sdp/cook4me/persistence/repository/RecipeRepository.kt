package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import android.util.Log
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val COLLECTION_PATH = "recipes"
private const val STORAGE_BASE_PATH = "/images/recipes"
private const val ONE_MEGABYTE: Long = 1024 * 1024

class RecipeRepository(
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun add(recipe: Recipe, image: Uri?): String {
        val email = auth.currentUser?.email
        checkNotNull(email)
        val id = store.addObjectToCollection(recipe.copy(user = email), COLLECTION_PATH)
        if (image != null) {
            getImageReference(id).putFile(image).await()
        }
        return id
    }

    suspend fun getById(id: String): Recipe? =
        store.getObjectByIdFromCollection(id, COLLECTION_PATH)

    suspend fun getAll(useOnlyCache: Boolean = false) =
        store.getAllObjectsFromCollection<Recipe>(COLLECTION_PATH, useOnlyCache = useOnlyCache)

    suspend fun getRecipeImage(id: String) =
        try {
            withContext(Dispatchers.IO) {
                val bytes = getImageReference(id).getBytes(ONE_MEGABYTE).await()
                bytes
            }
        } catch (e: StorageException) {
            Log.e("Recipe Image", e.toString())
            null
        }

    suspend fun delete(id: String) {
        store.deleteByIdFromCollection(id, COLLECTION_PATH)
        getImageReference(id).delete().await()
    }

    suspend fun deleteAll() {
        val ids = store.getAllObjectsFromCollection<Recipe>(COLLECTION_PATH).keys
        ids.forEach {
            delete(it)
        }
    }

    private fun getImageReference(id: String) = storage.reference
        .child("$STORAGE_BASE_PATH/$id")
}
