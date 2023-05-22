package ch.epfl.sdp.cook4me.ui.challengefeed

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.challenge.feed.ChallengeFeedScreen
import ch.epfl.sdp.cook4me.ui.challenge.feed.SearchViewModel
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChallengeFeedScreenTest {

    // Define Mocks
    private val onCreateNewChallengeClickMock = mockk<() -> Unit>(relaxed = true)
    private val onChallengeClickMock = mockk<(String) -> Unit>(relaxed = true)
    private val onFilterClickMock = mockk<() -> Unit>(relaxed = true)
    private val mockSearchViewModel = mockk<SearchViewModel>(relaxed = true)

    // Sample challenge data
    private val challengeData = Pair(
        "1",
        Challenge(
            name = "Challenge 1",
            description = "Description 1",
            creator = "Creator 1",
            type = "Type 1"
        )
    )

    private fun getString(id: Int): String {
        return composeTestRule.activity.getString(id)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        // Define behaviour for ViewModel Mock
        coEvery { mockSearchViewModel.loadNewData() } just Runs
        every { mockSearchViewModel.challenges } returns mutableStateListOf(challengeData)
        every { mockSearchViewModel.query } returns mutableStateOf("")
        every { mockSearchViewModel.isLoading } returns mutableStateOf(false)
    }

    @Test
    fun testChallengesDisplayed() {
        composeTestRule.setContent {
            ChallengeFeedScreen(
                onCreateNewChallengeClick = onCreateNewChallengeClickMock,
                onChallengeClick = onChallengeClickMock,
                onFilterClick = onFilterClickMock,
                searchViewModel = mockSearchViewModel
            )
        }

        // Verify if the challenge is displayed
        composeTestRule.onNodeWithText("Challenge 1").assertExists()
    }

    @Test
    fun testLoadingScreen() {
        every { mockSearchViewModel.isLoading } returns mutableStateOf(true)

        composeTestRule.setContent {
            ChallengeFeedScreen(
                onCreateNewChallengeClick = onCreateNewChallengeClickMock,
                onChallengeClick = onChallengeClickMock,
                onFilterClick = onFilterClickMock,
                searchViewModel = mockSearchViewModel
            )
        }

        // Verify if loading screen is visible
        composeTestRule.onNodeWithTag(getString(R.string.Loading_Screen_Tag))
    }
}
