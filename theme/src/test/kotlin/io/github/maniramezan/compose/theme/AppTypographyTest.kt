package io.github.maniramezan.compose.theme

import androidx.compose.ui.text.font.FontFamily
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppTypographyTest {
    @Test
    fun originalSlotsRetainHistoricSizes() {
        val typography = AppTypography.default()

        // The 4-slot scale was the library's first cut; size ordering must hold.
        assertThat(typography.display.fontSize.value).isGreaterThan(typography.title.fontSize.value)
        assertThat(typography.body.fontSize.value).isGreaterThan(typography.label.fontSize.value)
    }

    @Test
    fun materialThreeScaleIsOrderedBySize() {
        val typography = AppTypography.default()

        assertThat(typography.displayLarge.fontSize.value).isGreaterThan(typography.displayMedium.fontSize.value)
        assertThat(typography.displayMedium.fontSize.value).isGreaterThan(typography.displaySmall.fontSize.value)
        assertThat(typography.headlineLarge.fontSize.value).isGreaterThan(typography.headlineMedium.fontSize.value)
        assertThat(typography.titleLarge.fontSize.value).isGreaterThan(typography.titleMedium.fontSize.value)
        assertThat(typography.bodyLarge.fontSize.value).isGreaterThan(typography.bodyMedium.fontSize.value)
        assertThat(typography.labelLarge.fontSize.value).isGreaterThan(typography.labelMedium.fontSize.value)
    }

    @Test
    fun monospacedSlotsUseMonospaceFontFamily() {
        val typography = AppTypography.default()

        assertThat(typography.bodyMonospaced.fontFamily).isEqualTo(FontFamily.Monospace)
        assertThat(typography.captionMonospaced.fontFamily).isEqualTo(FontFamily.Monospace)
    }

    @Test
    fun iconSlotsAreOrderedBySize() {
        val typography = AppTypography.default()

        assertThat(typography.iconSmall.fontSize.value).isLessThan(typography.iconMedium.fontSize.value)
        assertThat(typography.iconMedium.fontSize.value).isLessThan(typography.iconLarge.fontSize.value)
        assertThat(typography.iconLarge.fontSize.value).isLessThan(typography.iconExtraLarge.fontSize.value)
    }
}
