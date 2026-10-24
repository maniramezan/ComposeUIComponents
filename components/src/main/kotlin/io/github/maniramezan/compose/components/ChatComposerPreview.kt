package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Chat Composer", group = "Feedback")
@Composable
public fun ChatComposerPreview(): Unit = AppTheme { ChatComposerPreviewContent() }

/** The preview rows without a theme, so callers can supply their own [AppTheme]. */
@Composable
internal fun ChatComposerPreviewContent() {
    Column(
        modifier = Modifier.padding(AppTheme.spacing.x2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
    ) {
        ChatComposerPreviewRow(text = "", voiceState = null)
        ChatComposerPreviewRow(text = "Draft message", voiceState = null)
        ChatComposerPreviewRow(text = "", voiceState = ChatComposerVoiceState.Idle)
        ChatComposerPreviewRow(text = "", voiceState = ChatComposerVoiceState.Unavailable)
        ChatComposerPreviewRow(text = "Dictated message", voiceState = ChatComposerVoiceState.Listening(level = 0.6f))
        ChatComposerPreviewRow(text = "Dictated message", voiceState = ChatComposerVoiceState.Processing)
    }
}

@Composable
private fun ChatComposerPreviewRow(
    text: String,
    voiceState: ChatComposerVoiceState?,
) {
    ChatComposer(
        text = text,
        onTextChange = {},
        onSend = {},
        placeholder = "Message",
        sendContentDescription = "Send",
        voiceInput =
            voiceState?.let {
                ChatComposerVoiceInput(
                    state = it,
                    onStart = {},
                    onStop = {},
                    startContentDescription = "Start voice input",
                    stopContentDescription = "Stop voice input",
                    processingContentDescription = "Processing voice input",
                )
            },
    )
}
