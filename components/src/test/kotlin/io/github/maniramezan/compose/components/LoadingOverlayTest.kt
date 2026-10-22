package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.DpRect
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class LoadingOverlayTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private var loading by mutableStateOf(false)
    private var showIndicator by mutableStateOf(true)
    private var clicks = 0

    private fun setOverlayContent() {
        composeRule.setContent {
            AppTheme {
                LoadingOverlay(
                    loading = loading,
                    contentDescription = "Saving",
                    message = "Saving changes",
                    showIndicator = showIndicator,
                ) {
                    Column {
                        TextButton(text = "Submit", onClick = { clicks++ })
                    }
                }
            }
        }
    }

    @Test
    public fun contentIsInteractiveWhileNotLoading() {
        setOverlayContent()

        composeRule.onNodeWithText("Submit").performClick()
        composeRule.onNodeWithContentDescription("Saving").assertDoesNotExist()
        assertEquals(1, clicks)
    }

    @Test
    public fun loadingBlocksTouchesOnContent() {
        setOverlayContent()
        val buttonBounds: DpRect = composeRule.onNodeWithText("Submit").getUnclippedBoundsInRoot()

        composeRule.runOnIdle { loading = true }
        composeRule.onRoot().performTouchInput {
            click(
                with(density) {
                    Offset(
                        ((buttonBounds.left + buttonBounds.right) / 2).toPx(),
                        (
                            (buttonBounds.top + buttonBounds.bottom) /
                                2
                        ).toPx(),
                    )
                },
            )
        }

        composeRule.runOnIdle { assertEquals(0, clicks) }
    }

    @Test
    public fun loadingReplacesContentSemanticsWithAnnouncement() {
        setOverlayContent()

        composeRule.runOnIdle { loading = true }

        composeRule.onNodeWithText("Submit").assertDoesNotExist()
        composeRule
            .onNodeWithContentDescription("Saving")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
        // The spinner card stays out of the tree so TalkBack hears a single announcement.
        composeRule
            .onAllNodes(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo.Indeterminate))
            .assertCountEquals(0)
    }

    @Test
    public fun finishingLoadingRestoresContent() {
        setOverlayContent()

        composeRule.runOnIdle { loading = true }
        composeRule.runOnIdle { loading = false }

        composeRule.onNodeWithText("Submit").performClick()
        composeRule.onNodeWithContentDescription("Saving").assertDoesNotExist()
        assertEquals(1, clicks)
    }

    @Test
    public fun scrimOnlyModeStillBlocksAndAnnounces() {
        showIndicator = false
        setOverlayContent()

        composeRule.runOnIdle { loading = true }

        composeRule.onNodeWithContentDescription("Saving").assert(
            SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite),
        )
        composeRule.onNodeWithText("Submit").assertDoesNotExist()
    }
}
