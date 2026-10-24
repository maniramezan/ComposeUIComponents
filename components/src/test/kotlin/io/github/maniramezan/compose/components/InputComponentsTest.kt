package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class InputComponentsTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    @Test
    public fun textAndPasswordFieldsForwardKeyboardOptions() {
        composeRule.setContent {
            Column {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = "Search",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                )
                PasswordField(
                    value = "",
                    onValueChange = {},
                    label = "Password",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                )
            }
        }

        composeRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, ImeAction.Search)).assertExists()
        composeRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, ImeAction.Done)).assertExists()
    }

    @Test
    public fun searchFieldForwardsTypedTextAndShowsPlaceholderWhenEmpty() {
        composeRule.setContent {
            var query by remember { mutableStateOf("") }
            SearchField(value = query, onValueChange = { query = it }, placeholder = "Find items")
        }

        composeRule.onNodeWithText("Find items").assertExists()
        composeRule.onNode(hasSetTextAction()).performTextInput("abc")
        composeRule.onNode(hasSetTextAction()).assert(hasText("abc"))
    }

    @Test
    public fun disabledSearchFieldIsNotEnabled() {
        composeRule.setContent {
            SearchField(value = "", onValueChange = {}, placeholder = "Find items", enabled = false)
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.EditableText)).assertIsNotEnabled()
    }

    @Test
    public fun searchFieldWithBlankPlaceholderRendersNoPlaceholderNode() {
        composeRule.setContent {
            SearchField(value = "", onValueChange = {})
        }

        composeRule
            .onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.Text), useUnmergedTree = true)
            .assertCountEquals(0)
    }

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
                Column {
                    Checkbox(checked = agreed, onCheckedChange = { agreed = it }, label = "Agree")
                    Switch(checked = enabled, onCheckedChange = { enabled = it }, label = "Enabled")
                }
            }
        }

        composeRule.onNodeWithText("Agree").assertIsOff().performClick()
        composeRule.onNodeWithText("Agree").assertIsOn()
        composeRule.onNodeWithText("Enabled").assertIsOn().performClick()
        composeRule.onNodeWithText("Enabled").assertIsOff()
    }
}
