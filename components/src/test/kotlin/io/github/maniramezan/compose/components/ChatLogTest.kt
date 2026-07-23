package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ChatLogTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun rendersIdleHintWhenMessagesAreEmpty() {
        composeRule.setContent {
            AppTheme {
                ChatLog(
                    messages = emptyList(),
                    idleHint = "Start a conversation",
                    errorAction = ChatErrorAction("Failed", "Retry", {}),
                )
            }
        }

        composeRule.onNodeWithText("Start a conversation").assertIsDisplayed()
    }

    @Test
    public fun blankIdleHintDoesNotCreateEmptyVisibleText() {
        composeRule.setContent {
            AppTheme {
                ChatLog(
                    messages = emptyList(),
                    idleHint = "",
                    errorAction = ChatErrorAction("Failed", "Retry", {}),
                )
            }
        }

        composeRule.onAllNodesWithText("").assertCountEquals(0)
    }

    @Test
    public fun rendersContentAndErrorMessages() {
        composeRule.setContent {
            AppTheme {
                ChatLog(
                    messages =
                        listOf(
                            ChatMessage("1", ChatMessageSender.User, ChatMessageState.Content("Hello")),
                            ChatMessage("2", ChatMessageSender.Assistant, ChatMessageState.Content("Hi there")),
                            ChatMessage("3", ChatMessageSender.Assistant, ChatMessageState.Error),
                        ),
                    idleHint = "Start",
                    errorAction = ChatErrorAction("Could not respond", "Retry", {}),
                )
            }
        }

        composeRule.onNodeWithText("Hello").assertIsDisplayed()
        composeRule.onNodeWithText("Hi there").assertIsDisplayed()
        composeRule.onNodeWithText("Could not respond").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    public fun messageContentSlotCanOverrideTextRendering() {
        composeRule.setContent {
            AppTheme {
                ChatLog(
                    messages = listOf(ChatMessage("1", ChatMessageSender.Assistant, ChatMessageState.Content("raw"))),
                    idleHint = "Start",
                    errorAction = ChatErrorAction("Failed", "Retry", {}),
                    messageContent = { _, text -> Text("Formatted $text") },
                )
            }
        }

        composeRule.onNodeWithText("Formatted raw").assertIsDisplayed()
    }

    @Test
    public fun typingStateUsesCallerSuppliedAccessibilityDescription() {
        composeRule.setContent {
            AppTheme {
                ChatLog(
                    messages = listOf(ChatMessage("1", ChatMessageSender.Assistant, ChatMessageState.Typing)),
                    idleHint = "Start",
                    typingDescription = "Assistant is responding",
                    errorAction = ChatErrorAction("Failed", "Retry", {}),
                )
            }
        }

        composeRule.onNodeWithContentDescription("Assistant is responding").assertIsDisplayed()
    }
}
