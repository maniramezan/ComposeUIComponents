package io.github.maniramezan.compose.components

import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class AccessibilityComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun actionComponentsMeetMinimumTouchTargetSize() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                SecondaryButton(text = "Secondary", onClick = {})
                TextButton(text = "Text", onClick = {})
                IconButton(
                    icon = AppTheme.icons.close,
                    contentDescription = "Close",
                    onClick = {},
                )
                FAB(
                    icon = AppTheme.icons.check,
                    contentDescription = "Create",
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithText("Secondary").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("Text").assertHeightIsAtLeast(48.dp)
        composeRule
            .onNodeWithContentDescription("Close")
            .assertWidthIsAtLeast(48.dp)
            .assertHeightIsAtLeast(48.dp)
        composeRule
            .onNodeWithContentDescription("Create")
            .assertWidthIsAtLeast(48.dp)
            .assertHeightIsAtLeast(48.dp)
    }

    @Test
    public fun inputComponentsMeetMinimumTouchTargetSize() {
        composeRule.setContent {
            AppTheme {
                Checkbox(checked = false, onCheckedChange = {}, label = "Agree")
                RadioGroup(
                    options = listOf("One", "Two"),
                    selectedIndex = 0,
                    onOptionSelected = {},
                )
                Switch(checked = true, onCheckedChange = {}, label = "Enabled")
            }
        }

        composeRule.onNodeWithText("Agree").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("One").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("Two").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("Enabled").assertHeightIsAtLeast(48.dp)
    }

    @Test
    public fun iconOnlyActionsExposeContentDescriptions() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                IconButton(
                    icon = AppTheme.icons.close,
                    contentDescription = "Dismiss panel",
                    onClick = {},
                )
                FAB(
                    icon = AppTheme.icons.check,
                    contentDescription = "Confirm selection",
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Dismiss panel").assertExists()
        composeRule.onNodeWithContentDescription("Confirm selection").assertExists()
    }

    @Test
    public fun errorAndEmptyStatesExposeReadableText() {
        composeRule.setContent {
            AppTheme {
                EmptyState(title = "No results", message = "Try a different filter.")
                ErrorState(title = "Could not load", message = "Check your connection.")
            }
        }

        composeRule.onNodeWithText("No results").assertExists()
        composeRule.onNodeWithText("Try a different filter.").assertExists()
        composeRule.onNodeWithText("Could not load").assertExists()
        composeRule.onNodeWithText("Check your connection.").assertExists()
    }
}
