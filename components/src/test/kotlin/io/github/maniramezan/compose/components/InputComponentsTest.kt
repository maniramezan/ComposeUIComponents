package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.v2.createComposeRule
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
}
