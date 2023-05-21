package ch.epfl.sdp.cook4me.application

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.assertThrowsAsync
import ch.epfl.sdp.cook4me.setupFirebaseAuth
import com.google.firebase.auth.FirebaseAuth
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

private const val USERNAME = "harry.potter@epfl.ch"
private const val PASSWORD = "123456"

private const val VALID_EMAIL = "bababa@epfl.ch"
private const val INVALID_EMAIL = "bababa@epfl.ch"


@ExperimentalCoroutinesApi
 @RunWith(AndroidJUnit4::class)
 class AccountServiceTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val auth: FirebaseAuth = setupFirebaseAuth()
    private val accountService: AccountService = AccountService(auth)

    @Before
    fun setUp() {
        runBlocking {
            auth.createUserWithEmailAndPassword(USERNAME, PASSWORD).await()
        }
    }

    @After
    fun cleanUp() {
        runBlocking {
            auth.signInWithEmailAndPassword(USERNAME, PASSWORD).await()
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun accountServiceSignsOutCurrentUser() = runTest {
        accountService.authenticate(USERNAME, PASSWORD)
        accountService.signOut()
        assertNull(auth.currentUser)
    }

    @Test
    fun accountServiceRegistersValidUser() = runTest {
        accountService.register(VALID_EMAIL, PASSWORD)
        accountService.authenticate(VALID_EMAIL, PASSWORD)
        assertThat(auth.currentUser?.email, `is`(VALID_EMAIL))
        auth.currentUser?.delete()?.await()
    }

    @Test
    fun accountServiceRefusesToRegisterInvalidEmail() = //Don't use runTest, because we already use runBlocking
        assertThrowsAsync {
            accountService.register("mrinvalid", PASSWORD)
        }

    @Test
    fun accountServiceRefusesToRegisterWeakPassword() =
        assertThrowsAsync {
            accountService.register("validemail@epfl.ch", "126")
        }

    @Test
    fun accountServiceRefusesToRegisterExistingUser() {
        assertThrowsAsync {
            accountService.register("validemail@epfl.ch", "123456")
            accountService.register("validemail@epfl.ch", "123456")
        }
        runBlocking {
            accountService.authenticate("validemail@epfl.ch", "123456")
            auth.currentUser?.delete()?.await()
        }
    }

    @Test
    fun accountServicePermitsValidEmail() {
        assertThat(accountService.isValidEmail(VALID_EMAIL), `is`(true))
    }

    @Test
    fun accountServiceDeclinesInvalidEmail() {
        assertThat(accountService.isValidEmail(INVALID_EMAIL), `is`(false))
    }

    @Test
    fun accountServicePermitsSignedUpUser() = runTest {
        accountService.authenticate(USERNAME, PASSWORD)
        assertThat(auth.currentUser?.email, `is`(USERNAME))
    }

    @Test
    fun accountServiceDeclinesInvalidUser() =
        assertThrowsAsync {
            accountService.authenticate("mrinvalid@epfl.ch", "hahaha")
        }

 }
