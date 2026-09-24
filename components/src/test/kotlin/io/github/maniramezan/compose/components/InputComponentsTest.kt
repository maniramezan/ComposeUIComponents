package io.github.maniramezan.compose.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class InputComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun radioGroupRowsExposeRadioButtonRoleInsideASelectableGroup() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableIntStateOf(0) }
                RadioGroup(
                    options = listOf("One", "Two"),
                    selectedIndex = selected,
                    onOptionSelected = { selected = it },
                    modifier = Modifier.testTag("group"),
                )
            }
        }

        composeRule
            .onNodeWithTag("group")
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.SelectableGroup))
        composeRule
            .onNodeWithText("Two")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
            .assertIsNotSelected()
            .performClick()
        composeRule.onNodeWithText("Two").assertIsSelected()
        composeRule.onNodeWithText("One").assertIsNotSelected()
    }

    @Test
    public fun checkboxAndSwitchToggleFromTheWholeRow() {
        composeRule.setContent {
            AppTheme {
                var agreed by remember { mutableStateOf(false) }
                var enabled by remember { mutableStateOf(true) }
                Checkbox(checked = agreed, onCheckedChange = { agreed = it }, label = "Agree")
                Switch(checked = enabled, onCheckedChange = { enabled = it }, label = "Enabled")
            }
        }

        composeRule.onNodeWithText("Agree").assertIsOff().performClick()
        composeRule.onNodeWithText("Agree").assertIsOn()
        composeRule.onNodeWithText("Enabled").assertIsOn().performClick()
        composeRule.onNodeWithText("Enabled").assertIsOff()
    }

    @Test
    public fun searchFieldWithBlankPlaceholderAddsNoEmptyTextNode() {
        composeRule.setContent {
            AppTheme {
                SearchField(value = "", onValueChange = {})
            }
        }

        // Match only the display Text property: the field's own EditableText is legitimately "".
        val emptyTextNode =
            SemanticsMatcher("has an empty Text node") { node ->
                node.config.getOrNull(SemanticsProperties.Text)?.any { it.text.isEmpty() } == true
            }
        composeRule.onAllNodes(emptyTextNode, useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    public fun searchFieldShowsASuppliedPlaceholder() {
        composeRule.setContent {
            AppTheme {
                SearchField(value = "", onValueChange = {}, placeholder = "Search items")
            }
        }

        composeRule.onNodeWithText("Search items", useUnmergedTree = true).assertExists()
    }
}
