package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
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

private const val TYPEWRITER_DEMO_CHUNK_DELAY_MS = 350L
private val TYPEWRITER_DEMO_SPEED_RANGE = 5f..80f
