package ch.epfl.sdp.cook4me.repository

import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test

private const val COLLECTION_PATH = "recipes"

@ExperimentalCoroutinesApi
class RecipeRepositoryTest {
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var store: FirebaseFirestore

    @Before
    fun setUp() {
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        recipeRepository = RecipeRepository(store)
    }

    @After
    fun cleanUp() {
        runBlocking {
            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
            for (documentSnapshot in querySnapshot.documents) {
                store.collection(COLLECTION_PATH).document(documentSnapshot.id).delete().await()
            }
        }
    }

    @Test
    fun storeNewRecipes() = runTest {
        val newEntry1 = Recipe(name = "newEntry1")
        val newEntry2 = Recipe(name = "newEntry2")
        recipeRepository.add(newEntry1)
        recipeRepository.add(newEntry2)
        val allRecipes = recipeRepository.getAll()
        assertThat(allRecipes.values, containsInAnyOrder(newEntry1, newEntry2))
    }

    @Test
    fun updateExistingRecipeKeepOnlyRecentRecipe() = runTest {
        val entryToBeUpdated = Recipe(name = "entryToBeUpdated")
        recipeRepository.add(entryToBeUpdated)
        val allRecipesBeforeUpdate = recipeRepository.getAll()
        val updatedEntry = entryToBeUpdated.copy(name = "updated")
        recipeRepository.update(allRecipesBeforeUpdate.keys.first(), updatedEntry)
        val allRecipesAfterUpdate = recipeRepository.getAll()
        assertThat(allRecipesAfterUpdate.values, contains(updatedEntry))
        assertThat(allRecipesAfterUpdate.values, not(contains(entryToBeUpdated)))
    }

    @Test
    fun getRecipeById() = runTest {
        recipeRepository.add(Recipe(name = "newEntry1"))
        recipeRepository.add(Recipe(name = "newEntry2"))
        recipeRepository.add(Recipe(name = "newEntry3"))
        val allRecipes = recipeRepository.getAll()
        val actual = recipeRepository.getById(allRecipes.keys.first())
        assertThat(actual, `is`(allRecipes.values.first()))
    }
}
