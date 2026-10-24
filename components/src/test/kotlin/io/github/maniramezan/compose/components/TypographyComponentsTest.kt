package io.github.maniramezan.compose.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.AppTypography
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class TypographyComponentsTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    @Test
    public fun everyAppTextStyleResolvesToItsThemeTypographyToken() {
        var style by mutableStateOf(AppTextStyle.Body)
        lateinit var typography: AppTypography
        composeRule.setContent {
            typography = AppTheme.typography
            AppText(text = SAMPLE, style = style)
        }

        AppTextStyle.entries.forEach { entry ->
            composeRule.runOnIdle { style = entry }
            val rendered = renderedTextStyle()
            val expected = typography.tokenFor(entry)
            assertEquals("fontSize for $entry", expected.fontSize, rendered.fontSize)
            assertEquals("fontWeight for $entry", expected.fontWeight, rendered.fontWeight)
            assertEquals("lineHeight for $entry", expected.lineHeight, rendered.lineHeight)
        }
    }

    @Test
    public fun appTextDefaultsToOnSurfaceColor() {
        var expected = Color.Unspecified
        composeRule.setContent {
            expected = AppTheme.colors.onSurface
            AppText(text = SAMPLE)
        }

        assertEquals(expected, renderedTextStyle().color)
    }

    private fun renderedTextStyle(): TextStyle {
        val results = mutableListOf<TextLayoutResult>()
        composeRule
            .onNodeWithText(SAMPLE)
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsActions.GetTextLayoutResult)
            ?.action
            ?.invoke(results)
        return results.single().layoutInput.style
    }

    private fun AppTypography.tokenFor(style: AppTextStyle): TextStyle =
        when (style) {
            AppTextStyle.Display -> display
            AppTextStyle.Title -> title
            AppTextStyle.Body -> body
            AppTextStyle.Label -> label
            AppTextStyle.LabelSmallSemibold -> labelSmallSemibold
            AppTextStyle.LabelSmallBold -> labelSmallBold
            AppTextStyle.LabelMediumSemibold -> labelMediumSemibold
            AppTextStyle.LabelMediumBold -> labelMediumBold
            AppTextStyle.LabelLargeBold -> labelLargeBold
            AppTextStyle.BodySmallMedium -> bodySmallMedium
            AppTextStyle.BodySmallSemibold -> bodySmallSemibold
            AppTextStyle.BodyMediumSemibold -> bodyMediumSemibold
            AppTextStyle.BodyLargeSemibold -> bodyLargeSemibold
        }

    private companion object {
        const val SAMPLE = "Sample text"
    }
}
