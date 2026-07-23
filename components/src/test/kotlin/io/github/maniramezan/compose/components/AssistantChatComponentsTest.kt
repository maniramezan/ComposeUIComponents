package io.github.maniramezan.compose.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.v2.createComposeRule
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
public class AssistantChatComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun contextCardSkipsBlankOptionalContent() {
        composeRule.setContent {
            AppTheme {
                AssistantContextCard(
                    title = "Subject",
                    highlight = "",
                    body = AssistantContextBody(""),
                    footnote = null,
                )
            }
        }

        composeRule.onNodeWithText("Subject").assertIsDisplayed()
    }

    @Test
    public fun quickActionChipsUseCallerStateAndCallbacks() {
        var selectedAction: String? = null

        composeRule.setContent {
            AppTheme {
                AssistantQuickActionChips(
                    actions = listOf("Explain", "Examples"),
                    actionState = { action ->
                        AssistantQuickActionState(
                            isSelected = action == "Explain",
                            isEnabled = action != "Examples",
                        )
                    },
                    label = { it },
                    onAction = { selectedAction = it },
                )
            }
        }

        composeRule.onNodeWithText("Explain").assertIsEnabled().performClick()
        composeRule.onNodeWithText("Examples").assertIsNotEnabled()
        composeRule.runOnIdle {
            assert(selectedAction == "Explain") { "Expected Explain, got $selectedAction" }
        }
    }

    @Test
    public fun quickActionsCanBeHiddenAfterUseAndRenewedByCaller() {
        val actions = mutableStateOf(listOf("Explain"))

        composeRule.setContent {
            AppTheme {
                AssistantQuickActionChips(
                    actions = actions.value,
                    actionState = { AssistantQuickActionState(isSelected = false, isEnabled = true) },
                    label = { it },
                    onAction = { actions.value = emptyList() },
                )
            }
        }

        composeRule.onNodeWithText("Explain").performClick()
        composeRule.onNodeWithText("Explain").assertDoesNotExist()
        composeRule.runOnIdle { actions.value = listOf("Explain") }
        composeRule.onNodeWithText("Explain").assertIsDisplayed()
    }

    @Test
    public fun limitPromptRendersCallerCopyAndActions() {
        composeRule.setContent {
            AppTheme {
                AssistantLimitPromptCard(
                    copy =
                        AssistantLimitPromptCopy(
                            message = "Limit reached",
                            supportingText = "Try again tomorrow or upgrade.",
                            primaryActionLabel = "Upgrade",
                            secondaryActionLabel = "Not now",
                        ),
                    onPrimaryAction = {},
                    onSecondaryAction = {},
                )
            }
        }

        composeRule.onNodeWithText("Limit reached").assertIsDisplayed()
        composeRule.onNodeWithText("Try again tomorrow or upgrade.").assertIsDisplayed()
        composeRule.onNodeWithText("Upgrade").assertIsDisplayed()
        composeRule.onNodeWithText("Not now").assertIsDisplayed()
    }

    @Test
    public fun blankDisclaimerDoesNotRenderTextNode() {
        composeRule.setContent {
            AppTheme {
                AssistantDisclaimerFooter(text = "")
            }
        }

        composeRule.onNodeWithText("").assertDoesNotExist()
    }
}
