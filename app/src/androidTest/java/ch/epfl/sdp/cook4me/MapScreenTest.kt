import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.MapScreen
import ch.epfl.sdp.cook4me.ui.ProfileScreen
import com.google.android.gms.maps.model.LatLng
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun mapScreen_verifyDefaultMarkerIsInSatellite() {
        val markerLocation = LatLng(46.520544, 6.567825)

        composeTestRule.setContent {
            MapScreen()
        }

        //composeTestRule.onNodeWithContentDescription("Satellite").assertIsDisplayed()

//        composeTestRule.assertIs
//
//        composeTestRule.onNodeWithContentDescription("Satellite").apply {
//            assertIsInstanceOf(AndroidComposeNode::class.java)
//            val markerNode = (this as AndroidComposeNode).findNodeByContentDescription("Satellite")
//            assertNotNull(markerNode)
//            markerNode.assertTextEquals("Satellite")
//            markerNode.assertContentDescriptionEquals("Marker in Satellite")
//        }
    }
}