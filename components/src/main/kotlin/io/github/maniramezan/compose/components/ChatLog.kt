package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import io.github.maniramezan.compose.theme.AppTheme

private const val ChatBubbleMaxWidthFraction = 0.85f

/** Sender alignment and color treatment for a chat message bubble. */
public enum class ChatMessageSender {
    User,
    Assistant,
}

/** Visual/behavioral state for a chat message bubble. */
public sealed interface ChatMessageState {
    /** Message text is available and should be rendered by the content slot. */
    public data class Content(
        val text: String,
    ) : ChatMessageState

    /** Response is pending; renders an indeterminate typing/waiting indicator. */
    public data object Typing : ChatMessageState

    /** Message failed; renders [ChatErrorAction] copy and retry action. */
    public data object Error : ChatMessageState
}

/** Stable, product-agnostic chat-log item. */
@Immutable
public data class ChatMessage(
    val id: String,
    val sender: ChatMessageSender,
    val state: ChatMessageState,
)

/** Caller-supplied error copy and retry behavior for failed chat bubbles. */
public data class ChatErrorAction(
    val message: String,
    val retryLabel: String,
    val onRetry: () -> Unit,
)

/**
 * Reusable chat-style conversation log with user/assistant bubbles, a typing
 * indicator, and retryable error state.
 *
 * User-visible and accessibility strings are caller-supplied. Pass
 * [messageContent] to render rich text, markdown, links, or app-specific
 * formatting while keeping the design-system component dependency-free. Supply
 * [typingDescription] to announce a pending assistant response to screen readers.
 */
@Composable
public fun ChatLog(
    messages: List<ChatMessage>,
    idleHint: String,
    errorAction: ChatErrorAction,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = AppTheme.spacing.x1),
    typingDescription: String = "",
    messageContent: @Composable (ChatMessageSender, String) -> Unit = { sender, text ->
        ChatMessageText(sender = sender, text = text)
    },
) {
    if (messages.isEmpty()) {
        ChatLogIdleHint(
            idleHint = idleHint,
            modifier = modifier,
        )
        return
    }

    val listState = rememberLazyListState()
    val isAtLatestMessage by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            layoutInfo.totalItemsCount == 0 ||
                // An appended message briefly makes the previous final item one
                // position behind the end, so treat that as still following.
                (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1) >= layoutInfo.totalItemsCount - 2
        }
    }
    val lastMessageState = messages.lastOrNull()?.state
    LaunchedEffect(messages.size, lastMessageState) {
        // Preserve a reader's position in history; only follow updates when they
        // were already reading the newest message.
        if (isAtLatestMessage) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
    ) {
        items(messages, key = { it.id }) { message ->
            ChatBubble(
                message = message,
                errorAction = errorAction,
                typingDescription = typingDescription,
                content = messageContent,
            )
        }
    }
}

@Composable
private fun ChatLogIdleHint(
    idleHint: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (idleHint.isNotBlank()) {
            Text(
                text = idleHint,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    errorAction: ChatErrorAction,
    typingDescription: String,
    content: @Composable (ChatMessageSender, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalArrangement =
        when (message.sender) {
            ChatMessageSender.User -> Arrangement.End
            ChatMessageSender.Assistant -> Arrangement.Start
        }
    val contentAlignment =
        when (message.sender) {
            ChatMessageSender.User -> Alignment.CenterEnd
            ChatMessageSender.Assistant -> Alignment.CenterStart
        }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = horizontalArrangement) {
        Box(modifier = Modifier.fillMaxWidth(ChatBubbleMaxWidthFraction), contentAlignment = contentAlignment) {
            when (message.state) {
                is ChatMessageState.Content -> ChatContentBubble(message = message, content = content)
                ChatMessageState.Typing -> ChatTypingBubble(typingDescription = typingDescription)
                ChatMessageState.Error -> ChatErrorBubble(errorAction = errorAction)
            }
        }
    }
}

@Composable
private fun ChatContentBubble(
    message: ChatMessage,
    content: @Composable (ChatMessageSender, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isUser = message.sender == ChatMessageSender.User
    Card(
        modifier = modifier,
        shape = AppTheme.shapes.large,
        colors =
            CardDefaults.cardColors(
                containerColor = if (isUser) AppTheme.colors.primaryContainer else AppTheme.colors.surfaceVariant,
            ),
    ) {
        Box(modifier = Modifier.padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1_5)) {
            content(message.sender, (message.state as ChatMessageState.Content).text)
        }
    }
}

@Composable
private fun ChatMessageText(
    sender: ChatMessageSender,
    text: String,
    modifier: Modifier = Modifier,
) {
    val color =
        when (sender) {
            ChatMessageSender.User -> AppTheme.colors.onPrimaryContainer
            ChatMessageSender.Assistant -> AppTheme.colors.onSurfaceVariant
        }
    Text(
        text = text,
        modifier = modifier,
        style = AppTheme.typography.bodyLarge,
        color = color,
    )
}

@Composable
private fun ChatTypingBubble(
    typingDescription: String,
    modifier: Modifier = Modifier,
) {
    val semanticsModifier =
        if (typingDescription.isNotBlank()) {
            Modifier.semantics {
                contentDescription = typingDescription
                liveRegion = LiveRegionMode.Polite
            }
        } else {
            Modifier.clearAndSetSemantics {}
        }
    Card(
        modifier = modifier.then(semanticsModifier),
        shape = AppTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceVariant),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(AppTheme.spacing.x2).size(AppTheme.spacing.x2),
            strokeWidth = AppTheme.spacing.half,
            color = AppTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun ChatErrorBubble(
    errorAction: ChatErrorAction,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surfaceVariant)
                .padding(AppTheme.spacing.x2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
    ) {
        Text(
            text = errorAction.message,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.error,
        )
        TextButton(onClick = errorAction.onRetry) {
            Text(text = errorAction.retryLabel)
        }
    }
}
