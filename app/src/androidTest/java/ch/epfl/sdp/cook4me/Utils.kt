package ch.epfl.sdp.cook4me

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.core.app.ActivityOptionsCompat
import androidx.test.espresso.matcher.ViewMatchers
import kotlinx.coroutines.runBlocking
fun registryOwnerFactory(testUri: Uri) = object : ActivityResultRegistryOwner {
    override val activityResultRegistry: ActivityResultRegistry =
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

fun assertThrowsAsync(f: suspend () -> Unit) {
    try {
        runBlocking {
            f()
        }
    } catch (
        e: Exception
    ) {
        return
    }
    throw AssertionError("no exception was thrown")
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 10_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
    }
}

// super hacky way to wait for AsyncImage to be displayed but seems to work
// should be called with assertIsDisplayed as it doesn't do the exhaustive checks
fun ComposeContentTestRule.waitUntilDisplayed(
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
