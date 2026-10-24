package io.github.maniramezan.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.buttonRole
import io.github.maniramezan.compose.utils.minimumTouchTarget

/** Opacity of the listening ring behind the stop button. */
private const val LISTENING_RING_ALPHA = 0.16f

/** Opacity of the disabled trailing button's container. */
private const val DISABLED_CONTAINER_ALPHA = 0.12f

/** The state of the voice input attached to a [ChatComposer]. */
public sealed interface ChatComposerVoiceState {
    /** Voice input is available and not recording. The trailing button starts it. */
    public data object Idle : ChatComposerVoiceState

    /**
     * Voice input is recording.
     *
     * @property level The current input level. Values outside `0..1` are clamped.
     */
    public data class Listening(
        public val level: Float,
    ) : ChatComposerVoiceState

    /** Recording stopped and the final transcript is pending. The trailing button shows progress. */
    public data object Processing : ChatComposerVoiceState

    /** Voice input cannot be used right now. The trailing button is a disabled microphone. */
    public data object Unavailable : ChatComposerVoiceState
}

/**
 * Voice-input state, actions, and accessibility labels for a [ChatComposer].
 *
 * The composer does not record audio or transcribe speech. The caller owns both: start and stop
 * recording in [onStart] and [onStop], and write the live transcript into the composer's text.
 *
 * @property state The current voice state.
 * @property onStart Called when the user taps the microphone.
 * @property onStop Called when the user taps the stop button while listening.
 * @property startContentDescription Accessibility label of the microphone button.
 * @property stopContentDescription Accessibility label of the stop button.
 * @property processingContentDescription Accessibility label of the progress indicator.
 */
@Immutable
public data class ChatComposerVoiceInput(
    public val state: ChatComposerVoiceState,
    public val onStart: () -> Unit,
    public val onStop: () -> Unit,
    public val startContentDescription: String,
    public val stopContentDescription: String,
    public val processingContentDescription: String,
)

/**
 * A chat message field with a send button and optional voice input.
 *
 * The field grows with its text from one line up to [maxLines]. The trailing button sends a
 * non-blank draft. When [voiceInput] is set and the draft is blank, it becomes a microphone, then
 * a stop button with a level ring while listening, then a progress indicator while the final
 * transcript is pending. Write the live transcript into [text] so dictated messages can be
 * reviewed and edited before sending. Editing is paused while listening or processing.
 *
 * The composer never clears [text] itself; clear it in [onSend]. Every string is caller-supplied,
 * so pass already-localized values.
 *
 * ```kotlin
 * var draft by remember { mutableStateOf("") }
 *
 * ChatComposer(
 *     text = draft,
 *     onTextChange = { draft = it },
 *     onSend = { send(draft); draft = "" },
 *     placeholder = "Message",
 *     sendContentDescription = "Send",
 * )
 * ```
 *
 * @param text The draft. Typed text and any live transcript both land here.
 * @param onTextChange Called with the new draft when the user edits it.
 * @param onSend Called when the user sends a non-blank draft with the button or the keyboard's
 *   Send action, and [enabled] is true.
 * @param placeholder Hint shown while the draft is empty. A blank value renders no hint.
 * @param sendContentDescription Accessibility label of the send button.
 * @param enabled Whether sending and starting voice input are allowed, for example `false` while
 *   a reply is streaming. Stopping an active recording is always allowed.
 * @param maxLines How many lines the field grows to before it scrolls. Values below 1 are
 *   treated as 1.
 * @param voiceInput Voice-input state, actions, and labels, or `null` for a text-only composer.
 */
@Composable
public fun ChatComposer(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    placeholder: String,
    sendContentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxLines: Int = 5,
    voiceInput: ChatComposerVoiceInput? = null,
) {
    val action = resolveChatComposerAction(text = text, voiceState = voiceInput?.state, enabled = enabled)
    val submit = {
        if (enabled && text.isNotBlank()) onSend()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        verticalAlignment = Alignment.Bottom,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            readOnly = !action.allowsEditing,
            placeholder = placeholder.takeIf { it.isNotBlank() }?.let { hint -> { Text(text = hint) } },
            maxLines = maxLines.coerceAtLeast(1),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { submit() }),
        )
        ChatComposerActionButton(
            action = action,
            contentDescription = chatComposerContentDescription(action, sendContentDescription, voiceInput),
            onClick = {
                when (action) {
                    is ChatComposerAction.Send -> submit()
                    is ChatComposerAction.StartVoice -> voiceInput?.onStart?.invoke()
                    is ChatComposerAction.StopVoice -> voiceInput?.onStop?.invoke()
                    ChatComposerAction.Processing -> Unit
                }
            },
        )
    }
}

