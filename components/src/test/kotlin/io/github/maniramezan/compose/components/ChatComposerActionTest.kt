package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ChatComposerActionTest {
    private data class Case(
        val name: String,
        val text: String,
        val voiceState: ChatComposerVoiceState?,
        val enabled: Boolean,
        val expected: ChatComposerAction,
    )

    private val cases =
        listOf(
            Case("listening with blank draft", "", ChatComposerVoiceState.Listening(0.5f), true, ChatComposerAction.StopVoice(0.5f)),
            Case("listening with draft", "hi", ChatComposerVoiceState.Listening(0.5f), true, ChatComposerAction.StopVoice(0.5f)),
            Case("listening while disabled", "", ChatComposerVoiceState.Listening(0.25f), false, ChatComposerAction.StopVoice(0.25f)),
            Case("listening level above range", "", ChatComposerVoiceState.Listening(3f), true, ChatComposerAction.StopVoice(1f)),
            Case("listening level below range", "", ChatComposerVoiceState.Listening(-2f), true, ChatComposerAction.StopVoice(0f)),
            Case("listening level NaN", "", ChatComposerVoiceState.Listening(Float.NaN), true, ChatComposerAction.StopVoice(0f)),
            Case("processing with blank draft", "", ChatComposerVoiceState.Processing, true, ChatComposerAction.Processing),
            Case("processing with draft", "hi", ChatComposerVoiceState.Processing, true, ChatComposerAction.Processing),
            Case("draft without voice", "hi", null, true, ChatComposerAction.Send(true)),
            Case("draft without voice while disabled", "hi", null, false, ChatComposerAction.Send(false)),
            Case("draft with idle voice", "hi", ChatComposerVoiceState.Idle, true, ChatComposerAction.Send(true)),
            Case("draft with unavailable voice", "hi", ChatComposerVoiceState.Unavailable, true, ChatComposerAction.Send(true)),
            Case("whitespace draft without voice", " \n\t ", null, true, ChatComposerAction.Send(false)),
            Case("blank draft without voice", "", null, true, ChatComposerAction.Send(false)),
            Case("blank draft with idle voice", "", ChatComposerVoiceState.Idle, true, ChatComposerAction.StartVoice(true)),
            Case("whitespace draft with idle voice", "  ", ChatComposerVoiceState.Idle, true, ChatComposerAction.StartVoice(true)),
            Case(
                "blank draft with idle voice while disabled",
                "",
                ChatComposerVoiceState.Idle,
                false,
                ChatComposerAction.StartVoice(false),
            ),
            Case("blank draft with unavailable voice", "", ChatComposerVoiceState.Unavailable, true, ChatComposerAction.StartVoice(false)),
        )

    @Test
    fun resolvesEveryCaseInTheDecisionTable() {
        cases.forEach { case ->
            assertThat(resolveChatComposerAction(case.text, case.voiceState, case.enabled))
                .isEqualTo(case.expected)
        }
    }

    @Test
    fun onlyStoppingAndEnabledActionsAreTappable() {
        assertThat(ChatComposerAction.Send(true).isTappable).isTrue()
        assertThat(ChatComposerAction.Send(false).isTappable).isFalse()
        assertThat(ChatComposerAction.StartVoice(true).isTappable).isTrue()
        assertThat(ChatComposerAction.StartVoice(false).isTappable).isFalse()
        assertThat(ChatComposerAction.StopVoice(0f).isTappable).isTrue()
        assertThat(ChatComposerAction.Processing.isTappable).isFalse()
    }

    @Test
    fun editingIsPausedOnlyWhileListeningOrProcessing() {
        assertThat(ChatComposerAction.Send(true).allowsEditing).isTrue()
        assertThat(ChatComposerAction.StartVoice(true).allowsEditing).isTrue()
        assertThat(ChatComposerAction.StopVoice(0.5f).allowsEditing).isFalse()
        assertThat(ChatComposerAction.Processing.allowsEditing).isFalse()
    }

    @Test
    fun ringScaleGrowsLinearlyAndClampsTheLevel() {
        assertThat(chatComposerRingScale(0f)).isWithin(TOLERANCE).of(0.8f)
        assertThat(chatComposerRingScale(0.5f)).isWithin(TOLERANCE).of(1.025f)
        assertThat(chatComposerRingScale(1f)).isWithin(TOLERANCE).of(1.25f)
        assertThat(chatComposerRingScale(5f)).isWithin(TOLERANCE).of(1.25f)
        assertThat(chatComposerRingScale(-1f)).isWithin(TOLERANCE).of(0.8f)
        assertThat(chatComposerRingScale(Float.NaN)).isWithin(TOLERANCE).of(0.8f)
    }

    private companion object {
        const val TOLERANCE = 0.0001f
    }
}
