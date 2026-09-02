package io.github.maniramezan.compose.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

    @Test
    fun weightVariantsRaiseWeightButKeepBaseSlotMetrics() {
        val typography = AppTypography.default()

        data class Variant(
            val name: String,
            val style: TextStyle,
            val base: TextStyle,
            val weight: FontWeight,
        )

        val variants =
            listOf(
                Variant("labelSmallSemibold", typography.labelSmallSemibold, typography.labelSmall, FontWeight.SemiBold),
                Variant("labelSmallBold", typography.labelSmallBold, typography.labelSmall, FontWeight.Bold),
                Variant("labelMediumSemibold", typography.labelMediumSemibold, typography.labelMedium, FontWeight.SemiBold),
                Variant("labelMediumBold", typography.labelMediumBold, typography.labelMedium, FontWeight.Bold),
                Variant("labelLargeBold", typography.labelLargeBold, typography.labelLarge, FontWeight.Bold),
                Variant("bodySmallMedium", typography.bodySmallMedium, typography.bodySmall, FontWeight.Medium),
                Variant("bodySmallSemibold", typography.bodySmallSemibold, typography.bodySmall, FontWeight.SemiBold),
                Variant("bodyMediumSemibold", typography.bodyMediumSemibold, typography.bodyMedium, FontWeight.SemiBold),
                Variant("bodyLargeSemibold", typography.bodyLargeSemibold, typography.bodyLarge, FontWeight.SemiBold),
            )

        for (variant in variants) {
            assertThat(variant.style.fontWeight).isEqualTo(variant.weight)
            assertThat(variant.style.fontSize).isEqualTo(variant.base.fontSize)
            assertThat(variant.style.lineHeight).isEqualTo(variant.base.lineHeight)
            assertThat(variant.style.fontWeight).isNotEqualTo(variant.base.fontWeight)
        }
    }
}
