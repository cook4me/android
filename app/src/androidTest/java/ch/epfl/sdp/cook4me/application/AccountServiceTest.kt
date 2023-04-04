package ch.epfl.sdp.cook4me.application

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertThrowsAsync
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AccountServiceTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var auth: FirebaseAuth
    private lateinit var accountService: AccountService
    private val invalidEmail = "invalidemail"
    private val validEmail = "bababa@epfl.ch"

    @Before
    fun setUp() {
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        auth = FirebaseAuth.getInstance()
        runBlocking {
            auth.createUserWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
        }
        accountService = AccountService(auth)
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword("harry.potter@epfl.ch", "123456").await()
            auth.currentUser?.delete()
        }
    }
    @Test
    fun accountServiceSignsOutCurrentUser() = runTest {
        accountService.authenticate("harry.potter@epfl.ch", "123456")
        val signOutResult = accountService.signOut()
        assertNull(auth.currentUser)
        assertEquals(signOutResult.getOrNull(), "Sign out successful")
    }
    @Test
    fun accountServiceRegistersValidUser() = runTest {
        accountService.register("validemail@epfl.ch", "123456")
        accountService.authenticate("validemail@epfl.ch", "123456")
        assertThat(auth.currentUser?.email, `is`("validemail@epfl.ch"))
        auth.currentUser?.delete()
    }

    @Test
    fun accountServiceRefusesToRegisterInvalidEmail() = runTest {
        assertThrowsAsync {
            accountService.register("mrinvalid", "123456")
        }
    }
    @Test
    fun accountServiceRefusesToRegisterWeakPassword() = runTest {
        assertThrowsAsync {
            accountService.register("validemail@epfl.ch", "126")
        }
    }

    @Test
    fun accountServiceRefusesToRegisterExistingUser() = runTest {
        accountService.register("validemail@epfl.ch", "123456")
        assertThrowsAsync {
            accountService.register("validemail@epfl.ch", "123456")
        }
        accountService.authenticate("validemail@epfl.ch", "123456")
        auth.currentUser?.delete()
    }

    @Test
    fun accountServicePermitsValidEmail() {
        assertThat(accountService.isValidEmail(validEmail), `is`(true))
    }

    @Test
    fun accountServiceDeclinesInvalidEmail() {
        assertThat(accountService.isValidEmail(invalidEmail), `is`(false))
    }

    @Test
    fun accountServicePermitsSignedUpUser() = runTest {
        accountService.authenticate("harry.potter@epfl.ch", "123456")
        assertThat(auth.currentUser?.email, `is`("harry.potter@epfl.ch"))
    }

    @Test
    fun accountServiceDeclinesInvalidUser() = runTest {
        assertThrowsAsync {
            accountService.authenticate("mrinvalid@epfl.ch", "hahaha")
        }
    }
}
