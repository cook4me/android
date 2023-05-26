package ch.epfl.sdp.cook4me.ui.user.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.persistence.model.Profile
import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import ch.epfl.sdp.cook4me.persistence.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class EmergencyDumbProfileTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var mockProfileRepository = mockk<ProfileRepository>(relaxed = true) // <--

    private var mockProfileImageRepository = mockk<ProfileImageRepository>(relaxed = true)

    private var mockAccountService = mockk<AccountService>(relaxed = true)

    private val testUserId = "testUserId"
    private val testProfile = Profile(
        name = "Test User",
        email = "testuser@gmail.com",
        favoriteDish = "Pizza",
        allergies = "Gluten",
        bio = "I love cooking!"
    )

    @Test
    fun profileScreenDisplaysCorrectInformation() {
        coEvery { mockAccountService.getCurrentUserWithEmail() } returns (testUserId)
        coEvery { mockProfileRepository.getById(testUserId) } returns(testProfile)

        composeTestRule.setContent {
            ProfileScreen(
                userId = testUserId,
                profileRepository = mockProfileRepository,
                profileImageRepository = mockProfileImageRepository,
                accountService = mockAccountService
            )
        }

        composeTestRule.onNodeWithText(testProfile.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(testProfile.favoriteDish).assertIsDisplayed()
        composeTestRule.onNodeWithText(testProfile.allergies).assertIsDisplayed()
        composeTestRule.onNodeWithText(testProfile.bio).assertIsDisplayed()
    }

    @Test
    fun profileScreenDisplaysLoadingScreenWhileFetchingProfile() {
        coEvery { mockAccountService.getCurrentUserWithEmail() } returns (null)
        composeTestRule.setContent {
            ProfileScreen(
                userId = null,
                profileRepository = mockProfileRepository,
                profileImageRepository = mockProfileImageRepository,
                accountService = mockAccountService
            )
        }

        composeTestRule.onNodeWithTag("Loading Screen test tag").assertIsDisplayed()
    }
}
