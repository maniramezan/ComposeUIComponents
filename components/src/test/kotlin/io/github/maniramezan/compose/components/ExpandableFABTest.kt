package io.github.maniramezan.compose.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ExpandableFABTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private val events = mutableListOf<String>()

    @Composable
    private fun Menu(
        expanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        direction: FABExpansionDirection = FABExpansionDirection.Up,
        enabled: Boolean = true,
        collapseOnAction: Boolean = true,
        count: Int = 2,
        showActionLabels: Boolean = true,
        triggerLabel: String? = null,
    ) {
        ExpandableFAB(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            actions =
                List(count) { index ->
                    FABAction(
                        id = index.toString(),
                        label = "Action $index",
                        icon = AppTheme.icons.check,
                        onClick = { events += "action $index" },
                        enabled = enabled,
                    )
                },
            icon = AppTheme.icons.expand,
            contentDescription = "Actions",
            expandedStateDescription = "Expanded",
            collapsedStateDescription = "Collapsed",
            direction = direction,
            collapseOnAction = collapseOnAction,
            showActionLabels = showActionLabels,
            triggerLabel = triggerLabel,
        )
    }

    private fun setMenu(
        initiallyExpanded: Boolean = false,
        direction: FABExpansionDirection = FABExpansionDirection.Up,
        enabled: Boolean = true,
        collapseOnAction: Boolean = true,
        count: Int = 2,
        layoutDirection: LayoutDirection = LayoutDirection.Ltr,
        fontScale: Float = 1f,
        showActionLabels: Boolean = true,
        triggerLabel: String? = null,
    ) {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                CompositionLocalProvider(
                    LocalLayoutDirection provides layoutDirection,
                    LocalDensity provides Density(LocalDensity.current.density, fontScale = fontScale),
                ) {
                    var expanded by remember { mutableStateOf(initiallyExpanded) }
                    val alignment =
                        when (direction) {
                            FABExpansionDirection.Up, FABExpansionDirection.Start -> Alignment.BottomEnd
                            FABExpansionDirection.Down -> Alignment.TopEnd
                            FABExpansionDirection.End -> Alignment.BottomStart
                        }
                    Box(Modifier.size(320.dp), contentAlignment = alignment) {
                        Menu(
                            expanded = expanded,
                            onExpandedChange = {
                                events += "expanded $it"
                                expanded = it
                            },
                            direction = direction,
                            enabled = enabled,
                            collapseOnAction = collapseOnAction,
                            count = count,
                            showActionLabels = showActionLabels,
                            triggerLabel = triggerLabel,
                        )
                    }
                }
            }
        }
    }

    @Test
    public fun toggleRevealsLabeledActionsAndAnnouncesState() {
        setMenu()
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
        composeRule
            .onNodeWithContentDescription("Actions")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Collapsed"))
            .performClick()
        composeRule.onNodeWithText("Action 0").assertIsDisplayed()
        composeRule
            .onNodeWithContentDescription("Actions")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Expanded"))
            .performClick()
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
        assertEquals(listOf("expanded true", "expanded false"), events)
    }

    @Test
    public fun selectingActionCollapsesBeforeInvokingCallback() {
        setMenu(initiallyExpanded = true)
        composeRule.onNodeWithText("Action 0").performClick()
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
        assertEquals(listOf("expanded false", "action 0"), events)
    }

    @Test
    public fun optionalPersistentMenuStaysOpenAfterSelection() {
        setMenu(initiallyExpanded = true, collapseOnAction = false)
        composeRule.onNodeWithText("Action 0").performClick().assertIsDisplayed()
        assertEquals(listOf("action 0"), events)
    }

    @Test
    public fun disabledActionsIgnoreClicks() {
        setMenu(initiallyExpanded = true, enabled = false)
        composeRule.onNodeWithText("Action 0").assertIsNotEnabled().performClick()
        assertTrue(events.isEmpty())
    }

    @Test
    public fun triggerAndActionsHaveButtonRoleAndMinimumTouchTargets() {
        setMenu(initiallyExpanded = true)
        val button = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)
        composeRule
            .onNodeWithContentDescription("Actions")
            .assert(button)
            .assertHeightIsAtLeast(48.dp)
            .assertWidthIsAtLeast(48.dp)
        composeRule
            .onNodeWithText("Action 0")
            .assert(button)
            .assertHeightIsAtLeast(48.dp)
            .assertWidthIsAtLeast(48.dp)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription))
    }

    @Test
    public fun iconOnlyActionsRetainAccessibleNamesRolesAndTouchTargets() {
        setMenu(initiallyExpanded = true, showActionLabels = false, fontScale = 2f)
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
        composeRule
            .onNodeWithContentDescription("Action 0")
            .assertIsDisplayed()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assertHeightIsAtLeast(48.dp)
            .assertWidthIsAtLeast(48.dp)
            .performClick()
        composeRule.onNodeWithContentDescription("Action 0").assertDoesNotExist()
        assertEquals(listOf("expanded false", "action 0"), events)
    }

    @Test
    public fun disabledIconOnlyActionsIgnoreClicks() {
        setMenu(initiallyExpanded = true, showActionLabels = false, enabled = false)
        composeRule.onNodeWithContentDescription("Action 0").assertIsNotEnabled().performClick()
        assertTrue(events.isEmpty())
    }

    @Test
    public fun iconOnlyPersistentMenuKeepsActionsAvailable() {
        setMenu(initiallyExpanded = true, showActionLabels = false, collapseOnAction = false)
        composeRule.onNodeWithContentDescription("Action 0").performClick().assertIsDisplayed()
        assertEquals(listOf("action 0"), events)
    }

    @Test
    public fun extendedTriggerUsesVisibleLabelAndAnnouncesExpansion() {
        setMenu(showActionLabels = false, triggerLabel = "Menu title", fontScale = 2f)
        composeRule.onNodeWithContentDescription("Actions").assertDoesNotExist()
        composeRule
            .onNodeWithContentDescription("Menu title")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Collapsed"))
            .performClick()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Expanded"))
        composeRule.onNodeWithContentDescription("Action 0").assertIsDisplayed()
    }

    @Test
    public fun blankTriggerLabelUsesIconOnlyTrigger() {
        setMenu(triggerLabel = " ")
        composeRule.onNodeWithText(" ").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Actions").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Action 0").assertIsDisplayed()
    }

    @Test
    public fun emptyActionsLeaveUsableTrigger() {
        setMenu(count = 0)
        composeRule
            .onNodeWithContentDescription("Actions")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
            .performClick()
            .assertIsDisplayed()
        assertEquals(listOf("expanded true"), events)
    }

    @Test
    public fun expansionIsHoisted() {
        composeRule.setContent {
            AppTheme {
                Menu(expanded = false, onExpandedChange = { events += "expanded $it" })
            }
        }
        composeRule.onNodeWithContentDescription("Actions").performClick()
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
        assertEquals(listOf("expanded true"), events)
    }

    @Test
    public fun upKeepsTriggerAnchoredAndRevealsAbove() {
        assertDirection(FABExpansionDirection.Up)
    }

    @Test
    public fun downKeepsTriggerAnchoredAndRevealsBelow() {
        assertDirection(FABExpansionDirection.Down)
    }

    @Test
    public fun startKeepsTriggerAnchoredAndRevealsBefore() {
        assertDirection(FABExpansionDirection.Start)
    }

    @Test
    public fun endKeepsTriggerAnchoredAndRevealsAfter() {
        assertDirection(FABExpansionDirection.End)
    }

    @Test
    public fun startMirrorsInRtl() {
        assertDirection(FABExpansionDirection.Start, LayoutDirection.Rtl)
    }

    @Test
    public fun endMirrorsInRtl() {
        assertDirection(FABExpansionDirection.End, LayoutDirection.Rtl)
    }

    @Test
    public fun horizontalTriggerStaysAnchoredAtDoubleFontScale() {
        assertDirection(FABExpansionDirection.Start, fontScale = 2f)
    }

    @Test
    public fun iconOnlyMenuWithExtendedTriggerStaysAnchoredInRtlAtDoubleFontScale() {
        assertDirection(
            FABExpansionDirection.Start,
            LayoutDirection.Rtl,
            fontScale = 2f,
            showActionLabels = false,
            triggerLabel = "Actions",
        )
    }

    @Test
    public fun iconOnlyEndMenuStaysAnchored() {
        assertDirection(FABExpansionDirection.End, showActionLabels = false)
    }

    private fun assertDirection(
        direction: FABExpansionDirection,
        layoutDirection: LayoutDirection = LayoutDirection.Ltr,
        fontScale: Float = 1f,
        showActionLabels: Boolean = true,
        triggerLabel: String? = null,
    ) {
        setMenu(
            direction = direction,
            layoutDirection = layoutDirection,
            count = 1,
            fontScale = fontScale,
            showActionLabels = showActionLabels,
            triggerLabel = triggerLabel,
        )
        val trigger = composeRule.onNodeWithContentDescription(triggerLabel ?: "Actions")
        val before = trigger.getUnclippedBoundsInRoot()
        trigger.performClick()
        assertEquals(before, trigger.getUnclippedBoundsInRoot())
        val action =
            if (showActionLabels) {
                composeRule.onNodeWithText("Action 0").getUnclippedBoundsInRoot()
            } else {
                composeRule.onNodeWithContentDescription("Action 0").getUnclippedBoundsInRoot()
            }
        when (direction) {
            FABExpansionDirection.Up -> assertTrue(action.bottom <= before.top)
            FABExpansionDirection.Down -> assertTrue(action.top >= before.bottom)
            FABExpansionDirection.Start ->
                if (layoutDirection == LayoutDirection.Ltr) {
                    assertTrue(action.right <= before.left)
                } else {
                    assertTrue(action.left >= before.right)
                }
            FABExpansionDirection.End ->
                if (layoutDirection == LayoutDirection.Ltr) {
                    assertTrue(action.left >= before.right)
                } else {
                    assertTrue(action.right <= before.left)
                }
        }
    }

    @Test
    public fun horizontalOverflowScrollsWithoutHidingTrigger() {
        setMenu(initiallyExpanded = true, direction = FABExpansionDirection.Start, count = 6)
        composeRule
            .onNodeWithText("Action 5")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithContentDescription("Actions").assertIsDisplayed()
        assertEquals(listOf("expanded false", "action 5"), events)
    }

    @Test
    public fun verticalOverflowScrollsWithoutHidingTrigger() {
        setMenu(initiallyExpanded = true, count = 10)
        composeRule.onNodeWithText("Action 9").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Actions").assertIsDisplayed()
    }

    @Test
    public fun worksInVerticallyScrollingParent() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Column(Modifier.size(320.dp).verticalScroll(rememberScrollState())) {
                    Menu(expanded = true, onExpandedChange = {})
                }
            }
        }
        composeRule.onNodeWithText("Action 0").assertIsDisplayed()
        composeRule.onNodeWithText("Action 1").assertIsDisplayed()
    }

    @Test
    public fun worksInHorizontallyScrollingParent() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Row(Modifier.size(320.dp).horizontalScroll(rememberScrollState())) {
                    Menu(expanded = true, onExpandedChange = {}, direction = FABExpansionDirection.Start)
                }
            }
        }
        composeRule.onNodeWithText("Action 0").assertIsDisplayed()
        composeRule.onNodeWithText("Action 1").assertIsDisplayed()
    }

    @Test
    public fun overflowStartsAtTheEndNearestTheTrigger() {
        setMenu(initiallyExpanded = true, count = 10)
        composeRule.onNodeWithText("Action 9").assertIsDisplayed()
    }

    @Test
    public fun horizontalOverflowStartsAtTheEndNearestTheTrigger() {
        setMenu(initiallyExpanded = true, direction = FABExpansionDirection.Start, count = 6)
        composeRule.onNodeWithText("Action 5").assertIsDisplayed()
    }

    @Test
    public fun duplicateIdsAreRejected() {
        val failure =
            runCatching {
                composeRule.setContent {
                    AppTheme(icons = defaultAppIcons()) {
                        val action = FABAction("same", "Label", AppTheme.icons.check, {})
                        ExpandableFAB(
                            expanded = false,
                            onExpandedChange = {},
                            actions = listOf(action, action),
                            icon = AppTheme.icons.expand,
                            contentDescription = "Actions",
                            expandedStateDescription = "Expanded",
                            collapsedStateDescription = "Collapsed",
                        )
                    }
                }
            }
        assertTrue(failure.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    public fun interruptedTransitionCanReverseAndExitingActionsLoseSemantics() {
        setMenu()
        composeRule.mainClock.autoAdvance = false
        val trigger = composeRule.onNodeWithContentDescription("Actions")
        trigger.performClick()
        composeRule.mainClock.advanceTimeBy(96)
        trigger.performClick()
        composeRule.mainClock.advanceTimeByFrame()
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
        trigger.performClick()
        composeRule.mainClock.advanceTimeBy(1000)
        composeRule.onNodeWithText("Action 0").assertIsDisplayed()
        trigger.performClick()
        composeRule.mainClock.advanceTimeBy(1000)
        composeRule.onNodeWithText("Action 0").assertDoesNotExist()
    }
}
