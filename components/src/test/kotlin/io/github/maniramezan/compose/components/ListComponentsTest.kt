package io.github.maniramezan.compose.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ListComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun emptyStateRendersCallerSuppliedTextAndAction() {
        composeRule.setContent {
            AppTheme {
                EmptyState(
                    title = "Nothing here",
                    message = "Items appear here",
                    action = { TextButton(text = "Add", onClick = {}) },
                )
            }
        }

        composeRule.onNodeWithText("Nothing here").assertIsDisplayed()
        composeRule.onNodeWithText("Items appear here").assertIsDisplayed()
        composeRule.onNodeWithText("Add").assertIsDisplayed()
    }
}
