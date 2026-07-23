package io.github.maniramezan.compose.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class TypewriterRevealTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun revealsOneCharacterAtATimeUntilTextIsComplete() {
        lateinit var revealed: State<String>
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            revealed = rememberTypewriterReveal(text = "Hi!", charsPerSecond = TEN_CHARS_PER_SECOND)
        }

        composeRule.mainClock.advanceTimeBy(MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("H", revealed.value)

        composeRule.mainClock.advanceTimeBy(MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("Hi", revealed.value)

        composeRule.mainClock.advanceTimeBy(MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("Hi!", revealed.value)
    }

    @Test
    public fun growingTextContinuesRevealInsteadOfRestarting() {
        val textState = mutableStateOf("Hello")
        lateinit var revealed: State<String>
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            revealed = rememberTypewriterReveal(text = textState.value, charsPerSecond = TEN_CHARS_PER_SECOND)
        }

        // Reveal "Hel" (3 characters).
        composeRule.mainClock.advanceTimeBy(3 * MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("Hel", revealed.value)

        // Extending the target text mid-reveal must preserve the already-revealed prefix: the
        // LaunchedEffect restart (on the new `text` key) needs a couple of frames to land —
        // one to recompose on the new state, one for the relaunched coroutine to actually run.
        composeRule.runOnIdle { textState.value = "Hello world" }
        settleEffectRestart()
        assertEquals("Hel", revealed.value)

        composeRule.mainClock.advanceTimeBy("Hello world".length * MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("Hello world", revealed.value)
    }

    @Test
    public fun unrelatedTextRestartsTheReveal() {
        val textState = mutableStateOf("Hello")
        lateinit var revealed: State<String>
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            revealed = rememberTypewriterReveal(text = textState.value, charsPerSecond = TEN_CHARS_PER_SECOND)
        }

        composeRule.mainClock.advanceTimeBy("Hello".length * MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("Hello", revealed.value)

        // A new value that isn't an extension of what's revealed restarts from empty, rather
        // than jumping straight from the old text to a same-length prefix of the new one.
        composeRule.runOnIdle { textState.value = "Goodbye" }
        settleEffectRestart()
        assertEquals("", revealed.value)

        composeRule.mainClock.advanceTimeBy("Goodbye".length * MILLIS_PER_CHAR)
        composeRule.waitForIdle()
        assertEquals("Goodbye", revealed.value)
    }

    private fun settleEffectRestart() {
        repeat(FRAMES_TO_SETTLE_EFFECT_RESTART) {
            composeRule.mainClock.advanceTimeByFrame()
            composeRule.waitForIdle()
        }
    }

    private companion object {
        const val TEN_CHARS_PER_SECOND = 10
        const val MILLIS_PER_CHAR = 100L
        const val FRAMES_TO_SETTLE_EFFECT_RESTART = 2
    }
}
