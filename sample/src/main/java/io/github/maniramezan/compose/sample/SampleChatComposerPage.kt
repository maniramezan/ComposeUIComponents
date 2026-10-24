package io.github.maniramezan.compose.sample

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.ChatComposer
import io.github.maniramezan.compose.components.ChatComposerVoiceInput
import io.github.maniramezan.compose.components.ChatComposerVoiceState

private val VOICE_STATE_OPTIONS = listOf("Idle", "Listening", "Processing", "Unavailable")
private val MAX_LINES_OPTIONS = listOf(1, 3, 5)

@Composable
internal fun ChatComposerPage() {
    var text by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(true) }
    var showPlaceholder by remember { mutableStateOf(true) }
    var maxLinesIndex by remember { mutableIntStateOf(MAX_LINES_OPTIONS.lastIndex) }
    var voiceEnabled by remember { mutableStateOf(true) }
    var voiceStateIndex by remember { mutableIntStateOf(0) }
    var level by remember { mutableFloatStateOf(0.6f) }
    var clearOnSend by remember { mutableStateOf(true) }
    var sentCount by remember { mutableIntStateOf(0) }

    val voiceState =
        when (voiceStateIndex) {
            1 -> ChatComposerVoiceState.Listening(level)
            2 -> ChatComposerVoiceState.Processing
            3 -> ChatComposerVoiceState.Unavailable
            else -> ChatComposerVoiceState.Idle
        }

    SamplePage(
        preview = {
            ChatComposer(
                text = text,
                onTextChange = { text = it },
                onSend = {
                    sentCount++
                    if (clearOnSend) text = ""
                },
                placeholder = if (showPlaceholder) "Message" else "",
                sendContentDescription = "Send",
                enabled = enabled,
                maxLines = MAX_LINES_OPTIONS[maxLinesIndex],
                voiceInput =
                    if (voiceEnabled) {
                        ChatComposerVoiceInput(
                            state = voiceState,
                            onStart = { voiceStateIndex = 1 },
                            onStop = { voiceStateIndex = 2 },
                            startContentDescription = "Start voice input",
                            stopContentDescription = "Stop voice input",
                            processingContentDescription = "Processing voice input",
                        )
                    } else {
                        null
                    },
            )
            Text("Sent $sentCount times")
        },
        controls = {
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(label = "Placeholder", checked = showPlaceholder, onCheckedChange = { showPlaceholder = it })
            ControlSwitch(label = "Clear text on send", checked = clearOnSend, onCheckedChange = { clearOnSend = it })
            ControlSegmented(
                label = "Max lines",
                options = MAX_LINES_OPTIONS.map { it.toString() },
                selectedIndex = maxLinesIndex,
                onOptionSelected = { maxLinesIndex = it },
            )
            ControlSwitch(label = "Voice input", checked = voiceEnabled, onCheckedChange = { voiceEnabled = it })
            ControlSegmented(
                label = "Voice state",
                options = VOICE_STATE_OPTIONS,
                selectedIndex = voiceStateIndex,
                onOptionSelected = { voiceStateIndex = it },
            )
            ControlSlider(label = "Listening level", value = level, onValueChange = { level = it })
        },
    )
}
