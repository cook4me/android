import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.espresso.matcher.ViewMatchers
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

val testProfile = Profile(
    email = "jean.valejean@epfl.ch",
    name = "Jean Valejean",
    allergies = "reading and writing",
    bio = "I am miserable!",
    favoriteDish = "Bread"
)
object RepositoryFiller {
    suspend fun setUpUser(
        profile: Profile,
        auth: FirebaseAuth = FirebaseAuth.getInstance(),
        store: FirebaseFirestore
    ) {
        val profileRepository = ProfileRepository(store)
        profileRepository.add(profile)
        auth.createUserWithEmailAndPassword(profile.email, "123456").await()
        auth.signInWithEmailAndPassword(profile.email, "123456").await()
    }

    suspend fun cleanUpUser(
        profile: Profile,
        auth: FirebaseAuth = FirebaseAuth.getInstance(),
        store: FirebaseFirestore
    ) {
        val profileRepository = ProfileRepository(store)
        profileRepository.delete(profile.email)
        auth.signInWithEmailAndPassword(profile.email, "123456")
        auth.currentUser?.delete()?.await()
    }
}

fun assertThrowsAsync(f: suspend () -> Unit) {
    try {
        runBlocking {
            f()
        }
    } catch (
        e: Exception
    ) {
        return
    }
    throw AssertionError("no exception was thrown")
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 10_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
    }
}

// super hacky way to wait for AsyncImage to be displayed but seems to work
// should be called with assertIsDisplayed as it doesn't do the exhaustive checks
fun ComposeContentTestRule.waitUntilDisplayed(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 2_000L
) {
    this.waitUntil(timeoutMillis) {
        // code taken from assertIsDisplayed()

        val node = this.onNode(matcher).fetchSemanticsNode()
        var returnValue = true

        (node.root as? ViewRootForTest)?.let {
            if (!ViewMatchers.isDisplayed().matches(it.view)) {
                returnValue = false
            }
        }
        val globalRect = node.boundsInWindow
        // checks if node has zero area, I think
        returnValue && (globalRect.width > 0f && globalRect.height > 0f)
    }
}
