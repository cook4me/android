package ch.epfl.sdp.cook4me.ui.chat

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MessagesActivityTest {
    @Test
    fun testMessagesActivityFinishesWhenNullChannelIdIsPassed() {
        val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, MessagesActivity::class.java)

        ActivityScenario.launchActivityForResult<MessagesActivity>(intent).use { scenario ->
            assert(scenario.result.resultCode == Activity.RESULT_CANCELED)
        }
    }
}
