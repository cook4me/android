package ch.epfl.sdp.cook4me.ui.recipe

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.TestPermissionStatusProvider
import ch.epfl.sdp.cook4me.persistence.repository.RecipeRepository
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateRecipePermissionWrapperTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRecipeRepository = mockk<RecipeRepository>(relaxed = true)

    @Test
    fun assertCreateRecipeIsDisplayedWhenCameraIsEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("camera" to Pair(true, false)))

        composeTestRule.setContent {
            CreateRecipePermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                repository = mockRecipeRepository
            )
        }

        composeTestRule.onNodeWithText(text = "Recipe name").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun assertCreateRecipeIsNotDisplayedWhenCameraIsNotEnabled() {
        val permissionStatusProvider = TestPermissionStatusProvider(mapOf("camera" to Pair(false, false)))

        composeTestRule.setContent {
            CreateRecipePermissionWrapper(
                permissionStatusProvider = permissionStatusProvider,
                repository = mockRecipeRepository
            )
        }

        composeTestRule.onNodeWithText("The Camera permission will grant a better experience in the app")
            .assertIsDisplayed()
    }
}
