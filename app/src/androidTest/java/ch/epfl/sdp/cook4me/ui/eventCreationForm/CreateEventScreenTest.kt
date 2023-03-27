package ch.epfl.sdp.cook4me.ui.eventCreationForm

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.application.EventFormService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
//import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class CreateEventScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

//    val mockEventService = Mockito.mock(EventFormService::class.java)
//    mockEventService.when { submitForm(any()) }.thenReturn(null)

    @Test
    fun submitIncorrectFormsShowsError(){

    }

    @Test
    fun submitCorrectFormsError(){

    }

}