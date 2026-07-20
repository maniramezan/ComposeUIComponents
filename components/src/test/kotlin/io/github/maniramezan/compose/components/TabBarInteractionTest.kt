package io.github.maniramezan.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
public class TabBarInteractionTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private fun items(disableIndex: Int? = null) =
        listOf(0, 1, 2).mapIndexed { index, value ->
            TabBarItemData(
                value = value,
                icon = { Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null) },
                label = { Text(text = "Item $value") },
                enabled = index != disableIndex,
            )
        }

    @Test
    public fun tappingItemInvokesCallbackWithItsValue() {
        var lastSelection = -1
        composeRule.setContent {
            AppTheme {
                TabBar(items = items(), selection = 0, onSelectionChange = { lastSelection = it })
            }
        }

        composeRule.onNodeWithText("Item 2").performClick()

        composeRule.runOnIdle {
            assert(lastSelection == 2) { "Expected value 2, got $lastSelection" }
        }
    }

    @Test
    public fun selectionIsDrivenByValueNotPosition() {
        composeRule.setContent {
            AppTheme {
                var selection by remember { mutableIntStateOf(1) }
                TabBar(
                    // Reversed order — selection must still track by value, not list position.
                    items = items().reversed(),
                    selection = selection,
                    onSelectionChange = { selection = it },
                )
            }
        }

        composeRule.onNodeWithText("Item 1").assertIsSelected()
        composeRule.onNodeWithText("Item 0").assertIsNotSelected()
        composeRule.onNodeWithText("Item 2").assertIsNotSelected()
    }

    @Test
    public fun disabledItemDoesNotInvokeCallbackAndIsNotEnabled() {
        var lastSelection = -1
        composeRule.setContent {
            AppTheme {
                TabBar(
                    items = items(disableIndex = 2),
                    selection = 0,
                    onSelectionChange = { lastSelection = it },
                )
            }
        }

        composeRule.onNodeWithText("Item 2").assertIsNotEnabled()
        composeRule.onNodeWithText("Item 2").performClick()

        composeRule.runOnIdle {
            assert(lastSelection == -1) { "Disabled item must not invoke the selection callback" }
        }
        composeRule.onNodeWithText("Item 0").assertIsEnabled()
    }

    @Test
    public fun itemsMeetMinimumTouchTargetHeight() {
        composeRule.setContent {
            AppTheme {
                TabBar(items = items(), selection = 0, onSelectionChange = {})
            }
        }

        composeRule.onNodeWithText("Item 0").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("Item 1").assertHeightIsAtLeast(48.dp)
    }

    @Test
    public fun iconOnlyItemExposesContentDescriptionForAccessibility() {
        composeRule.setContent {
            AppTheme {
                TabBar(
                    items =
                        listOf(
                            TabBarItemData(
                                value = 0,
                                icon = {
                                    Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null)
                                },
                                label = null,
                                contentDescription = "Home",
                            ),
                        ),
                    selection = 0,
                    onSelectionChange = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Home").assertIsSelected()
    }

    @Test
    public fun centeredArrangementRendersAllItems() {
        composeRule.setContent {
            AppTheme {
                TabBar(
                    items = items(),
                    selection = 0,
                    onSelectionChange = {},
                    arrangement = TabBarArrangement.Centered,
                )
            }
        }

        composeRule.onNodeWithText("Item 0").assertIsSelected()
        composeRule.onNodeWithText("Item 1").assertIsNotSelected()
        composeRule.onNodeWithText("Item 2").assertIsNotSelected()
    }
}
