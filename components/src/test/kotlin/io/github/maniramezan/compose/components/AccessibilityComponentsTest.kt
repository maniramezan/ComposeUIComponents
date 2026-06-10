package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.testing.assertMinimumTouchTarget
import io.github.maniramezan.compose.testing.onNodeWithRequiredContentDescription
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
            .onNodeWithRequiredContentDescription("Close")
            .assertMinimumTouchTarget()
        composeRule
            .onNodeWithRequiredContentDescription("Create")
            .assertMinimumTouchTarget()
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

        composeRule.onNodeWithRequiredContentDescription("Dismiss panel").assertExists()
        composeRule.onNodeWithRequiredContentDescription("Confirm selection").assertExists()
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

    @Test
    public fun pillChipExposesSelectionState() {
        composeRule.setContent {
            AppTheme {
                PillChip(label = "All", isSelected = true, onClick = {})
                PillChip(label = "Unread", isSelected = false, onClick = {})
            }
        }

        composeRule.onNodeWithText("All").assertIsSelected()
        composeRule.onNodeWithText("Unread").assertIsNotSelected()
    }

    @Test
    public fun segmentedControlExposesSelectionState() {
        composeRule.setContent {
            AppTheme {
                SegmentedControl(
                    options = listOf("Day", "Week", "Month"),
                    selectedIndex = 1,
                    onOptionSelected = {},
                )
            }
        }

        composeRule.onNodeWithText("Day").assertIsNotSelected()
        composeRule.onNodeWithText("Week").assertIsSelected()
        composeRule.onNodeWithText("Month").assertIsNotSelected()
    }

    @Test
    public fun extractedInteractiveComponentsMeetMinimumTouchTargetSize() {
        composeRule.setContent {
            AppTheme {
                Column(modifier = Modifier.fillMaxWidth()) {
                    PillChip(label = "Filter", isSelected = false, onClick = {})
                    ContentRow(title = "Row", modifier = Modifier.testTag("content-row"), onClick = {})
                    SegmentedContent(
                        items = listOf(SegmentedItem("One"), SegmentedItem("Two")),
                        initialSelectedIndex = 0,
                    ) { _, item ->
                        Text(text = item.title)
                    }
                }
            }
        }

        composeRule.onNode(hasText("Filter") and hasClickAction()).assertMinimumTouchTarget()
        composeRule.onNodeWithTag("content-row").assertMinimumTouchTarget()
        composeRule.onNode(hasText("One") and hasClickAction()).assertMinimumTouchTarget()
        composeRule.onNode(hasText("Two") and hasClickAction()).assertMinimumTouchTarget()
    }

    @Test
    public fun sliderExposesLabelAndValueDescription() {
        composeRule.setContent {
            AppTheme {
                Slider(
                    value = 0.5f,
                    onValueChange = {},
                    label = "Volume",
                    valueDescription = "50 percent",
                )
            }
        }

        composeRule.onNodeWithContentDescription("Volume").assertExists()
    }

    @Test
    public fun expandableSelectionRowsExposeStateOnRow() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                SelectionListContent(
                    title = "Options",
                    nodes =
                        listOf(
                            SelectionListNode(
                                id = "parent",
                                title = "Parent",
                                children = listOf(SelectionListNode(id = "child", title = "Child")),
                            ),
                        ),
                    selectedIds = emptySet(),
                    onSelect = {},
                    expandedDescription = "Expanded",
                    collapsedDescription = "Collapsed",
                )
            }
        }

        composeRule
            .onNode(hasText("Parent") and hasClickAction())
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Collapsed"))
    }

    @Test
    public fun navigationItemIconsAreDecorativeWhenLabelIsVisible() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                BottomBar(
                    items =
                        listOf(
                            NavigationItem(
                                label = "Home",
                                icon = AppTheme.icons.check,
                                contentDescription = "Home icon",
                            ),
                        ),
                    selectedIndex = 0,
                    onItemSelected = {},
                )
            }
        }

        composeRule.onNodeWithText("Home").assertExists()
        composeRule.onNodeWithContentDescription("Home icon").assertDoesNotExist()
    }

    @Test
    public fun extendedFabIconIsDecorativeByDefault() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                ExtendedFloatingActionButton(
                    text = "Create",
                    icon = AppTheme.icons.check,
                    contentDescription = "Create icon",
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithText("Create", useUnmergedTree = true).assertExists()
        composeRule.onNodeWithContentDescription("Create icon").assertDoesNotExist()
    }

    @Test
    public fun progressAndLoadingStatesExposeReadableText() {
        composeRule.setContent {
            AppTheme {
                ProgressIndicator(label = "Loading")
                LoadingState(label = "Loading projects")
            }
        }

        composeRule.onNodeWithText("Loading").assertExists()
        composeRule.onNodeWithText("Loading projects").assertExists()
    }
}
