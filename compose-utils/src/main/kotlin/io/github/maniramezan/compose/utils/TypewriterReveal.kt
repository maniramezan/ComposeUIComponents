package io.github.maniramezan.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

private const val DEFAULT_CHARS_PER_SECOND = 60
private const val MILLIS_PER_SECOND = 1000L

/**
 * Progressively reveals [text] one character at a time, at [charsPerSecond], instead of
 * showing it all at once.
 *
 * Designed for streaming/incremental content — a token stream that grows [text] over
 * several recompositions. When [text] grows, the reveal continues from wherever it left off
 * and catches up toward the new, longer value; it does not restart from the beginning unless
 * the new [text] is not an extension of what has already been revealed (i.e. a genuinely
 * different value), in which case the reveal restarts from the start of the new text.
 *
 * This holds no opinion on rendering: the returned [State] is the currently revealed prefix
 * of [text], and the caller renders it with whatever text/markdown composable fits.
 *
 * @param text the full target text; may grow between recompositions.
 * @param charsPerSecond reveal speed. Values below 1 are treated as 1.
 */
@Composable
public fun rememberTypewriterReveal(
    text: String,
    charsPerSecond: Int = DEFAULT_CHARS_PER_SECOND,
): State<String> {
    val revealed = remember { mutableStateOf("") }

    LaunchedEffect(text, charsPerSecond) {
        if (!text.startsWith(revealed.value)) {
            revealed.value = ""
        }
        val delayMillisPerChar = MILLIS_PER_SECOND / charsPerSecond.coerceAtLeast(1)
        while (revealed.value.length < text.length) {
            delay(delayMillisPerChar)
            val nextLength = (revealed.value.length + 1).coerceAtMost(text.length)
            revealed.value = text.substring(0, nextLength)
        }
    }

    return revealed
}
