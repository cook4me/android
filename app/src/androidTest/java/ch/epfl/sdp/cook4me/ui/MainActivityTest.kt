package ch.epfl.sdp.cook4me.ui

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.cook4me.MainActivity
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun activity_getsCreated() {
        rule.scenario.moveToState(Lifecycle.State.CREATED)
        assertTrue(rule.scenario.state.isAtLeast(Lifecycle.State.CREATED))
    }
}
