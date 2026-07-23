package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Chat Log", group = "Feedback")
@Composable
public fun ChatLogPreview(): Unit =
    AppTheme {
        ChatLog(
            messages = previewChatMessages,
            idleHint = "Ask a question to get started.",
            typingDescription = "Assistant is responding",
            errorAction =
                ChatErrorAction(
                    message = "Could not load the response.",
                    retryLabel = "Retry",
                    onRetry = {},
                ),
        )
    }

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Assistant Chat Surface", group = "Feedback")
@Composable
public fun AssistantChatSurfacePreview(): Unit =
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            AssistantStatusBanner(message = "The assistant is temporarily unavailable.")
            AssistantContextCard(
                title = "Context title",
                highlight = "Highlighted context",
                body =
                    AssistantContextBody(
                        text = "This is the source material the assistant will use.",
                        isQuoted = true,
                    ),
                footnote = "From current screen",
            )
            ChatLog(
                messages = previewChatMessages.take(4),
                idleHint = "Ask a question to get started.",
                typingDescription = "Assistant is responding",
                errorAction = previewChatErrorAction,
                modifier = Modifier.weight(1f),
            )
            AssistantLimitPromptCard(
                copy =
                    AssistantLimitPromptCopy(
                        message = "You reached today's assistant limit.",
                        supportingText = "Upgrade for more assistant responses across your projects.",
                        primaryActionLabel = "Upgrade",
                        secondaryActionLabel = "Not now",
                    ),
                onPrimaryAction = {},
                onSecondaryAction = {},
            )
            AssistantQuickActionChips(
                actions = previewQuickActions,
                actionState = { action ->
                    AssistantQuickActionState(
                        isSelected = action == "Summarize",
                        isEnabled = action != "Translate",
                    )
                },
                label = { it },
                onAction = {},
            )
            AssistantDisclaimerFooter(text = "Assistant responses may be incomplete. Review before using.")
        }
    }

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Assistant Quick Actions", group = "Feedback")
@Composable
public fun AssistantQuickActionChipsPreview(): Unit =
    AppTheme {
        AssistantQuickActionChips(
            actions = previewQuickActions,
            actionState = { action ->
                AssistantQuickActionState(
                    isSelected = action == "Explain",
                    isEnabled = action != "Examples",
                )
            },
            label = { it },
            onAction = {},
        )
    }

internal val previewChatMessages =
    listOf(
        ChatMessage(
            id = "user-1",
            sender = ChatMessageSender.User,
            state = ChatMessageState.Content("Summarize this article"),
        ),
        ChatMessage(
            id = "assistant-1",
            sender = ChatMessageSender.Assistant,
            state = ChatMessageState.Content("Here are the main points in a concise summary."),
        ),
        ChatMessage(
            id = "user-2",
            sender = ChatMessageSender.User,
            state = ChatMessageState.Content("Give me examples"),
        ),
        ChatMessage(
            id = "assistant-2",
            sender = ChatMessageSender.Assistant,
            state = ChatMessageState.Typing,
        ),
        ChatMessage(
            id = "assistant-error",
            sender = ChatMessageSender.Assistant,
            state = ChatMessageState.Error,
        ),
    )

private val previewChatErrorAction =
    ChatErrorAction(
        message = "Could not load the response.",
        retryLabel = "Retry",
        onRetry = {},
    )

private val previewQuickActions = listOf("Summarize", "Explain", "Examples", "Translate")
