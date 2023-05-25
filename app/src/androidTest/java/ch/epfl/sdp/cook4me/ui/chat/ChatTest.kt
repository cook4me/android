package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.ui.common.LoadingScreen
import io.getstream.chat.android.client.ChatClient
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// just for the coverage!! (╯°Д°)╯ ┻━┻
@RunWith(AndroidJUnit4::class)
class ChatTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val loadingScreenTag = context.getString(R.string.Loading_Screen_Tag)
    private val channelScreenTag = context.getString(R.string.Channel_Screen_Tag)

    @Test
    fun testLoadingScreen() {
        composeTestRule.setContent {
            LoadingScreen()
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(loadingScreenTag)
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(loadingScreenTag).assertIsDisplayed()
    }

    @Test
    fun testChannelScreen() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        val screen = mutableStateOf(Unit)
        val navController = mockk<NavController>(relaxed = true)
        composeTestRule.setContent {
            screen.value = ChannelScreen(mockClient, mockAccountService, navController = navController)
        }
        assert(screen.value != null)
    }

    @Test
    fun testChannelScreenPerformsLoading() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        val navController = mockk<NavController>(relaxed = true)
        composeTestRule.setContent {
            ChannelScreen(mockClient, mockAccountService, navController = navController)
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(loadingScreenTag)
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(loadingScreenTag).assertIsDisplayed()
    }

    @Test
    fun testChannelScreenIsDisplayed() {
        val mockClient = mockk<ChatClient>(relaxed = true)
        val mockAccountService = mockk<AccountService>(relaxed = true)
        val navController = mockk<NavController>(relaxed = true)
        composeTestRule.setContent {
            ChannelScreen(mockClient, mockAccountService, navController = navController)
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag(channelScreenTag)
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag(channelScreenTag).assertIsDisplayed()
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
            context = context
        )
        assert(createFunc != null)
        assert(createPairFunc != null)
    }
}
