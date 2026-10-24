package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class SelectableCardTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    @Test
    public fun tapInvokesOnClickOnce() {
        var clicks = 0
        composeRule.setContent {
            SelectableCard(selected = false, onClick = { clicks++ }, modifier = Modifier.testTag(CARD)) {
                Text("Monthly")
            }
        }

        composeRule.onNodeWithTag(CARD).performClick()

        composeRule.runOnIdle { assertThat(clicks).isEqualTo(1) }
    }

    @Test
    public fun selectedStateIsExposedToSemantics() {
        composeRule.setContent {
            Column {
                SelectableCard(selected = true, onClick = {}, modifier = Modifier.testTag("on")) { Text("On") }
                SelectableCard(selected = false, onClick = {}, modifier = Modifier.testTag("off")) { Text("Off") }
            }
        }

        composeRule.onNodeWithTag("on").assertIsSelected()
        composeRule.onNodeWithTag("off").assertIsNotSelected()
    }

    @Test
    public fun choosingAnotherOptionMovesTheSelection() {
        composeRule.setContent {
            var current by remember { mutableStateOf(0) }
            Column(modifier = Modifier.selectableGroup()) {
                listOf("Monthly", "Yearly").forEachIndexed { index, name ->
                    SelectableCard(
                        selected = current == index,
                        onClick = { current = index },
                        modifier = Modifier.testTag(name),
                    ) {
                        Text(name)
                    }
                }
            }
        }

        composeRule.onNodeWithTag("Monthly").assertIsSelected()
        composeRule.onNodeWithTag("Yearly").assertIsNotSelected()

        composeRule.onNodeWithTag("Yearly").performClick()

        composeRule.onNodeWithTag("Yearly").assertIsSelected()
        composeRule.onNodeWithTag("Monthly").assertIsNotSelected()
    }

    @Test
    public fun selectedCardCanBeDeselectedByTheCaller() {
        composeRule.setContent {
            var checked by remember { mutableStateOf(true) }
            SelectableCard(
                selected = checked,
                onClick = { checked = !checked },
                modifier = Modifier.testTag(CARD),
                role = Role.Checkbox,
            ) {
                Text("Add-on")
            }
        }

        composeRule.onNodeWithTag(CARD).assertIsSelected()
        composeRule.onNodeWithTag(CARD).performClick()
        composeRule.onNodeWithTag(CARD).assertIsNotSelected()
    }

    @Test
    public fun defaultRoleIsRadioButton() {
        composeRule.setContent {
            SelectableCard(selected = false, onClick = {}, modifier = Modifier.testTag(CARD)) { Text("Plan") }
        }

        composeRule.onNodeWithTag(CARD).assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
    }

    @Test
    public fun roleCanBeOverriddenForMultiSelect() {
        composeRule.setContent {
            SelectableCard(selected = false, onClick = {}, modifier = Modifier.testTag(CARD), role = Role.Checkbox) {
                Text("Plan")
            }
        }

        composeRule.onNodeWithTag(CARD).assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
    }

    @Test
    public fun disabledCardIgnoresTapsAndReportsDisabled() {
        var clicks = 0
        composeRule.setContent {
            SelectableCard(
                selected = true,
                onClick = { clicks++ },
                modifier = Modifier.testTag(CARD),
                enabled = false,
            ) {
                Text("Unavailable")
            }
        }

        composeRule
            .onNodeWithTag(CARD)
            .assertIsNotEnabled()
            .assertIsSelected()
            .performClick()

        composeRule.runOnIdle { assertThat(clicks).isEqualTo(0) }
    }

    @Test
    public fun enabledCardReportsEnabled() {
        composeRule.setContent {
            SelectableCard(selected = false, onClick = {}, modifier = Modifier.testTag(CARD)) { Text("Plan") }
        }

        composeRule.onNodeWithTag(CARD).assertIsEnabled()
    }

    @Test
    public fun callerStateDescriptionIsExposed() {
        composeRule.setContent {
            SelectableCard(
                selected = true,
                onClick = {},
                modifier = Modifier.testTag(CARD),
                stateDescription = "Current plan",
            ) {
                Text("Monthly")
            }
        }

        composeRule
            .onNodeWithTag(CARD)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Current plan"))
    }

    @Test
    public fun blankOrMissingStateDescriptionAddsNoCustomState() {
        composeRule.setContent {
            Column {
                SelectableCard(selected = true, onClick = {}, modifier = Modifier.testTag("none")) { Text("None") }
                SelectableCard(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.testTag("blank"),
                    stateDescription = "  ",
                ) {
                    Text("Blank")
                }
            }
        }

        composeRule.onNodeWithTag("none").assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
        composeRule.onNodeWithTag("blank").assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
    }

    @Test
    public fun contentIsMergedIntoOneNode() {
        composeRule.setContent {
            SelectableCard(selected = false, onClick = {}, modifier = Modifier.testTag(CARD)) {
                Text("Monthly")
                Text("Billed every month")
            }
        }

        composeRule.onNodeWithText("Monthly").assertExists()
        composeRule.onNodeWithText("Billed every month").assertExists()
        composeRule.onNodeWithTag(CARD).assert(hasMergedChildText("Monthly", "Billed every month"))
    }

    @Test
    public fun smallContentStillMeetsMinimumTouchTarget() {
        composeRule.setContent {
            SelectableCard(
                selected = false,
                onClick = {},
                modifier = Modifier.testTag(CARD),
                contentPadding = PaddingValues(),
            ) {
                Text("A")
            }
        }

        composeRule
            .onNodeWithTag(CARD)
            .assertWidthIsAtLeast(MIN_TARGET)
            .assertHeightIsAtLeast(MIN_TARGET)
    }

    @Test
    public fun togglingSelectionDoesNotChangeSizeOrContentPosition() {
        var selected by mutableStateOf(false)
        composeRule.setContent {
            SelectableCard(
                selected = selected,
                onClick = {},
                modifier = Modifier.fillMaxWidth().testTag(CARD),
            ) {
                Text(text = "Monthly", modifier = Modifier.testTag(LABEL))
            }
        }

        val cardBefore = composeRule.onNodeWithTag(CARD).getUnclippedBoundsInRoot()
        val labelBefore = composeRule.onNodeWithTag(LABEL, useUnmergedTree = true).getUnclippedBoundsInRoot()

        composeRule.runOnIdle { selected = true }
        composeRule.waitForIdle()

        val cardAfter = composeRule.onNodeWithTag(CARD).getUnclippedBoundsInRoot()
        val labelAfter = composeRule.onNodeWithTag(LABEL, useUnmergedTree = true).getUnclippedBoundsInRoot()

        assertThat(cardAfter).isEqualTo(cardBefore)
        assertThat(labelAfter).isEqualTo(labelBefore)
    }

    private fun hasMergedChildText(vararg texts: String): SemanticsMatcher =
        SemanticsMatcher("merged text contains ${texts.toList()}") { node ->
            val merged =
                node.config
                    .getOrNull(SemanticsProperties.Text)
                    .orEmpty()
                    .map { it.text }
            texts.all { it in merged }
        }

    private companion object {
        const val CARD = "card"
        const val LABEL = "label"
        val MIN_TARGET = 48.dp
    }
}
