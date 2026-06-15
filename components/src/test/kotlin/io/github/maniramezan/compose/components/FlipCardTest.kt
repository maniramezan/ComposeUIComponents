package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class FlipCardTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun uncontrolledCardSwapsFaceOnTap() {
        composeRule.setContent {
            AppTheme {
                FlipCard(
                    modifier = Modifier.size(200.dp, 120.dp),
                    front = { Text("Front") },
                    back = { Text("Back") },
                )
            }
        }

        // Both faces are composed, but the away-facing one is culled from the
        // semantics tree, so only the front is discoverable at rest.
        composeRule.onNodeWithText("Front").assertExists()
        composeRule.onNodeWithText("Back").assertDoesNotExist()

        composeRule.onNodeWithText("Front").performClick()

        composeRule.onNodeWithText("Back").assertExists()
        composeRule.onNodeWithText("Front").assertDoesNotExist()
    }

    @Test
    public fun controlledCardReportsRequestedStateOnTap() {
        var lastRequested: Boolean? = null
        composeRule.setContent {
            AppTheme {
                var flipped by remember { mutableStateOf(false) }
                FlipCard(
                    modifier = Modifier.size(200.dp, 120.dp),
                    flipped = flipped,
                    onFlippedChange = {
                        lastRequested = it
                        flipped = it
                    },
                    front = { Text("Front") },
                    back = { Text("Back") },
                )
            }
        }

        composeRule.onNodeWithText("Front").performClick()

        composeRule.runOnIdle {
            assert(lastRequested == true) { "Expected flip request true, got $lastRequested" }
        }
        composeRule.onNodeWithText("Back").assertExists()
    }

    @Test
    public fun disabledCardDoesNotFlipOnTap() {
        composeRule.setContent {
            AppTheme {
                FlipCard(
                    modifier = Modifier.size(200.dp, 120.dp),
                    enabled = false,
                    front = { Text("Front") },
                    back = { Text("Back") },
                )
            }
        }

        composeRule.onNodeWithText("Front").performClick()

        composeRule.onNodeWithText("Front").assertExists()
    }
}
