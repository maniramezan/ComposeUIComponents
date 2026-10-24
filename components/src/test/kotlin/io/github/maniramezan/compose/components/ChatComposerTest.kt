package io.github.maniramezan.compose.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ChatComposerTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private var sendCount = 0
    private var startCount = 0
    private var stopCount = 0
    private var latestText = ""

    private fun voiceInput(state: ChatComposerVoiceState) =
        ChatComposerVoiceInput(
            state = state,
            onStart = { startCount++ },
            onStop = { stopCount++ },
            startContentDescription = START,
            stopContentDescription = STOP,
            processingContentDescription = PROCESSING,
        )

    private fun setComposer(
        initialText: String = "",
        enabled: Boolean = true,
        voiceState: ChatComposerVoiceState? = null,
        maxLines: Int = 5,
        placeholder: String = HINT,
    ) {
        composeRule.setContent {
            var text by remember { mutableStateOf(initialText) }
            ChatComposer(
                text = text,
                onTextChange = {
                    text = it
                    latestText = it
                },
                onSend = { sendCount++ },
                placeholder = placeholder,
                sendContentDescription = SEND,
                modifier = Modifier.testTag(TAG),
                enabled = enabled,
                maxLines = maxLines,
                voiceInput = voiceState?.let(::voiceInput),
            )
        }
    }

    @Test
    public fun blankDraftWithoutVoiceShowsDisabledSend() {
        setComposer()

        composeRule.onNodeWithContentDescription(SEND).assertIsNotEnabled().assertHasNoClickAction()
        composeRule.onNodeWithText(HINT).assertExists()
        composeRule.onNodeWithTag(TAG).assertExists()
    }

    @Test
    public fun draftShowsEnabledSendThatSendsWithoutClearingText() {
        setComposer(initialText = "Hello")

        composeRule
            .onNodeWithContentDescription(SEND)
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()

        assertEquals(1, sendCount)
        composeRule.onNodeWithText("Hello").assertExists()
        composeRule.onNodeWithContentDescription(SEND).assertIsEnabled()
    }

    @Test
    public fun typingReportsEditsAndEnablesSend() {
        setComposer()

        composeRule.onNode(hasSetTextAction()).performTextInput("Hi")

        assertEquals("Hi", latestText)
        composeRule.onNodeWithContentDescription(SEND).assertIsEnabled()
    }

    @Test
    public fun imeSendActionSendsOnlyNonBlankDraft() {
        setComposer(initialText = "Hello")
        composeRule.onNode(hasSetTextAction()).performImeAction()
        assertEquals(1, sendCount)
    }

    @Test
    public fun imeSendActionIgnoresBlankDraft() {
        setComposer(initialText = "   ")
        composeRule.onNode(hasSetTextAction()).performImeAction()
        assertEquals(0, sendCount)
    }

    @Test
    public fun disabledComposerNeitherSendsNorStartsVoice() {
        setComposer(initialText = "Hello", enabled = false)
        composeRule.onNodeWithContentDescription(SEND).assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performImeAction()
        assertEquals(0, sendCount)
    }

    @Test
    public fun idleVoiceShowsMicrophoneThatStartsVoice() {
        setComposer(voiceState = ChatComposerVoiceState.Idle)

        composeRule.onNodeWithContentDescription(START).assertIsEnabled().performClick()

        assertEquals(1, startCount)
        composeRule.onNodeWithContentDescription(SEND).assertDoesNotExist()
    }

    @Test
    public fun disabledComposerDisablesMicrophone() {
        setComposer(enabled = false, voiceState = ChatComposerVoiceState.Idle)
        composeRule.onNodeWithContentDescription(START).assertIsNotEnabled()
    }

    @Test
    public fun unavailableVoiceShowsDisabledMicrophone() {
        setComposer(voiceState = ChatComposerVoiceState.Unavailable)
        composeRule.onNodeWithContentDescription(START).assertIsNotEnabled()
        assertEquals(0, startCount)
    }

    @Test
    public fun draftOverridesIdleMicrophoneWithSend() {
        setComposer(initialText = "Hello", voiceState = ChatComposerVoiceState.Idle)
        composeRule.onNodeWithContentDescription(SEND).assertIsEnabled()
        composeRule.onNodeWithContentDescription(START).assertDoesNotExist()
    }

    @Test
    public fun listeningShowsStopThatStopsEvenWhenDisabledAndPausesEditing() {
        setComposer(
            initialText = "Dictated",
            enabled = false,
            voiceState = ChatComposerVoiceState.Listening(level = 0.5f),
        )

        composeRule.onNodeWithContentDescription(STOP).assertIsEnabled().performClick()

        assertEquals(1, stopCount)
        composeRule
            .onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.EditableText))
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.SetText))
    }

    @Test
    public fun processingShowsNonTappableProgressWithDescription() {
        setComposer(initialText = "Dictated", voiceState = ChatComposerVoiceState.Processing)

        composeRule.onNodeWithContentDescription(PROCESSING).assertExists().assertHasNoClickAction()
        composeRule.onNodeWithContentDescription(SEND).assertDoesNotExist()
    }

    @Test
    public fun trailingButtonMeetsMinimumTouchTarget() {
        setComposer(initialText = "Hello")
        composeRule
            .onNodeWithContentDescription(SEND)
            .assertWidthIsAtLeast(MIN_TARGET)
            .assertHeightIsAtLeast(MIN_TARGET)
    }

    @Test
    public fun blankPlaceholderRendersNoHint() {
        setComposer(placeholder = "")
        composeRule.onNodeWithText(HINT).assertDoesNotExist()
    }

    @Test
    public fun blankSendDescriptionLeavesNoLabel() {
        composeRule.setContent {
            ChatComposer(
                text = "Hello",
                onTextChange = {},
                onSend = {},
                placeholder = "",
                sendContentDescription = "",
            )
        }
        composeRule.onNodeWithContentDescription(SEND).assertDoesNotExist()
    }

    private companion object {
        const val TAG = "composer"
        const val HINT = "Message"
        const val SEND = "Send"
        const val START = "Start voice input"
        const val STOP = "Stop voice input"
        const val PROCESSING = "Processing voice input"
        val MIN_TARGET = 48.dp
    }
}
