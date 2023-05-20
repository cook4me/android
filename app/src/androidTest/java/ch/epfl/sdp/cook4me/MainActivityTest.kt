package ch.epfl.sdp.cook4me

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// TODO: is this still needed?
class MainActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun activity_getsCreated() {
        rule.scenario.moveToState(Lifecycle.State.CREATED)
        assertTrue(rule.scenario.state.isAtLeast(Lifecycle.State.CREATED))
    }
}
