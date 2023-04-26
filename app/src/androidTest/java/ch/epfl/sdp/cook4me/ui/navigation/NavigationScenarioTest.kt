package ch.epfl.sdp.cook4me.ui.navigation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.Cook4MeApp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.permissions.TestPermissionStatusProvider
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.repository.RecipeNoteRepository
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import ch.epfl.sdp.cook4me.ui.onNodeWithStringId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import waitUntilExists

class NavigationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    private lateinit var context: Context

    private val permissionStatusProvider = TestPermissionStatusProvider(
        initialPermissions = mapOf(
            "TestPermission1" to Pair(true, true),
            "TestPermission2" to Pair(true, true)
        )
    )
    private fun getString(id: Int): String {
        return composeTestRule.activity.getString(id)
    }
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        store = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080") // connect to local firestore emulator
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
        store.firestoreSettings = settings
        auth = FirebaseAuth.getInstance()
        val recipe = Recipe(name = "Test recipe 1", cookingTime = "10 min")
        val recipeRepository = RecipeRepository(store)
        val recipeNoteRepo = RecipeNoteRepository(store)
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            recipeRepository.add(recipe)
            val recipeId = recipeRepository.getAll().toList()[0].first
            recipeNoteRepo.addRecipeNote(recipeId, 9)
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }

    @Test
    fun navigatingToTupperwareScreenFromBottomBar() {
        composeTestRule.setContent {
            Cook4MeApp(permissionStatusProvider = permissionStatusProvider)
        }
        composeTestRule.onNodeWithText("nope").assertDoesNotExist()
        composeTestRule.onNodeWithText("Tups").performClick()
        composeTestRule.waitUntilExists(hasText("nope"))
    }

    @Test
    fun navigatingToEventScreen() {
        composeTestRule.setContent {
            Cook4MeApp(permissionStatusProvider = permissionStatusProvider)
        }
        composeTestRule.onNodeWithText("EPFL").assertDoesNotExist()
        composeTestRule.onNodeWithText("Events").performClick()
        composeTestRule.waitUntilExists(hasText("EPFL"))
    }

    @Test
    fun navigatingToRecipes() {
        composeTestRule.setContent {
            Cook4MeApp(permissionStatusProvider = permissionStatusProvider)
        }
        composeTestRule.onNodeWithText("Tups").performClick()
        composeTestRule.onNodeWithText("Top recipes").assertDoesNotExist()
        composeTestRule.onNodeWithText("Recipes").performClick()
        composeTestRule.waitUntilExists(hasText("Top recipes"))
    }

    @Test
    fun navigateToCreateRecipe() {
        composeTestRule.setContent {
            Cook4MeApp(permissionStatusProvider = permissionStatusProvider)
        }
        composeTestRule.onNodeWithStringId(R.string.RecipeCreationRecipeTitle).assertDoesNotExist()
        composeTestRule.onNodeWithText("Recipes").performClick()
        composeTestRule.onNodeWithText("Create a new Recipe").performClick()
        composeTestRule.waitUntilExists(hasText("Recipe name"))
    }

    @Test
    fun navigateToCreateEvent() {
        composeTestRule.setContent {
            Cook4MeApp(permissionStatusProvider = permissionStatusProvider)
        }
        composeTestRule.onNodeWithText("Name of the event?").assertDoesNotExist()
        composeTestRule.onNodeWithText("Events").performClick()
        composeTestRule.onNodeWithText("Create a new Event").performClick()
        composeTestRule.waitUntilExists(hasText("Name of the event?"))
    }

    @Test
    fun navigateToCreateTupperware() {
        composeTestRule.setContent {
            Cook4MeApp(permissionStatusProvider = permissionStatusProvider)
        }
        composeTestRule.onNodeWithText("nope").assertDoesNotExist()
        composeTestRule.onNodeWithText("Tups").performClick()
        composeTestRule.waitUntilExists(hasText("nope"))
        composeTestRule.onNodeWithText("Create a new Tupperware").performClick()
        composeTestRule.waitUntilExists(hasText("Description"))
    }
}
