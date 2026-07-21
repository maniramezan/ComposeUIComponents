package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
public class SegmentedContentInteractionTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private val items =
        listOf(
            SegmentedItem(title = "Overview"),
            SegmentedItem(title = "Activity"),
            SegmentedItem(title = "Settings"),
        )

    @Test
    public fun tappingSegmentInvokesCallbackWithItsIndex() {
        var lastIndex = -1
        composeRule.setContent {
            AppTheme {
                SegmentedContent(
                    items = items,
                    selectedIndex = 0,
                    onSelectionChanged = { lastIndex = it },
                ) { _, item -> Text("${item.title} body") }
            }
        }

        composeRule.onNodeWithText("Settings").performClick()

        composeRule.runOnIdle {
            assert(lastIndex == 2) { "Expected index 2, got $lastIndex" }
        }
    }

    @Test
    public fun selectingSegmentSwapsContent() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableIntStateOf(0) }
                SegmentedContent(
                    items = items,
                    selectedIndex = selected,
                    onSelectionChanged = { selected = it },
                ) { _, item -> Text("${item.title} body") }
            }
        }

        composeRule.onNodeWithText("Overview body").assertExists()

        composeRule.onNodeWithText("Activity").performClick()

        composeRule.onNodeWithText("Activity body").assertExists()
        composeRule.onNodeWithText("Overview body").assertDoesNotExist()
    }

    @Test
    public fun selectedSegmentExposesSelectedState() {
        composeRule.setContent {
            AppTheme {
                SegmentedContent(
                    items = items,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                ) { _, item -> Text("${item.title} body") }
            }
        }

        composeRule.onNodeWithText("Activity").assertIsSelected()
    }

    @Test
    public fun segmentsMeetMinimumTouchTargetHeight() {
        composeRule.setContent {
            AppTheme {
                SegmentedContent(
                    items = items,
                    selectedIndex = 0,
                    onSelectionChanged = {},
                ) { _, item -> Text("${item.title} body") }
            }
        }

        composeRule.onNodeWithText("Overview").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("Settings").assertHeightIsAtLeast(48.dp)
    }

    @Test
    public fun compactDensityRendersAVisuallyShorterSlotThanRegular() {
        var density by mutableStateOf(SegmentDensity.Regular)
        composeRule.setContent {
            AppTheme {
                SegmentedContent(
                    items = items,
                    selectedIndex = 0,
                    onSelectionChanged = {},
                    density = density,
                ) { _, item -> Text("${item.title} body") }
            }
        }
        val regularVisualHeight =
            composeRule
                .onNodeWithTag("$SEGMENT_SLOT_VISUAL_TEST_TAG-0", useUnmergedTree = true)
                .fetchSemanticsNode()
                .size.height

        density = SegmentDensity.Compact
        composeRule.waitForIdle()

        val compactVisualHeight =
            composeRule
                .onNodeWithTag("$SEGMENT_SLOT_VISUAL_TEST_TAG-0", useUnmergedTree = true)
                .fetchSemanticsNode()
                .size.height

        // Compact's whole point is a visually shorter/denser highlight than Regular; the
        // 48dp touch target is still met by the outer slot regardless (see the test above and
        // AccessibilityComponentsTest.compactSegmentedContentMeetsMinimumTouchTargetSize).
        assert(compactVisualHeight < regularVisualHeight) {
            "Expected Compact's visual slot ($compactVisualHeight px) to be shorter than " +
                "Regular's ($regularVisualHeight px)"
        }
    }

    @Test
    public fun selectedSegmentScrollsIntoViewWhenOverflowing() {
        val manyItems =
            listOf(
                SegmentedItem(title = "Overview"),
                SegmentedItem(title = "Activity"),
                SegmentedItem(title = "Settings"),
                SegmentedItem(title = "Notifications"),
                SegmentedItem(title = "Permissions"),
                SegmentedItem(title = "Integrations"),
                SegmentedItem(title = "Billing"),
            )
        composeRule.setContent {
            AppTheme {
                SegmentedContent(
                    items = manyItems,
                    selectedIndex = manyItems.lastIndex,
                    onSelectionChanged = {},
                    fitMode = SegmentFitMode.Intrinsic,
                    modifier = Modifier.width(200.dp),
                ) { _, item -> Text("${item.title} body") }
            }
        }

        // The initially-selected last segment must be scrolled into the viewport
        // (an off-screen LazyRow item would not be composed).
        composeRule.onNodeWithText("Billing").assertIsSelected()
    }

    @Test
    public fun selectingMiddleSegmentPeeksPreviousNeighbour() {
        val manyItems =
            listOf(
                SegmentedItem(title = "Overview"),
                SegmentedItem(title = "Activity"),
                SegmentedItem(title = "Settings"),
                SegmentedItem(title = "Notifications"),
                SegmentedItem(title = "Permissions"),
                SegmentedItem(title = "Integrations"),
                SegmentedItem(title = "Billing"),
            )
        composeRule.setContent {
            AppTheme {
                SegmentedContent(
                    items = manyItems,
                    selectedIndex = 3,
                    onSelectionChanged = {},
                    fitMode = SegmentFitMode.Intrinsic,
                    modifier = Modifier.width(220.dp),
                ) { _, item -> Text("${item.title} body") }
            }
        }

        // We scrolled far enough that the first segment is gone…
        composeRule.onNodeWithText("Overview").assertIsNotDisplayed()
        // …but the segment immediately before the selection still peeks in,
        // so the user can tell there's more to the left.
        composeRule.onNodeWithText("Settings").assertIsDisplayed()
    }
}
