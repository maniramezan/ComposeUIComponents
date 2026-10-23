package io.github.maniramezan.compose.components

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class PageIndicatorTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun describedPositionIsClampedAndAnnouncedPolitely() {
        composeRule.setContent {
            AppTheme {
                PageIndicator(
                    pageCount = 4,
                    currentPage = 10,
                    modifier = Modifier.testTag("indicator"),
                    pagePositionDescription = { index, count -> "Page ${index + 1} of $count" },
                )
            }
        }

        composeRule
            .onNodeWithTag("indicator")
            .assertContentDescriptionEquals("Page 4 of 4")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
    }

    @Test
    public fun missingOrBlankDescriptionsLeaveDotsDecorative() {
        composeRule.setContent {
            AppTheme {
                PageIndicator(3, 0, modifier = Modifier.testTag("missing"))
                PageIndicator(3, 0, modifier = Modifier.testTag("blank"), pagePositionDescription = { _, _ -> "  " })
            }
        }

        listOf("missing", "blank").forEach { tag ->
            composeRule
                .onNodeWithTag(tag)
                .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription))
                .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.LiveRegion))
        }
    }

    @Test
    public fun nonPositivePageCountsRenderNothing() {
        composeRule.setContent {
            AppTheme {
                PageIndicator(0, 0, modifier = Modifier.testTag("empty"))
                PageIndicator(-1, 0, modifier = Modifier.testTag("negative"))
            }
        }

        composeRule.onNodeWithTag("empty").assertDoesNotExist()
        composeRule.onNodeWithTag("negative").assertDoesNotExist()
    }

    @Test
    public fun pagerBindingTracksPageCountChangesAndNegativeIndicesClampToZero() {
        var updateCount: (Int) -> Unit = {}
        composeRule.setContent {
            AppTheme {
                var count by remember { mutableIntStateOf(5) }
                updateCount = { count = it }
                val state = rememberPagerState(initialPage = 1, pageCount = { count })
                PageIndicator(
                    pagerState = state,
                    modifier = Modifier.testTag("bound"),
                    pagePositionDescription = { index, total -> "Page ${index + 1} of $total" },
                )
                PageIndicator(
                    pageCount = count,
                    currentPage = -1,
                    modifier = Modifier.testTag("negative-index"),
                    pagePositionDescription = { index, total -> "Page ${index + 1} of $total" },
                )
            }
        }

        composeRule.onNodeWithTag("bound").assertContentDescriptionEquals("Page 2 of 5")
        composeRule.onNodeWithTag("negative-index").assertContentDescriptionEquals("Page 1 of 5")
        composeRule.runOnIdle { updateCount(3) }
        composeRule.onNodeWithTag("bound").assertContentDescriptionEquals("Page 2 of 3")
    }
}
