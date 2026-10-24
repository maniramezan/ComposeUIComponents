package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class NoticeBannerTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private val minimumTarget = 48.dp

    private fun hasLiveRegion(mode: LiveRegionMode) = SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, mode)

    private val hasNoLiveRegion = SemanticsMatcher.keyNotDefined(SemanticsProperties.LiveRegion)

    private val isButton = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)

    @Test
    public fun errorIsAnAssertiveLiveRegionAndWarningIsPolite() {
        composeRule.setContent {
            AppTheme {
                NoticeBanner(message = "Error message", severity = NoticeSeverity.Error)
                NoticeBanner(message = "Warning message", severity = NoticeSeverity.Warning)
            }
        }

        composeRule.onNodeWithText("Error message").assert(hasLiveRegion(LiveRegionMode.Assertive))
        composeRule.onNodeWithText("Warning message").assert(hasLiveRegion(LiveRegionMode.Polite))
    }

    @Test
    public fun infoAndSuccessAreNotLiveRegions() {
        composeRule.setContent {
            AppTheme {
                NoticeBanner(message = "Info message")
                NoticeBanner(message = "Success message", severity = NoticeSeverity.Success)
            }
        }

        composeRule.onNodeWithText("Info message").assert(hasNoLiveRegion)
        composeRule.onNodeWithText("Success message").assert(hasNoLiveRegion)
    }

    @Test
    public fun iconAndMessageMergeIntoOneFocusStopWithCallerSuppliedLabel() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                NoticeBanner(
                    message = "Saved",
                    severity = NoticeSeverity.Success,
                    icon = AppTheme.icons.check,
                    iconContentDescription = "Success",
                )
            }
        }

        composeRule
            .onNodeWithText("Saved")
            .assertContentDescriptionEquals("Success")
        composeRule.onAllNodesWithContentDescription("Success").assertCountEquals(1)
    }

    @Test
    public fun blankOrMissingIconDescriptionLeavesIconDecorative() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                NoticeBanner(
                    message = "Blank label",
                    icon = AppTheme.icons.check,
                    iconContentDescription = "  ",
                )
                NoticeBanner(message = "No label", icon = AppTheme.icons.check)
            }
        }

        listOf("Blank label", "No label").forEach { text ->
            composeRule
                .onNodeWithText(text)
                .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription))
        }
    }

    @Test
    public fun actionIsAButtonSeparateFromTheMessageAndMeetsTouchTarget() {
        var actions = 0
        composeRule.setContent {
            AppTheme {
                NoticeBanner(
                    message = "Something happened",
                    actionLabel = "Retry",
                    onAction = { actions++ },
                )
            }
        }

        val action = composeRule.onNode(hasClickAction() and hasText("Retry"))
        action
            .assertHeightIsAtLeast(minimumTarget)
            .assertWidthIsAtLeast(minimumTarget)
            .assert(isButton)
            .performClick()
        assertEquals(1, actions)
        composeRule.onNodeWithText("Something happened").assert(!hasClickAction())
    }

    @Test
    public fun dismissIsALabeledButtonThatMeetsTouchTarget() {
        var dismissals = 0
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                NoticeBanner(
                    message = "Dismissible",
                    dismissContentDescription = "Dismiss notice",
                    onDismiss = { dismissals++ },
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Dismiss notice")
            .assertHeightIsAtLeast(minimumTarget)
            .assertWidthIsAtLeast(minimumTarget)
            .assert(isButton)
            .performClick()
        assertEquals(1, dismissals)
    }

    @Test
    public fun controlsAreOmittedWhenTheirLabelOrCallbackIsMissing() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                NoticeBanner(message = "Label only", actionLabel = "Label only action")
                NoticeBanner(message = "Callback only", onAction = {})
                NoticeBanner(message = "Blank action", actionLabel = " ", onAction = {})
                NoticeBanner(message = "Dismiss label only", dismissContentDescription = "Label only dismiss")
                NoticeBanner(message = "Dismiss callback only", onDismiss = {})
                NoticeBanner(message = "Blank dismiss", dismissContentDescription = "", onDismiss = {})
            }
        }

        composeRule.onAllNodesWithText("Label only action").assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("Label only dismiss").assertCountEquals(0)
        composeRule.onAllNodes(hasClickAction()).assertCountEquals(0)
    }

    @Test
    public fun bannerFillsTheAvailableWidth() {
        composeRule.setContent {
            AppTheme {
                Box(modifier = Modifier.width(320.dp)) {
                    NoticeBanner(message = "Wide", modifier = Modifier.testTag("banner"))
                }
            }
        }

        composeRule.onNodeWithTag("banner").assertWidthIsEqualTo(320.dp)
    }

    @Test
    public fun controlsKeepTouchTargetsAtDoubleFontScale() {
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f)) {
                AppTheme(icons = defaultAppIcons()) {
                    NoticeBanner(
                        message = "A longer message that wraps across several lines at a large font scale.",
                        actionLabel = "Retry",
                        onAction = {},
                        dismissContentDescription = "Dismiss notice",
                        onDismiss = {},
                    )
                }
            }
        }

        composeRule.onNode(hasClickAction() and hasText("Retry")).assertHeightIsAtLeast(minimumTarget)
        composeRule.onNodeWithContentDescription("Dismiss notice").assertHeightIsAtLeast(minimumTarget)
        composeRule.onAllNodesWithText("A longer message", substring = true).assertCountEquals(1)
    }

    @Test
    public fun severityResolvesDistinctAccentColors() {
        var accents = emptyList<Color>()
        composeRule.setContent {
            AppTheme {
                accents = NoticeSeverity.entries.map { it.accentColor() }
            }
        }

        composeRule.runOnIdle {
            assertEquals(NoticeSeverity.entries.size, accents.distinct().size)
            assertFalse(accents.isEmpty())
        }
    }
}
