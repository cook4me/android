package ch.epfl.sdp.cook4me

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.application.TupperwareService
import ch.epfl.sdp.cook4me.ui.TupCreationScreen
import ch.epfl.sdp.cook4me.ui.TupCreationScreenWithState
import ch.epfl.sdp.cook4me.ui.TupCreationViewModel
import ch.epfl.sdp.cook4me.ui.TupCreationViewModelFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TupCreationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    val testUri: Uri = Uri.parse(
        "android.resource://ch.epfl.sdp.cook4me/" + R.drawable.placeholder_tupperware
    )

    private val workingUri: Uri = testUri

    private val registryOwner = ActivityResultRegistryOwner {
        object : ActivityResultRegistry() {
            override fun <I : Any?, O : Any?> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                // don't launch an activity, just respond with the test Uri
                val intent = Intent().setData(testUri)
                this.dispatchResult(requestCode, Activity.RESULT_OK, intent)
            }
        }
    }

    class MockTupperwareService(
        private val expectedTitle: String,
        private val expectedDesc: String,
        private val expectedTags: List<String>,
        private val expectedImages: List<Uri>,
    ): TupperwareService() {
        override fun submitForm(
            title: String,
            desc: String,
            tags: List<String>,
            photos: List<Uri>,
        ) {
            assert(expectedTitle == title)
            assert(expectedDesc == desc)
            assert(expectedTags.zip(tags).all {(x, y) -> x == y})
            assert(expectedImages.zip(photos).all {(x, y) -> x == y})
        }
    }

    @Composable
    fun TestScreenWithWorkingAddImage(
        viewModel: TupCreationViewModel = viewModel()
    ) {
        fun workingAddImageOrTakePhoto() {
            viewModel.addImage(workingUri)
        }
        TupCreationScreen(
            onClickAddImage = { workingAddImageOrTakePhoto() },
            onClickTakePhoto = { workingAddImageOrTakePhoto() }
        )
    }

    // super hacky way to wait for AsyncImage to be displayed but seems to work
    // should be called with assertIsDisplayed as it doesn't do the exhaustive checks
    private fun ComposeContentTestRule.waitUntilDisplayed(
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

    @Test
    fun submittingValidTupFormShouldOutputCorrectTupperwareObject() {
        val title = "Pizza"
        val desc = "Yeah the photo is not lying it's not good..."
        val tags = listOf<String>()
        val images = listOf(testUri)
        val mockService = MockTupperwareService(title, desc, tags, images)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState(viewModel = viewModel(factory = TupCreationViewModelFactory(mockService)))
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("TitleTextField").performTextInput("Pizza")
        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
    }

    @Test
    fun descriptionFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState()
        }
        composeTestRule.onNodeWithText(text = "Description").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("DescriptionTextField").assertIsDisplayed()
    }
    @Test
    fun titleFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState()
        }
        composeTestRule.onNodeWithText(text = "Tupperware Name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("TitleTextField").assertIsDisplayed()
    }

    @Test
    fun tagsFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState()
        }
        composeTestRule.onNodeWithContentDescription("TagsTextField").performScrollTo()
        composeTestRule.onNodeWithText(text = "Tags").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("TagsTextField").assertIsDisplayed()
    }

    @Test
    fun headerIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState()
        }
        composeTestRule.onNodeWithText(text = "Header").assertIsDisplayed()
    }

    @Test
    fun buttonRowIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreenWithState()
        }
        composeTestRule.onNodeWithText(text = "Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Done").assertIsDisplayed()
    }

    // no title or no image or no description
    @Test
    fun tupperwareFormWithNoTitleShouldNotBeSubmittable() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState()
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
    }

    @Test
    fun tupperwareFormWithNoImageShouldNotBeSubmittable() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState()
            }
        }

        composeTestRule.onNodeWithContentDescription("TitleTextField").performTextInput("Pizza")
        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
            .performTextInput("Yeah the photo is not lying it's not good...")
        composeTestRule.onNodeWithText("Done").performClick()
    }

    @Test
    fun tupperwareFormWithNoDescriptionShouldNotBeSubmittable() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState()
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("TitleTextField").performTextInput("Pizza")
        composeTestRule.onNodeWithText("Done").performClick()
        composeTestRule.onNodeWithContentDescription("DescriptionTextField")
    }

    @Test
    fun addingImageFromGalleryShouldDisplayImage() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreenWithState()
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun takingPictureShouldDisplayImage() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                TestScreenWithWorkingAddImage()
            }
        }
        composeTestRule.onNodeWithTag("takePhoto").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
    }
}
