package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
import io.github.maniramezan.compose.components.AssistantContextBody
import io.github.maniramezan.compose.components.AssistantContextCard
import io.github.maniramezan.compose.components.AssistantDisclaimerFooter
import io.github.maniramezan.compose.components.AssistantLimitPromptCard
import io.github.maniramezan.compose.components.AssistantLimitPromptCopy
import io.github.maniramezan.compose.components.AssistantQuickActionChips
import io.github.maniramezan.compose.components.AssistantQuickActionState
import io.github.maniramezan.compose.components.AssistantStatusBanner
import io.github.maniramezan.compose.components.ChatErrorAction
import io.github.maniramezan.compose.components.ChatLog
import io.github.maniramezan.compose.components.ChatMessage
import io.github.maniramezan.compose.components.ChatMessageSender
import io.github.maniramezan.compose.components.ChatMessageState
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.ProgressIndicator
import io.github.maniramezan.compose.components.Skeleton
import io.github.maniramezan.compose.components.SkeletonBlock
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.rememberTypewriterReveal
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// Feedback
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun ProgressIndicatorPage() {
    var determinate by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0.45f) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        ProgressIndicator(
            progress = if (determinate) progress else null,
            label =
                if (determinate) {
                    "Progress: ${(progress * 100).toInt()}%"
                } else {
                    "Loading…"
                },
        )
        ControlsDivider()
        ControlSwitch(
            label = "Determinate",
            checked = determinate,
            onCheckedChange = { determinate = it },
        )
        if (determinate) {
            ControlSlider(
                label = "Progress: ${(progress * 100).toInt()}%",
                value = progress,
                onValueChange = { progress = it },
            )
        }
    }
}

@Composable
internal fun SkeletonPage() {
    var count by remember { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        repeat(count) { Skeleton() }
        ControlsDivider()
        ControlSegmented(
            label = "Count",
            options = listOf("1", "2", "3"),
            selectedIndex = count - 1,
            onOptionSelected = { count = it + 1 },
        )
    }
}

@Composable
internal fun SkeletonBlockPage() {
    var heightFraction by remember { mutableFloatStateOf(0.5f) }
    var widthFraction by remember { mutableFloatStateOf(0.5f) }
    val height: Dp = (40 + (heightFraction * 120)).dp
    val width: Dp = (40 + (widthFraction * 200)).dp

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        SkeletonBlock(height = height, width = width)
        ControlsDivider()
        ControlSlider(
            label = "Height: ${height.value.toInt()} dp",
            value = heightFraction,
            onValueChange = { heightFraction = it },
        )
        ControlSlider(
            label = "Width: ${width.value.toInt()} dp",
            value = widthFraction,
            onValueChange = { widthFraction = it },
        )
    }
}

private val TYPEWRITER_DEMO_WORDS =
    "The quick brown fox jumps over the lazy dog, revealing itself one word at a time."
        .split(" ")

@Composable
internal fun TypewriterRevealPage() {
    var streamRunId by remember { mutableIntStateOf(0) }
    var streamedText by remember { mutableStateOf("") }
    var charsPerSecond by remember { mutableFloatStateOf(30f) }

    // Simulates a token stream (e.g. an LLM response) arriving a word at a time, so the
    // reveal below has a growing `text` to catch up to instead of one instant chunk.
    LaunchedEffect(streamRunId) {
        streamedText = ""
        for (word in TYPEWRITER_DEMO_WORDS) {
            delay(TYPEWRITER_DEMO_CHUNK_DELAY_MS)
            streamedText = if (streamedText.isEmpty()) word else "$streamedText $word"
        }
    }

    val revealed by rememberTypewriterReveal(text = streamedText, charsPerSecond = charsPerSecond.toInt())

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        AppText(text = revealed.ifEmpty { " " }, style = AppTextStyle.Body)
        ControlsDivider()
        ControlSlider(
            label = "Speed: ${charsPerSecond.toInt()} chars/sec",
            value = charsPerSecond,
            onValueChange = { charsPerSecond = it },
            valueRange = TYPEWRITER_DEMO_SPEED_RANGE,
        )
        PrimaryButton(text = "Restart stream", onClick = { streamRunId++ })
    }
}

