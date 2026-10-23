package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
public class LabeledDividerTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun labelIsAnnouncedOnce() {
        composeRule.setContent {
            AppTheme { LabeledDivider(label = "Section") }
        }

        composeRule.onAllNodesWithText("Section").assertCountEquals(1)
        composeRule.onNodeWithText("Section").assertIsDisplayed()
    }

    @Test
    public fun blankLabelDrawsFullWidthRuleWithoutEmptyText() {
        composeRule.setContent {
            AppTheme {
                Box(Modifier.requiredWidth(240.dp)) {
                    LabeledDivider(label = "  ", modifier = Modifier.testTag("divider"))
                }
            }
        }

        composeRule.onNodeWithTag("divider").assertWidthIsEqualTo(240.dp)
        composeRule.onAllNodesWithText("  ").assertCountEquals(0)
    }

    @Test
    public fun longLabelWrapsAndStaysCenteredInRtlAtDoubleFontScale() {
        val label = "Additional section information"
        composeRule.setContent {
            val density = LocalDensity.current.density
            CompositionLocalProvider(
                LocalDensity provides Density(density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                AppTheme {
                    Box(Modifier.requiredWidth(240.dp)) {
                        LabeledDivider(label = label, modifier = Modifier.testTag("divider"))
                    }
                }
            }
        }

        val divider = composeRule.onNodeWithTag("divider").getUnclippedBoundsInRoot()
        val text = composeRule.onNodeWithText(label).assertIsDisplayed().getUnclippedBoundsInRoot()
        assertTrue(text.right - text.left <= 120.dp)
        val textLayouts = mutableListOf<TextLayoutResult>()
        composeRule
            .onNodeWithText(label)
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsActions.GetTextLayoutResult)
            ?.action
            ?.invoke(textLayouts)
        val textLayout = textLayouts.single()
        assertTrue(textLayout.lineCount > 1)
        assertFalse(textLayout.hasVisualOverflow)
        assertEquals((divider.left + divider.right).value / 2, (text.left + text.right).value / 2, 1f)
        assertTrue(text.left > divider.left)
        assertTrue(text.right < divider.right)
    }
}
