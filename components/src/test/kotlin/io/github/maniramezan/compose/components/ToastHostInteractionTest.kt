package io.github.maniramezan.compose.components

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ToastHostInteractionTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private fun setUpHost(): Pair<ToastHostState, CoroutineScope> {
        lateinit var hostState: ToastHostState
        lateinit var scope: CoroutineScope
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            AppTheme {
                hostState = rememberToastHostState()
                scope = rememberCoroutineScope()
                ToastHost(hostState = hostState, dismissContentDescription = "Dismiss")
            }
        }
        return hostState to scope
    }

    @Test
    public fun tappingActionReturnsActionPerformed() {
        val (hostState, scope) = setUpHost()
        var result: ToastResult? = null

        composeRule.runOnIdle {
            scope.launch {
                result = hostState.showToast(message = "Saved", actionLabel = "Undo")
            }
        }
        // Play the enter animation so the toast is fully on screen.
        composeRule.mainClock.advanceTimeBy(500)

        composeRule.onNodeWithText("Saved").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").performClick()
        composeRule.mainClock.advanceTimeBy(500)

        composeRule.runOnIdle {
            assert(result == ToastResult.ActionPerformed) { "Expected ActionPerformed, got $result" }
        }
    }

    @Test
    public fun shortDurationAutoDismissesWithDismissedResult() {
        val (hostState, scope) = setUpHost()
        var result: ToastResult? = null

        composeRule.runOnIdle {
            scope.launch {
                result = hostState.showToast(message = "Bye", duration = ToastDuration.Short)
            }
        }
        composeRule.mainClock.advanceTimeBy(500)
        composeRule.onNodeWithText("Bye").assertIsDisplayed()

        // Cross the Short timeout, then let the exit animation finish.
        composeRule.mainClock.advanceTimeBy(ToastDuration.Short.timeoutMillis!!)
        composeRule.mainClock.advanceTimeBy(500)

        composeRule.runOnIdle {
            assert(result == ToastResult.Dismissed) { "Expected Dismissed, got $result" }
        }
    }

    @Test
    public fun indefiniteDoesNotAutoDismissAndTapDismisses() {
        val (hostState, scope) = setUpHost()
        var result: ToastResult? = null

        composeRule.runOnIdle {
            scope.launch {
                result = hostState.showToast(message = "Stay", duration = ToastDuration.Indefinite)
            }
        }
        composeRule.mainClock.advanceTimeBy(500)

        // Well past any auto-dismiss timeout: an Indefinite toast stays put.
        composeRule.mainClock.advanceTimeBy(30_000)
        composeRule.onNodeWithText("Stay").assertIsDisplayed()
        composeRule.runOnIdle {
            assert(result == null) { "Indefinite toast should not have resolved yet, got $result" }
        }

        // Tap-to-dismiss affordance resolves it as Dismissed.
        composeRule.onNodeWithText("Stay").performClick()
        composeRule.mainClock.advanceTimeBy(500)
        composeRule.runOnIdle {
            assert(result == ToastResult.Dismissed) { "Expected Dismissed, got $result" }
        }
    }
}
