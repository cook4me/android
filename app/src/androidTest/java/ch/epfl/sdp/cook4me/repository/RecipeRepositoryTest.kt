package ch.epfl.sdp.cook4me.repository

import android.net.Uri
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

private const val COLLECTION_PATH = "recipes"
private const val USER_NAME = "donald.fudging.duck@epfl.ch"

@ExperimentalCoroutinesApi
class RecipeRepositoryTest {
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var store: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        store.firestoreSettings = settings
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        storage = FirebaseStorage.getInstance()
        storage.useEmulator("10.0.2.2", 9199)
        auth = FirebaseAuth.getInstance()
        recipeRepository = RecipeRepository(store, storage, auth)
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, "123456").await()
            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
            for (documentSnapshot in querySnapshot.documents) {
                recipeRepository.delete(documentSnapshot.id)
            }
            auth.signInWithEmailAndPassword(USER_NAME, "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun storeNewRecipes() = runTest {
        val files = withContext(Dispatchers.IO) {
            generateTempFiles(3)
        }
        val urls = files.map { Uri.fromFile(it) }
        val newEntry1 = Recipe(name = "newEntry1", user = USER_NAME)
        val newEntry2 = Recipe(name = "newEntry2", user = USER_NAME)
        recipeRepository.addAndGetId(newEntry1, urls)
        recipeRepository.addAndGetId(newEntry2, urls.drop(1))
        val allRecipes = recipeRepository.getAll<Recipe>()
        assertThat(allRecipes.values, containsInAnyOrder(newEntry1, newEntry2))
        val userFolder = storage.reference.child("images/$USER_NAME/recipes")
        val recipeFolder = userFolder.listAll().await()
        assertThat(recipeFolder.prefixes.count(), `is`(2))
    }

    @Test
    fun deleteRecipe() = runTest {
        val file = withContext(Dispatchers.IO) {
            generateTempFiles(2)
        }
        val urls = file.map{ Uri.fromFile(it) }
        val newEntry1 = Recipe(name = "newEntry1", user = USER_NAME)
        val recipeId = recipeRepository.addAndGetId(newEntry1, urls)
        recipeId?.let{
            runBlocking { recipeRepository.delete(it) }
        }
        val recipes = recipeRepository.getAll<Recipe>()
        assert(recipes.isEmpty())
        val images = storage.reference.child("images/$USER_NAME/recipes").listAll().await()
        assert(images.prefixes.isEmpty())
    }

    @Test
    fun updateExistingRecipeKeepOnlyRecentRecipe() = runTest {
        val entryToBeUpdated = Recipe(name = "entryToBeUpdated")
        recipeRepository.add(entryToBeUpdated)
        val allRecipesBeforeUpdate = recipeRepository.getAll<Recipe>()
        val updatedEntry = entryToBeUpdated.copy(name = "updated")
        recipeRepository.update(allRecipesBeforeUpdate.keys.first(), updatedEntry)
        val allRecipesAfterUpdate = recipeRepository.getAll<Recipe>()
        assertThat(allRecipesAfterUpdate.values, contains(updatedEntry))
        assertThat(allRecipesAfterUpdate.values, not(contains(entryToBeUpdated)))
    }

    @Test
    fun getRecipeById() = runTest {
        recipeRepository.add(Recipe(name = "newEntry1"))
        recipeRepository.add(Recipe(name = "newEntry2"))
        recipeRepository.add(Recipe(name = "newEntry3"))
        val allRecipes = recipeRepository.getAll<Recipe>()
        val actual = recipeRepository.getById<Recipe>(allRecipes.keys.first())
        assertThat(actual, `is`(allRecipes.values.first()))
    }

    private fun generateTempFiles(count: Int): List<File> =
        (0 until count).map {
            val file = File.createTempFile("temp_", "$it")
            file.writeText("temp$it")
            file.deleteOnExit()
            file
        }
}
