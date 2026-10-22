package io.github.maniramezan.compose.components

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme

/** Number of dots in a [TypingIndicator]. */
private const val TYPING_DOT_COUNT = 3

/** Length of one full wave across every dot, including the rest before the next wave. */
private const val TYPING_CYCLE_MILLIS = 900L

/** Delay between one dot brightening and the next; also the ramp-up and ramp-down time. */
private const val TYPING_DOT_STAGGER_MILLIS = 150L

/** Alpha of a resting dot. */
private const val TYPING_DOT_DIM_ALPHA = 0.3f

/** Alpha of a dot at the crest of the wave. */
private const val TYPING_DOT_BRIGHT_ALPHA = 1f

/**
 * Three dots pulsing in sequence, for "someone is composing a reply" — typically inside a
 * chat bubble while a response is pending. [ChatLog] uses it for
 * [ChatMessageState.Typing].
 *
 * Reads as "composing" rather than "loading", unlike a spinner. To keep the pending
 * indicator and the streamed reply in the same bubble, render this while the bubble's text
 * is still empty and swap to the text once the first token arrives.
 *
 * The wave is a pure function of the frame clock and is applied in the draw phase, so it
 * never recomposes. It is static (every dot fully opaque) under the system "Remove
 * animations" setting and in `@Preview` inspection.
 *
 * @param contentDescription announced to screen readers as a polite live region. Pass a
 *   blank string (the default) to hide the indicator from accessibility, e.g. when an
 *   enclosing bubble already announces the pending state.
 * @param color the dot color.
 */
@Composable
public fun TypingIndicator(
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    color: Color = AppTheme.colors.onSurfaceVariant,
) {
    val animate = rememberDecorativeAnimationsEnabled()
    val frameMillis by produceState(initialValue = 0L, animate) {
        if (!animate) return@produceState
        while (true) {
            withInfiniteAnimationFrameMillis { value = it }
        }
    }
    val semanticsModifier =
        if (contentDescription.isNotBlank()) {
            Modifier.semantics {
                this.contentDescription = contentDescription
                liveRegion = LiveRegionMode.Polite
            }
        } else {
            Modifier.clearAndSetSemantics {}
        }

    Row(
        modifier = modifier.then(semanticsModifier).padding(vertical = AppTheme.spacing.half),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(TYPING_DOT_COUNT) { index ->
            Box(
                modifier =
                    Modifier
                        .size(AppTheme.spacing.x1)
                        .graphicsLayer {
                            alpha = if (animate) typingDotAlpha(index, frameMillis) else TYPING_DOT_BRIGHT_ALPHA
                        }.background(color, CircleShape),
            )
        }
    }
}

/**
 * Alpha of dot [index] at [frameMillis]. Each dot ramps from dim to bright over one stagger
 * step, starting one step after the previous dot, then ramps back down over the next step and
 * rests until the cycle repeats — a travelling wave rather than a synchronized blink.
 */
internal fun typingDotAlpha(
    index: Int,
    frameMillis: Long,
): Float {
    val t = frameMillis.mod(TYPING_CYCLE_MILLIS)
    val rampStart = index * TYPING_DOT_STAGGER_MILLIS
    val crest = rampStart + TYPING_DOT_STAGGER_MILLIS
    val rampEnd = crest + TYPING_DOT_STAGGER_MILLIS
    val range = TYPING_DOT_BRIGHT_ALPHA - TYPING_DOT_DIM_ALPHA
    return when {
        t <= rampStart || t >= rampEnd -> TYPING_DOT_DIM_ALPHA
        t < crest -> TYPING_DOT_DIM_ALPHA + range * (t - rampStart) / TYPING_DOT_STAGGER_MILLIS
        else -> TYPING_DOT_BRIGHT_ALPHA - range * (t - crest) / TYPING_DOT_STAGGER_MILLIS
    }
}
