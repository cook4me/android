package ch.epfl.sdp.cook4me.repository

import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.not

// private const val COLLECTION_PATH = "recipes"
// private const val USER_NAME = "donald.fudging.duck@epfl.ch"
// private const val PASSWORD = "123456"
//
// @ExperimentalCoroutinesApi
// class RecipeRepositoryTest {
//    private lateinit var recipeRepository: RecipeRepository
//    private lateinit var store: FirebaseFirestore
//    private lateinit var storage: FirebaseStorage
//    private lateinit var auth: FirebaseAuth

//    @Before
//    fun setUp() {
//        store = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setHost("10.0.2.2:8080") // connect to local firestore emulator
//            .setSslEnabled(false)
//            .setPersistenceEnabled(false)
//            .build()
//        store.firestoreSettings = settings
//        store.firestoreSettings = settings
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
//        storage = FirebaseStorage.getInstance()
//        storage.useEmulator("10.0.2.2", 9199)
//        auth = FirebaseAuth.getInstance()
//        recipeRepository = RecipeRepository(store, storage, auth)
//        runBlocking {
//            auth.createUserWithEmailAndPassword(USER_NAME, PASSWORD).await()
//            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
//        }
//    }

//    @After
//    fun cleanUp() {
//        runBlocking {
//            val querySnapshot = store.collection(COLLECTION_PATH).get().await()
//            for (documentSnapshot in querySnapshot.documents) {
//                recipeRepository.delete(documentSnapshot.id)
//            }
//            auth.signInWithEmailAndPassword(USER_NAME, PASSWORD).await()
//            auth.currentUser?.delete()
//        }
//    }

//    @Test
//    fun storeNewRecipes() = runTest {
//        val files = withContext(Dispatchers.IO) {
//            generateTempFiles(3)
//        }
//        val urls = files.map { Uri.fromFile(it) }
//        val newEntry1 = Recipe(name = "newEntry1", user = USER_NAME)
//        val newEntry2 = Recipe(name = "newEntry2", user = USER_NAME)
//        recipeRepository.add(newEntry1, urls)
//        recipeRepository.add(newEntry2, urls.drop(1))
//        val allRecipes = recipeRepository.getAll<Recipe>()
//        assertThat(allRecipes.values, containsInAnyOrder(newEntry1, newEntry2))
//        val userFolder = storage.reference.child("images/$USER_NAME/recipes")
//        val recipeFolder = userFolder.listAll().await()
//        assertThat(recipeFolder.prefixes.count(), `is`(2))
//    }

//    @Test
//    fun deleteRecipe() = runTest {
//        val file = withContext(Dispatchers.IO) {
//            generateTempFiles(2)
//        }
//        val urls = file.map { Uri.fromFile(it) }
//        val newEntry1 = Recipe(name = "newEntry1", user = USER_NAME)
//        recipeRepository.add(newEntry1, urls)
//        val recipeId = recipeRepository
//            .getWithGivenField<Recipe>("name", newEntry1.name).first().id
//        runBlocking { recipeRepository.delete(recipeId) }
//        val recipes = recipeRepository.getAll<Recipe>()
//        assert(recipes.isEmpty())
//        val images = storage.reference.child("images/$USER_NAME/recipes").listAll().await()
//        assert(images.prefixes.isEmpty())
//    }

//    @Test
//    fun updateExistingRecipeKeepOnlyRecentRecipe() = runTest {
//        val entryToBeUpdated = Recipe(name = "entryToBeUpdated")
//        recipeRepository.add(entryToBeUpdated)
//        val allRecipesBeforeUpdate = recipeRepository.getAll<Recipe>()
//        val updatedEntry = entryToBeUpdated.copy(name = "updated")
//        recipeRepository.update(allRecipesBeforeUpdate.keys.first(), updatedEntry)
//        val allRecipesAfterUpdate = recipeRepository.getAll<Recipe>()
//        assertThat(allRecipesAfterUpdate.values, contains(updatedEntry))
//        assertThat(allRecipesAfterUpdate.values, not(contains(entryToBeUpdated)))
//    }

//    @Test
//    fun getRecipeByName() = runTest {
//        val expectedRecipe = Recipe(name = "newEntry1", difficulty = "Hard")
//        recipeRepository.add(expectedRecipe)
//        recipeRepository.add(Recipe(name = "newEntry2"))
//        recipeRepository.add(Recipe(name = "newEntry3"))
//        val actual = recipeRepository.getWithGivenField<Recipe>("name", "newEntry1")
//            .map { it.toObject(Recipe::class.java) }.first()
//        assertThat(actual, `is`(expectedRecipe))
//    }

//    @Test
//    fun getRecipeById() = runTest {
//        recipeRepository.add(Recipe(name = "newEntry1"))
//        recipeRepository.add(Recipe(name = "newEntry2"))
//        recipeRepository.add(Recipe(name = "newEntry3"))
//        val allRecipes = recipeRepository.getAll<Recipe>()
//        val actual = recipeRepository.getById<Recipe>(allRecipes.keys.first())
//        assertThat(actual, `is`(allRecipes.values.first()))
//    }

//    private fun generateTempFiles(count: Int): List<File> =
//        (0 until count).map {
//            val file = File.createTempFile("temp_", "$it")
//            file.writeText("temp$it")
//            file.deleteOnExit()
//            file
//        }
// }
