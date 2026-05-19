package io.github.maniramezan.compose.theme

import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppSpacingTest {
    @Test
    fun multiplierScaleMatchesEightPointGrid() {
        val spacing = AppSpacing.default()
        assertThat(spacing.half).isEqualTo(4.dp)
        assertThat(spacing.x1).isEqualTo(8.dp)
        assertThat(spacing.x1_5).isEqualTo(12.dp)
        assertThat(spacing.x2).isEqualTo(16.dp)
        assertThat(spacing.x3).isEqualTo(24.dp)
        assertThat(spacing.x4).isEqualTo(32.dp)
        assertThat(spacing.x5).isEqualTo(40.dp)
        assertThat(spacing.x6).isEqualTo(48.dp)
        assertThat(spacing.x9).isEqualTo(72.dp)
    }

    @Test
    fun tShirtScaleAliasesMultiplierScale() {
        val spacing = AppSpacing.default()
        assertThat(spacing.xs).isEqualTo(spacing.half)
        assertThat(spacing.sm).isEqualTo(spacing.x1)
        assertThat(spacing.md).isEqualTo(spacing.x1_5)
        assertThat(spacing.lg).isEqualTo(spacing.x2)
        assertThat(spacing.xl).isEqualTo(spacing.x3)
    }

    @Test
    fun semanticAliasesMatchMultiplierScale() {
        val spacing = AppSpacing.default()
        assertThat(spacing.padding).isEqualTo(spacing.x2)
        assertThat(spacing.contentPadding).isEqualTo(spacing.x3)
        assertThat(spacing.itemSpacing).isEqualTo(spacing.x1)
        assertThat(spacing.gridSpacing).isEqualTo(spacing.x2)
    }

    @Test
    fun strokeWidthsAreOrdered() {
        val spacing = AppSpacing.default()
        assertThat(spacing.strokeThin).isEqualTo(1.dp)
        assertThat(spacing.strokeRegular).isEqualTo(2.dp)
        assertThat(spacing.strokeThick).isEqualTo(4.dp)
    }

    @Test
    fun minTapTargetMeetsAccessibilityMinimum() {
        assertThat(AppSpacing.default().minTapTarget).isEqualTo(44.dp)
    }

    @Test
    fun defaultsObjectMatchesDataClassDefault() {
        val spacing = AppSpacing.default()
        assertThat(AppSpacingDefaults.half).isEqualTo(spacing.half)
        assertThat(AppSpacingDefaults.x2).isEqualTo(spacing.x2)
        assertThat(AppSpacingDefaults.minTapTarget).isEqualTo(spacing.minTapTarget)
    }
}
