package io.github.maniramezan.compose.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class InteractionModifiersTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun minimumTouchTargetExpandsSmallContentInBothAxes() {
        composeRule.setContent {
            Box(Modifier.testTag(TAG).minimumTouchTarget(TARGET)) {
                Box(Modifier.size(SMALL))
            }
        }

        composeRule
            .onNodeWithTag(TAG)
            .assertWidthIsEqualTo(TARGET)
            .assertHeightIsEqualTo(TARGET)
    }

    @Test
    public fun minimumTouchTargetDoesNotShrinkLargerContent() {
        composeRule.setContent {
            Box(Modifier.testTag(TAG).minimumTouchTarget(TARGET)) {
                Box(Modifier.size(LARGE))
            }
        }

        composeRule
            .onNodeWithTag(TAG)
            .assertWidthIsEqualTo(LARGE)
            .assertHeightIsEqualTo(LARGE)
    }

    @Test
    public fun minimumTouchTargetHeightOnlyConstrainsHeight() {
        composeRule.setContent {
            Box(Modifier.testTag(TAG).minimumTouchTargetHeight(TARGET)) {
                Box(Modifier.size(SMALL))
            }
        }

        composeRule
            .onNodeWithTag(TAG)
            .assertWidthIsEqualTo(SMALL)
            .assertHeightIsEqualTo(TARGET)
    }

    @Test
    public fun minimumTouchTargetWidthOnlyConstrainsWidth() {
        composeRule.setContent {
            Box(Modifier.testTag(TAG).minimumTouchTargetWidth(TARGET)) {
                Box(Modifier.size(SMALL))
            }
        }

        composeRule
            .onNodeWithTag(TAG)
            .assertWidthIsEqualTo(TARGET)
            .assertHeightIsEqualTo(SMALL)
    }

    @Test
    public fun buttonRoleAppliesButtonSemanticsTouchTargetAndClick() {
        var clicks = 0
        composeRule.setContent {
            Box(Modifier.testTag(TAG).buttonRole(onClick = { clicks++ }, minimumTouchTarget = TARGET)) {
                Box(Modifier.size(SMALL))
            }
        }

        composeRule
            .onNodeWithTag(TAG)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assertWidthIsAtLeast(TARGET)
            .assertHeightIsAtLeast(TARGET)
            .performClick()

        assertEquals(1, clicks)
    }

    @Test
    public fun selectableRoleExposesSelectionStateAndClick() {
        var clicks = 0
        composeRule.setContent {
            Box(
                Modifier
                    .testTag(TAG)
                    .selectableRole(selected = true, onClick = { clicks++ }, minimumTouchTarget = TARGET),
            ) {
                Box(Modifier.size(SMALL))
            }
        }

        composeRule
            .onNodeWithTag(TAG)
            .assertIsSelected()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assertWidthIsAtLeast(TARGET)
            .assertHeightIsAtLeast(TARGET)
            .performClick()

        assertEquals(1, clicks)
    }

    @Test
    public fun selectableRoleReportsUnselectedState() {
        composeRule.setContent {
            Box(
                Modifier
                    .testTag(TAG)
                    .selectableRole(selected = false, onClick = {}, minimumTouchTarget = TARGET),
            ) {
                Box(Modifier.size(SMALL))
            }
        }

        composeRule.onNodeWithTag(TAG).assertIsNotSelected()
    }

    private companion object {
        const val TAG = "target"
        val TARGET = 48.dp
        val SMALL = 16.dp
        val LARGE = 72.dp
    }
}
