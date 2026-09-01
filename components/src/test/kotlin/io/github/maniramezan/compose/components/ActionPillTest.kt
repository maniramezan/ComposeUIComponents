package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ActionPillTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun invokesActionWithoutSelectionSemantics() {
        var taps = 0
        composeRule.setContent {
            AppTheme {
                ActionPill(onClick = { taps += 1 }) { Text("Run action") }
            }
        }

        composeRule.onNodeWithText("Run action").performClick()
        composeRule.runOnIdle { check(taps == 1) }
    }
}
