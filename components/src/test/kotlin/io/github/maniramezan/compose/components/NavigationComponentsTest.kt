package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class NavigationComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun topAppBarVariantsRenderTitleAndSlots() {
        var navigationClicks = 0
        var actionClicks = 0
        composeRule.setContent {
            AppTheme {
                Column {
                    TopAppBar(
                        title = "Small title",
                        navigationIcon = { TextButton(text = "Back", onClick = { navigationClicks++ }) },
                        actions = { TextButton(text = "Action", onClick = { actionClicks++ }) },
                    )
                    MediumTopAppBar(title = "Medium title")
                    LargeTopAppBar(title = "Large title")
                }
            }
        }

        composeRule.onNodeWithText("Small title").assertIsDisplayed()
        composeRule.onNodeWithText("Medium title").assertIsDisplayed()
        composeRule.onNodeWithText("Large title").assertIsDisplayed()
        composeRule.onNodeWithText("Back").performClick()
        composeRule.onNodeWithText("Action").performClick()
        assertEquals(1, navigationClicks)
        assertEquals(1, actionClicks)
    }

    @Test
    public fun navRailSelectsByValueAndReportsTaps() {
        composeRule.setContent {
            AppTheme {
                var selection by remember { mutableStateOf(Destination.First) }
                NavRail(
                    items = railItems(),
                    selection = selection,
                    onSelectionChange = { selection = it },
                )
            }
        }

        composeRule.onNodeWithText("First").assertIsSelected()
        composeRule.onNodeWithText("Second").assertIsNotSelected()

        composeRule.onNodeWithText("Second").performClick()

        composeRule.onNodeWithText("Second").assertIsSelected()
        composeRule.onNodeWithText("First").assertIsNotSelected()
    }

    @Test
    public fun navRailDisabledItemIgnoresTaps() {
        val selections = mutableListOf<Destination>()
        composeRule.setContent {
            AppTheme {
                NavRail(
                    items = railItems(disabled = Destination.Second),
                    selection = Destination.First,
                    onSelectionChange = { selections += it },
                )
            }
        }

        composeRule.onNodeWithText("Second").assertIsNotEnabled().performClick()
        assertTrue(selections.isEmpty())
    }

    @Test
    public fun navRailIconOnlyItemUsesContentDescriptionAndMeetsTouchTarget() {
        composeRule.setContent {
            AppTheme {
                NavRail(
                    items =
                        listOf(
                            TabBarItemData(
                                value = Destination.First,
                                icon = { Text("•") },
                                contentDescription = "First destination",
                            ),
                        ),
                    selection = Destination.First,
                    onSelectionChange = {},
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("First destination")
            .assertIsSelected()
            .assertHeightIsAtLeast(48.dp)
    }

    private fun railItems(disabled: Destination? = null): List<TabBarItemData<Destination>> =
        Destination.entries.map { destination ->
            TabBarItemData(
                value = destination,
                icon = { Text("•") },
                label = { Text(destination.name) },
                enabled = destination != disabled,
            )
        }

    private enum class Destination { First, Second, Third }
}
