package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
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
    public fun textAndPasswordFieldsForwardKeyboardOptions() {
        composeRule.setContent {
            AppTheme {
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
        }

        composeRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, ImeAction.Search)).assertExists()
        composeRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.ImeAction, ImeAction.Done)).assertExists()
    }

    @Test
    public fun searchFieldForwardsTypedTextAndShowsPlaceholderWhenEmpty() {
        composeRule.setContent {
            AppTheme {
                var query by remember { mutableStateOf("") }
                SearchField(value = query, onValueChange = { query = it }, placeholder = "Find items")
            }
        }

        composeRule.onNodeWithText("Find items").assertExists()
        composeRule.onNode(hasSetTextAction()).performTextInput("abc")
        composeRule.onNode(hasSetTextAction()).assert(hasText("abc"))
    }

    @Test
    public fun disabledSearchFieldIsNotEnabled() {
        composeRule.setContent {
            AppTheme {
                SearchField(value = "", onValueChange = {}, placeholder = "Find items", enabled = false)
            }
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.EditableText)).assertIsNotEnabled()
    }
}