/** The accessibility label for the trailing button in [action]. */
private fun chatComposerContentDescription(
    action: ChatComposerAction,
    sendContentDescription: String,
    voiceInput: ChatComposerVoiceInput?,
): String =
    when (action) {
        is ChatComposerAction.Send -> sendContentDescription
        is ChatComposerAction.StartVoice -> voiceInput?.startContentDescription.orEmpty()
        is ChatComposerAction.StopVoice -> voiceInput?.stopContentDescription.orEmpty()
        ChatComposerAction.Processing -> voiceInput?.processingContentDescription.orEmpty()
    }

/** The trailing send / microphone / stop / progress button of a [ChatComposer]. */
@Composable
private fun ChatComposerActionButton(
    action: ChatComposerAction,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val minimumTarget = minimumTouchTargetSize()
    val interaction =
        when {
            action.isTappable -> Modifier.buttonRole(onClick = onClick, minimumTouchTarget = minimumTarget)
            action == ChatComposerAction.Processing -> Modifier.minimumTouchTarget(minimumTarget)
            else ->
                Modifier.minimumTouchTarget(minimumTarget).semantics {
                    role = Role.Button
                    disabled()
                }
        }

    Box(
        modifier =
            modifier
                .then(interaction)
                .semantics {
                    if (contentDescription.isNotBlank()) this.contentDescription = contentDescription
                },
        contentAlignment = Alignment.Center,
    ) {
        when (action) {
            is ChatComposerAction.Send -> ChatComposerGlyphButton(ChatComposerGlyph.Send, action.enabled)
            is ChatComposerAction.StartVoice -> ChatComposerGlyphButton(ChatComposerGlyph.Mic, action.enabled)
            is ChatComposerAction.StopVoice -> {
                ChatComposerLevelRing(level = action.level)
                ChatComposerGlyphButton(ChatComposerGlyph.Stop, enabled = true)
            }
            ChatComposerAction.Processing ->
                CircularProgressIndicator(
                    modifier = Modifier.size(AppTheme.spacing.x3),
                    color = AppTheme.colors.primary,
                    strokeWidth = AppTheme.spacing.strokeRegular,
                )
        }
    }
}

/** A filled circle holding one [glyph], tinted for its [enabled] state. */
@Composable
private fun ChatComposerGlyphButton(
    glyph: ChatComposerGlyph,
    enabled: Boolean,
) {
    val containerColor =
        if (enabled) AppTheme.colors.primary else AppTheme.colors.onSurface.copy(alpha = DISABLED_CONTAINER_ALPHA)
    val glyphColor =
        if (enabled) AppTheme.colors.onPrimary else AppTheme.colors.onSurface.copy(alpha = DISABLED_CONTENT_ALPHA)
    val glyphSize = AppTheme.spacing.x2
    val painter: Painter = rememberVectorPainter(rememberChatComposerGlyph(glyph, glyphSize))

    Box(
        modifier = Modifier.size(AppTheme.spacing.x4).pillSurface(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(glyphSize)
                    .paint(painter = painter, colorFilter = ColorFilter.tint(glyphColor)),
        )
    }
}

/**
 * A ring behind the stop button that grows with the input [level] while listening. It stays at
 * rest size when decorative animations are off.
 */
@Composable
private fun ChatComposerLevelRing(level: Float) {
    val animate = rememberDecorativeAnimationsEnabled()
    val scale by animateFloatAsState(
        targetValue = if (animate) chatComposerRingScale(level) else 1f,
        animationSpec = tween(durationMillis = AppTheme.motion.shortMillis, easing = AppTheme.motion.emphasizedEasing),
        label = "ChatComposerLevelRing",
    )

    Box(
        modifier =
            Modifier
                .size(minimumTouchTargetSize())
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }.pillSurface(AppTheme.colors.primary.copy(alpha = LISTENING_RING_ALPHA)),
    )
}
