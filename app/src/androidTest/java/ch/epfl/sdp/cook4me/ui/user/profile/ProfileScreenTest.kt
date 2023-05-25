package ch.epfl.sdp.cook4me.ui.user.profile
//
//import android.net.Uri
//import androidx.activity.ComponentActivity
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import ch.epfl.sdp.cook4me.R
//import ch.epfl.sdp.cook4me.application.AccountService
//import ch.epfl.sdp.cook4me.persistence.model.Profile
//import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
//import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
//import ch.epfl.sdp.cook4me.setupFirebaseAuth
//import ch.epfl.sdp.cook4me.setupFirebaseStorage
//import ch.epfl.sdp.cook4me.setupFirestore
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.tasks.await
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.runner.RunWith
//
//private const val USERNAME = "donald.duck@epfl.ch"
//private const val PASSWORD = "123456"
//
//@RunWith(AndroidJUnit4::class)
//class ProfileScreenTest {
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private val auth: FirebaseAuth = setupFirebaseAuth()
//    private val store: FirebaseFirestore = setupFirestore()
//    private val storage: FirebaseStorage = setupFirebaseStorage()
//    private val account: AccountService = AccountService(auth)
//    private val repository: ProfileRepository = ProfileRepository(store)
//    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository(storage, auth)
//    private val profileImage = Uri.parse("android.resource://ch.epfl.sdp.cook4me/drawable/" + R.drawable.ic_user)
//    private val user = Profile(
//        email = USERNAME,
//        allergies = "Hazelnut",
//        bio = "I am a duck",
//        favoriteDish = "Spaghetti",
//    )
//
//    @Before
//    fun setUp() {
//        runBlocking {
//            auth.createUserWithEmailAndPassword(USERNAME, PASSWORD).await()
//            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
//            repository.add(user)
//            profileImageRepository.add(profileImage)
//        }
//    }
//
//    @After
//    fun cleanUp() {
//        runBlocking {
//            // delete the user from the database
//            repository.deleteAll()
//            profileImageRepository.deleteImageForCurrentUser()
//            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
//            auth.currentUser?.delete()?.await()
//        }
//    }

    // TODO: Fix me
//    @Test
//    fun profileInfoIsDisplayed() {
//        composeTestRule.setContent {
//            ProfileScreen(
//                userId = USERNAME,
//                profileRepository = repository,
//                profileImageRepository = profileImageRepository,
//                accountService = account,
//            )
//        }
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//         composeTestRule.onAllNodesWithText(user.allergies).fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.onNodeWithText(user.allergies).assertExists()
//        composeTestRule.onNodeWithText(user.bio).assertExists()
//        composeTestRule.onNodeWithText(user.favoriteDish).assertExists()
//    }
}
