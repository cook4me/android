package ch.epfl.sdp.cook4me

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.layout.LayoutInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.TupCreationScreen
import ch.epfl.sdp.cook4me.ui.TupCreationViewModel
import coil.Coil
import coil.EventListener
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import okhttp3.internal.wait
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TupCreationScenarioTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    val testUri: Uri = Uri.parse(
        "android.resource://ch.epfl.sdp.cook4me/"+R.drawable.placeholder_tupperware
    )
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
        composeTestRule.setContent {
            Coil.setImageLoader(
                ImageLoader.Builder(LocalContext.current)
                    .eventListener(object : EventListener {
                        override fun onSuccess(
                            request: ImageRequest,
                            result: SuccessResult
                        ) {
                            assert(testUri.toString() == request.data.toString())
                        }
                    })
                    .build()
            )

            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreen()
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun descriptionFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreen()
        }
        composeTestRule.onNodeWithText(text = "Description").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DescriptionTextField").assertIsDisplayed()

    }
    @Test
    fun titleFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreen()
        }
        composeTestRule.onNodeWithText(text = "Title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("TitleTextField").assertIsDisplayed()
    }

    @Test
    fun tagsFieldIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreen()
        }
        composeTestRule.onNodeWithText(text = "Tags").assertIsDisplayed()
        composeTestRule.onNodeWithTag("TagsTextField").assertIsDisplayed()
    }

    @Test
    fun headerIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreen()
        }
        composeTestRule.onNodeWithText(text = "Header").assertIsDisplayed()
    }

    @Test
    fun buttonRowIsDisplayed() {
        composeTestRule.setContent {
            TupCreationScreen()
        }
        composeTestRule.onNodeWithText(text = "Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Done").assertIsDisplayed()
    }

    // no title or no image or no description
    @Test
    fun tupperwareFormWithNoTitleShouldNotBeSubmittable() {

        composeTestRule.setContent {
            Log.d("Debug", "${LocalViewModelStoreOwner.current}")
            TupCreationScreen(viewModel = viewModel())
        }
    }

    @Test
    fun tupperwareFormWithNoImageShouldNotBeSubmittable() {

    }

    @Test
    fun tupperwareFormWithNoDescriptionShouldNotBeSubmittable() {

    }

    @Test
    fun addingImageFromGalleryShouldDisplayImage() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                // any composable inside this block will now use our mock ActivityResultRegistry
                TupCreationScreen()
            }
        }
        composeTestRule.onNodeWithTag("AddImage").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun takingPictureShouldDisplayImage() {
        composeTestRule.setContent {
            val viewModel: TupCreationViewModel = viewModel()


            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
                TupCreationScreen()
            }
        }
        composeTestRule.onNodeWithTag("takePhoto").performClick()
        composeTestRule.waitUntilDisplayed(hasTestTag("image"))
        composeTestRule.onNodeWithTag("image", useUnmergedTree = true).assertIsDisplayed()

    }

    @Test
    fun cancellingImageSelectionShouldNotDisplayImage() {

    }

    @Test
    fun selectingInvalidImageShouldShowErrorMessage() {

    }

    @Test
    fun cancellingTakingPhotoShouldNotDisplayImage() {

    }
}