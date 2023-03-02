package ch.epfl.sdp.cook4me

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    var testRule = createAndroidComposeRule<MainActivity>()

    /** We only test on compose level and don't have any logic in the activity.
     * This placeholder is a workaround to exclude the MainActivity and layout files
     * from the Jacoco report.
     */
    @Test
    fun placeholder() {
        testRule.activity
    }
}