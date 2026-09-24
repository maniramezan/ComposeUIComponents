package io.github.maniramezan.compose.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class BadgeAndPageIndicatorTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun countBadgeShowsCappedCountAndReadsItByDefault() {
        composeRule.setContent {
            AppTheme { Badge(count = 250) }
        }

        composeRule.onNodeWithText("99+").assertExists()
    }

    @Test
    public fun suppliedDescriptionReplacesTheVisibleCount() {
        composeRule.setContent {
            AppTheme { Badge(count = 3, contentDescription = "3 unread", modifier = Modifier.testTag("badge")) }
        }

        composeRule.onNodeWithTag("badge").assertContentDescriptionEquals("3 unread")
        composeRule.onNodeWithText("3").assertDoesNotExist()
    }

    @Test
    public fun dotBadgeWithoutDescriptionStaysOutOfTheAccessibilityTree() {
        composeRule.setContent {
            AppTheme { Badge(modifier = Modifier.testTag("dot")) }
        }

        composeRule
            .onNodeWithTag("dot")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription))
    }

    @Test
    public fun pageIndicatorAnnouncesTheClampedPositionWhenDescribed() {
        composeRule.setContent {
            AppTheme {
                PageIndicator(
                    pageCount = 4,
                    currentPage = 10,
                    pagePositionDescription = { index, count -> "Page ${index + 1} of $count" },
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Page 4 of 4")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
    }
}
