package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.generateTempFiles
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import ch.epfl.sdp.cook4me.setupFirebaseStorage
import ch.epfl.sdp.cook4me.setupFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
import org.junit.runner.RunWith

private const val USER_NAME = "donald.fudging.duck@epfl.ch"
private const val PASSWORD = "123456"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RecipeRepositoryTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val recipeRepository: RecipeRepository = RecipeRepository(store, storage, auth)

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            recipeRepository.deleteAll()
            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun storeNewRecipes() = runTest {
        val files = generateTempFiles(3)
        val urls = files.map { Uri.fromFile(it) }
        val newEntry1 = Recipe(name = "newEntry1", user = USER_NAME)
        val newEntry2 = Recipe(name = "newEntry2", user = USER_NAME)
        recipeRepository.add(newEntry1, urls[0])
        recipeRepository.add(newEntry2, urls[1])
        val allRecipes = recipeRepository.getAll()
        assertThat(allRecipes.values, containsInAnyOrder(newEntry1, newEntry2))
        val folderContent = getUserFolder().listAll().await()
        assertThat(folderContent.prefixes.count(), `is`(2))
    }

    @Test
    fun deleteRecipe() = runTest {
        val file = generateTempFiles(1)
        val url = Uri.fromFile(file.first())
        val newEntry1 = Recipe(name = "newEntry1", user = USER_NAME)
        val id = recipeRepository.add(newEntry1, url)
        recipeRepository.delete(id ?: error("should never happen"))
        val recipes = recipeRepository.getAll()
        assertThat(recipes.isEmpty(), `is`(true))
        val images = getUserFolder().listAll().await()
        assertThat(images.prefixes.isEmpty(), `is`(true))
    }

    @Test
    fun getRecipeById() = runTest {
        val file = generateTempFiles(1)
        val url = Uri.fromFile(file.first())
        recipeRepository.add(Recipe(name = "newEntry1"), url)
        recipeRepository.add(Recipe(name = "newEntry2"), url)
        recipeRepository.add(Recipe(name = "newEntry3"), url)
        val allRecipes = recipeRepository.getAll()
        val actual = recipeRepository.getById(allRecipes.keys.first())
        assertThat(actual, `is`(allRecipes.values.first()))
    }

    private fun getUserFolder() = storage.reference.child("images/$USER_NAME/recipes")
}
