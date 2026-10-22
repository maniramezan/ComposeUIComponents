package io.github.maniramezan.compose.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class TypingIndicatorTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun dotsRestDimOutsideTheirSlotAndCrestInSequence() {
        // Dot 0 crests at 150ms, dot 1 at 300ms, dot 2 at 450ms; all rest from 600ms.
        assertEquals(DIM, typingDotAlpha(index = 0, frameMillis = 0), TOLERANCE)
        assertEquals(BRIGHT, typingDotAlpha(index = 0, frameMillis = 150), TOLERANCE)
        assertEquals(BRIGHT, typingDotAlpha(index = 1, frameMillis = 300), TOLERANCE)
        assertEquals(BRIGHT, typingDotAlpha(index = 2, frameMillis = 450), TOLERANCE)
        assertEquals(DIM, typingDotAlpha(index = 2, frameMillis = 150), TOLERANCE)
        (0 until 3).forEach { index -> assertEquals(DIM, typingDotAlpha(index, frameMillis = 700), TOLERANCE) }
    }

    @Test
    public fun dotAlphaRampsLinearlyAndRepeatsEveryCycle() {
        assertEquals((DIM + BRIGHT) / 2, typingDotAlpha(index = 0, frameMillis = 75), TOLERANCE)
        assertEquals((DIM + BRIGHT) / 2, typingDotAlpha(index = 0, frameMillis = 225), TOLERANCE)
        (0L until 900L step 25).forEach { t ->
            assertEquals(typingDotAlpha(1, t), typingDotAlpha(1, t + 900), TOLERANCE)
            val alpha = typingDotAlpha(1, t)
            assertTrue("alpha $alpha at $t out of range", alpha in DIM..BRIGHT)
        }
    }

    @Test
    public fun nonBlankDescriptionIsAnnouncedAsPoliteLiveRegion() {
        composeRule.setContent {
            AppTheme { TypingIndicator(contentDescription = "Responding") }
        }

        composeRule
            .onNodeWithContentDescription("Responding")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
    }

    @Test
    public fun blankDescriptionHidesIndicatorFromAccessibility() {
        composeRule.setContent {
            AppTheme { TypingIndicator(modifier = Modifier.testTag(TAG)) }
        }

        val node = composeRule.onNodeWithTag(TAG).fetchSemanticsNode()
        assertTrue(node.config.isClearingSemantics)
        assertTrue(node.children.isEmpty())
    }

    private companion object {
        const val TAG = "typing"
        const val DIM = 0.3f
        const val BRIGHT = 1f
        const val TOLERANCE = 0.001f
    }
}
