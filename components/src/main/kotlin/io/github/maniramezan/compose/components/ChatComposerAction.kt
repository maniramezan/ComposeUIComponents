package io.github.maniramezan.compose.components

/** Ring scale at an input level of `0`. */
internal const val CHAT_COMPOSER_RING_BASE_SCALE: Float = 0.8f

/** How much the listening ring grows beyond its base scale at an input level of `1`. */
internal const val CHAT_COMPOSER_RING_MAXIMUM_GROWTH: Float = 0.45f

/**
 * What a [ChatComposer]'s trailing button does, derived from the draft and voice state.
 */
internal sealed interface ChatComposerAction {
    /** Sends the draft. */
    data class Send(
        val enabled: Boolean,
    ) : ChatComposerAction

    /** Starts voice input. */
    data class StartVoice(
        val enabled: Boolean,
    ) : ChatComposerAction

    /** Stops voice input; [level] is clamped to `0..1` and drives the listening ring. */
    data class StopVoice(
        val level: Float,
    ) : ChatComposerAction

    /** Waits for the final transcript; not tappable. */
    data object Processing : ChatComposerAction
}

/**
 * Chooses the trailing action.
 *
 * Listening and processing take priority so the user can always stop recording. Otherwise a
 * non-blank draft shows send, and a blank draft shows the microphone when voice input is
 * configured (a disabled send when it is not).
 */
internal fun resolveChatComposerAction(
    text: String,
    voiceState: ChatComposerVoiceState?,
    enabled: Boolean,
): ChatComposerAction =
    when {
        voiceState is ChatComposerVoiceState.Listening ->
            ChatComposerAction.StopVoice(clampChatComposerLevel(voiceState.level))
        voiceState is ChatComposerVoiceState.Processing -> ChatComposerAction.Processing
        text.isNotBlank() -> ChatComposerAction.Send(enabled)
        voiceState is ChatComposerVoiceState.Idle -> ChatComposerAction.StartVoice(enabled)
        voiceState is ChatComposerVoiceState.Unavailable -> ChatComposerAction.StartVoice(false)
        else -> ChatComposerAction.Send(false)
    }

/** Whether the trailing button responds to taps. Stopping a recording is always allowed. */
internal val ChatComposerAction.isTappable: Boolean
    get() =
        when (this) {
            is ChatComposerAction.Send -> enabled
            is ChatComposerAction.StartVoice -> enabled
            is ChatComposerAction.StopVoice -> true
            ChatComposerAction.Processing -> false
        }

/** Whether the draft field accepts edits. Typing is paused while the transcript is live. */
internal val ChatComposerAction.allowsEditing: Boolean
    get() =
        when (this) {
            is ChatComposerAction.StopVoice, ChatComposerAction.Processing -> false
            is ChatComposerAction.Send, is ChatComposerAction.StartVoice -> true
        }

/** Clamps an input [level] to `0..1`, treating NaN as silence. */
internal fun clampChatComposerLevel(level: Float): Float = if (level.isNaN()) 0f else level.coerceIn(0f, 1f)

/** The listening ring's scale for an input [level] (clamped to `0..1`). */
internal fun chatComposerRingScale(level: Float): Float =
    CHAT_COMPOSER_RING_BASE_SCALE + clampChatComposerLevel(level) * CHAT_COMPOSER_RING_MAXIMUM_GROWTH
