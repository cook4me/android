package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockDatabase = Firebase.database


    @Test
    fun setAndGetWithSameKeyReturnsSameValue() {
        mockDatabase.useEmulator("localhost", 9000)
        composeTestRule.setContent {
            FirebaseScreen(
                database = mockDatabase.reference
            )
        }

        val phoneNumber = "012"
        val email = "a@epfl.ch"

        // enter phone number and email
        composeTestRule.onNodeWithTag(EMAIL_TEXT_FIELD_TAG).performTextInput(email)
        composeTestRule.onNodeWithTag(PHONE_TEXT_FIELD_TAG).performTextInput(phoneNumber)

        // click on set button
        composeTestRule.onNodeWithTag(SET_DB_BUTTON_TAG).performClick()

        // clean the email
        composeTestRule.onNodeWithTag(EMAIL_TEXT_FIELD_TAG).performTextInput("")

        // click on get button
        composeTestRule.onNodeWithTag(GET_DB_BUTTON_TAG).performClick()

        // check if the email is the same
        composeTestRule.onNodeWithTag(EMAIL_TEXT_FIELD_TAG).assertTextEquals(email)
    }


}