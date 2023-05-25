package ch.epfl.sdp.cook4me.ui.tupperware.form

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.persistence.repository.TupperwareRepository
import ch.epfl.sdp.cook4me.registryOwnerFactory
import ch.epfl.sdp.cook4me.waitUntilDisplayed
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TupperwareCreationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testUri: Uri = Uri.parse(
        "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.ic_user
    )

    private val textFieldWithErrorMatcher =
        SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Error"
        )

    @Test
    fun submittingValidTupFormShouldOutputCorrectTupperwareObject() {
        val mockTupperwareRepository = mockk<TupperwareRepository>(relaxed = true)
        val expectedTitle = "Pizza"
        val expectedDescription = "Yeah the photo is not lying it's not good..."
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwnerFactory(testUri)) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                CreateTupperwareScreen({}, {}, mockTupperwareRepository)
            }
        }
        composeTestRule.onNodeWithTag("Add From Gallery Button").performClick()
        composeTestRule.waitUntilDisplayed(hasContentDescription("Selected Image"))
        composeTestRule.onNodeWithContentDescription("Selected Image", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("title").performTextInput(expectedTitle)
        composeTestRule.onNodeWithTag("description")
            .performTextInput(expectedDescription)
        composeTestRule.onNodeWithText("Done").performClick()
        verify {
            runBlocking {
                mockTupperwareRepository.add(
                    expectedTitle,
                    expectedDescription,
                    testUri
                )
            }
        }
        confirmVerified(mockTupperwareRepository)
    }

    @Test
    fun descriptionFieldIsDisplayed() {
        composeTestRule.setContent {
            CreateTupperwareScreen({}, {})
        }
        composeTestRule.onNodeWithText(text = "Description").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun titleFieldIsDisplayed() {
        composeTestRule.setContent {
            CreateTupperwareScreen({}, {})
        }
        composeTestRule.onNodeWithText(text = "Tupperware Name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("title").assertIsDisplayed()
    }

    @Test
    fun buttonRowIsDisplayed() {

        composeTestRule.setContent {
            CreateTupperwareScreen({}, {})
        }
        composeTestRule.onNodeWithText(text = "Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Done").assertIsDisplayed()
    }

// no title or no image or no description
    @Test
    fun tupperwareFormWithNoTitleShouldNotBeSubmittable() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwnerFactory(testUri)) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                CreateTupperwareScreen({}, {})
            }
        }
        composeTestRule.onNodeWithTag("Add From Gallery Button").performClick()
        composeTestRule.waitUntilDisplayed(hasContentDescription("Selected Image"))
        composeTestRule.onNodeWithContentDescription("Selected Image", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("description")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
        composeTestRule.onAllNodes(textFieldWithErrorMatcher)[0].assertExists()
    }

    @Test
    fun tupperwareFormWithNoDescriptionShouldNotBeSubmittable() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwnerFactory(testUri)) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                CreateTupperwareScreen({}, {})
            }
        }
        composeTestRule.onNodeWithTag("Add From Gallery Button").performClick()
        composeTestRule.waitUntilDisplayed(hasContentDescription("Selected Image"))
        composeTestRule.onNodeWithContentDescription("Selected Image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithTag("title").performTextInput("Pizza")
        composeTestRule.onNodeWithText("Done").performClick()
        composeTestRule.onAllNodes(textFieldWithErrorMatcher)[0].assertExists()
    }

    @Test
    fun addingImageFromGalleryShouldDisplayImage() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwnerFactory(testUri)) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                CreateTupperwareScreen({}, {})
            }
        }
        composeTestRule.onNodeWithTag("Add From Gallery Button").performClick()
        composeTestRule.waitUntilDisplayed(hasContentDescription("Selected Image"))
        composeTestRule.onNodeWithContentDescription("Selected Image", useUnmergedTree = true).assertIsDisplayed()
    }
}