@Composable
internal fun AssistantChatPage() {
    var mode by remember { mutableStateOf(AssistantSampleMode.Typing) }
    var showStatusBanner by remember { mutableStateOf(false) }
    var showLimitPrompt by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf(AssistantSampleAction.SUMMARIZE) }
    var usedActions by remember { mutableStateOf(setOf(AssistantSampleAction.SUMMARIZE)) }

    val messages =
        when (mode) {
            AssistantSampleMode.Idle -> emptyList()
            AssistantSampleMode.Typing -> assistantSampleMessages(ChatMessageState.Typing)
            AssistantSampleMode.Complete ->
                assistantSampleMessages(
                    ChatMessageState.Content(
                        "Here is a reusable assistant response. Apps can swap this text renderer for Markdown.",
                    ),
                )
            AssistantSampleMode.Error -> assistantSampleMessages(ChatMessageState.Error)
        }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
        if (showStatusBanner) {
            AssistantStatusBanner(message = "The assistant provider is temporarily unavailable.")
        }

        AssistantContextCard(
            title = "Reusable assistant surface",
            highlight = selectedAction.label,
            body =
                AssistantContextBody(
                    text = "Use this page to validate quick actions, typing, completed responses, errors, and limits.",
                    isQuoted = true,
                ),
            footnote = "Sample app validation surface",
        )

        ChatLog(
            messages = messages,
            idleHint = "Choose a quick action to start a conversation.",
            typingDescription = "Assistant is responding",
            errorAction =
                ChatErrorAction(
                    message = "The assistant could not respond.",
                    retryLabel = "Retry",
                    onRetry = { mode = AssistantSampleMode.Typing },
                ),
            modifier = Modifier.fillMaxWidth().height(280.dp),
            messageContent = { sender, text ->
                Text(
                    text = if (sender == ChatMessageSender.Assistant) "Assistant: $text" else text,
                    style = AppTheme.typography.bodyLarge,
                    color =
                        if (sender == ChatMessageSender.Assistant) {
                            AppTheme.colors.onSurfaceVariant
                        } else {
                            AppTheme.colors.onPrimaryContainer
                        },
                )
            },
        )

        if (showLimitPrompt) {
            AssistantLimitPromptCard(
                copy =
                    AssistantLimitPromptCopy(
                        message = "Daily assistant limit reached.",
                        supportingText = "Use this state to validate quota or upgrade prompts.",
                        primaryActionLabel = "Upgrade",
                        secondaryActionLabel = "Not now",
                    ),
                onPrimaryAction = {},
                onSecondaryAction = { showLimitPrompt = false },
            )
        }

        AssistantQuickActionChips(
            actions = AssistantSampleAction.entries,
            actionState = { action ->
                AssistantQuickActionState(
                    isSelected = action == selectedAction || action in usedActions,
                    isEnabled = !showLimitPrompt && mode != AssistantSampleMode.Typing && action !in usedActions,
                )
            },
            label = { it.label },
            onAction = { action ->
                selectedAction = action
                usedActions = usedActions + action
                mode = AssistantSampleMode.Typing
            },
        )

        AssistantDisclaimerFooter(text = "Assistant output is generated. Review important information before using it.")

        ControlsDivider()
        ControlSegmented(
            label = "Response state",
            options = AssistantSampleMode.entries.map { it.label },
            selectedIndex = AssistantSampleMode.entries.indexOf(mode),
            onOptionSelected = { mode = AssistantSampleMode.entries[it] },
        )
        ControlSwitch(
            label = "Show status banner",
            checked = showStatusBanner,
            onCheckedChange = { showStatusBanner = it },
        )
        ControlSwitch(
            label = "Show limit prompt",
            checked = showLimitPrompt,
            onCheckedChange = { showLimitPrompt = it },
        )
        PrimaryButton(
            text = "Reset quick actions",
            onClick = {
                selectedAction = AssistantSampleAction.SUMMARIZE
                usedActions = emptySet()
                mode = AssistantSampleMode.Idle
            },
        )
    }
}

private const val TYPEWRITER_DEMO_CHUNK_DELAY_MS = 350L
private val TYPEWRITER_DEMO_SPEED_RANGE = 5f..80f

private enum class AssistantSampleMode(
    val label: String,
) {
    Idle("Idle"),
    Typing("Typing"),
    Complete("Complete"),
    Error("Error"),
}

private enum class AssistantSampleAction(
    val label: String,
) {
    SUMMARIZE("Summarize"),
    EXPLAIN("Explain"),
    EXAMPLES("Examples"),
    TRANSLATE("Translate"),
}

private fun assistantSampleMessages(lastAssistantState: ChatMessageState): List<ChatMessage> =
    listOf(
        ChatMessage(
            id = "user-1",
            sender = ChatMessageSender.User,
            state = ChatMessageState.Content("Summarize this content"),
        ),
        ChatMessage(
            id = "assistant-1",
            sender = ChatMessageSender.Assistant,
            state = ChatMessageState.Content("This first answer is complete and stays in the conversation history."),
        ),
        ChatMessage(
            id = "user-2",
            sender = ChatMessageSender.User,
            state = ChatMessageState.Content("Explain it differently"),
        ),
        ChatMessage(
            id = "assistant-2",
            sender = ChatMessageSender.Assistant,
            state = lastAssistantState,
        ),
    )
