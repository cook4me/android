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
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

private const val USER = "donald.fudging.duck@epfl.ch"
private const val PASSWORD = "123456"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RecipeRepositoryTest {
    private val store: FirebaseFirestore = setupFirestore()
    private val storage: FirebaseStorage = setupFirebaseStorage()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val recipeRepo: RecipeRepository =
        RecipeRepository(store, storage, auth)

    @Before
    fun setup() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USER, PASSWORD).await()
            auth.signInWithEmailAndPassword(USER, PASSWORD).await()
        }
    }

    @After
    fun cleanup() {
        runBlocking {
            recipeRepo.deleteAll()
            auth.signInWithEmailAndPassword(USER, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun storeAndGetNewRecipeTest() = runTest {
        val files = generateTempFiles(2)
        val ids = recipeRepo.addMultipleTestRecipes(files)
        ids.zip(files).forEachIndexed { i, data ->
            val actualRecipe = recipeRepo.getById(data.first)
            val actualRecipeImage = recipeRepo.getRecipeImage(data.first)
            assertThat(actualRecipe, `is`(Matchers.notNullValue()))
            assertThat(actualRecipeImage, `is`(Matchers.notNullValue()))
            actualRecipe?.let {
                assertThat(it.name, `is`("$i"))
                assertThat(it.user, `is`(auth.currentUser?.email))
            }
            actualRecipeImage?.let {
                assertThat(it, `is`(data.second.readBytes()))
            }
        }
    }

    private suspend fun RecipeRepository.addMultipleTestRecipes(files: List<File>) =
        files.mapIndexed { i, file ->
            add(
                Recipe(name = i.toString()),
                Uri.fromFile(file)
            )
        }
}
