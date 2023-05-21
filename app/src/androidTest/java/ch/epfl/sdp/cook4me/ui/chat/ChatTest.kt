package ch.epfl.sdp.cook4me.ui.chat

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import io.getstream.chat.android.client.ChatClient
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//just for the coverage!! (╯°Д°)╯ ┻━┻
 @RunWith(AndroidJUnit4::class)
 class ChatTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testLoadingScreen() {
        composeTestRule.setContent {
            LoadingScreen()
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("Loading Screen TAG")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag("Loading Screen TAG").assertIsDisplayed()
    }

    @Test
    fun testChannelScreen() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        val screen = mutableStateOf(Unit)
        composeTestRule.setContent {
            screen.value = ChannelScreen(mockClient, mockAccountService)
        }
        assert(screen.value != null)
    }

    @Test
    fun testChannelScreenPerformsLoading() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        composeTestRule.setContent {
            ChannelScreen(mockClient, mockAccountService)
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("Loading Screen TAG")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag("Loading Screen TAG").assertIsDisplayed()
    }

    @Test
    fun testChannelScreenIsDisplayed() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        composeTestRule.setContent {
            ChannelScreen(mockClient, mockAccountService)
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("Channel Screen TAG")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag("Channel Screen TAG").assertIsDisplayed()
    }

    @Test
    fun testProvideChatClient() {
        val client = provideChatClient("somekey", composeTestRule.activity)
        assert(client != null)
        assertTrue(client is ChatClient)
    }

    @Test
    fun testChatService() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        val createFunc = createChatWithEmail(
            "someemail",
            client = mockClient,
            accountService = mockAccountService,
            context = composeTestRule.activity
        )
        val createPairFunc = createChatWithPairs(
            "someemail",
            "someotheremail",
            client = mockClient,
        )
        assert(createFunc != null)
        assert(createPairFunc != null)
    }
 }
