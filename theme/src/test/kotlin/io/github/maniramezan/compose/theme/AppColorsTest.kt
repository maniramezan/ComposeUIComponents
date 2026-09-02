package io.github.maniramezan.compose.theme

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppColorsTest {
    @Test
    fun lightAndDarkColorsDiffer() {
        assertThat(AppColors.light().surface).isNotEqualTo(AppColors.dark().surface)
    }

    @Test
    fun lightAndDarkBackgroundsDiffer() {
        assertThat(AppColors.light().background).isNotEqualTo(AppColors.dark().background)
    }

    @Test
    fun lightAndDarkPrimaryContainersDiffer() {
        assertThat(AppColors.light().primaryContainer).isNotEqualTo(AppColors.dark().primaryContainer)
    }

    @Test
    fun statusSlotsArePopulated() {
        val light = AppColors.light()
        assertThat(light.success).isNotEqualTo(Color.Unspecified)
        assertThat(light.warning).isNotEqualTo(Color.Unspecified)
        assertThat(light.error).isNotEqualTo(Color.Unspecified)
    }

    @Test
    fun overlaySlotsArePopulated() {
        val light = AppColors.light()
        assertThat(light.overlayHeavy).isNotEqualTo(Color.Unspecified)
        assertThat(light.overlayMedium).isNotEqualTo(Color.Unspecified)
        assertThat(light.overlaySubtle).isNotEqualTo(Color.Unspecified)
        assertThat(light.onOverlay).isNotEqualTo(Color.Unspecified)
    }

    @Test
    fun onOverlayDefaultsToWhiteForScrimLegibility() {
        assertThat(AppColors.light().onOverlay).isEqualTo(Color.White)
        assertThat(AppColors.dark().onOverlay).isEqualTo(Color.White)
    }

    @Test
    fun overlayHeavyIsHigherAlphaThanOverlaySubtle() {
        val light = AppColors.light()
        assertThat(light.overlayHeavy.alpha).isGreaterThan(light.overlaySubtle.alpha)
    }

    @Test
    fun levelsDefaultToEmptyPalette() {
        assertThat(AppColors.light().levels.size).isEqualTo(0)
        assertThat(AppColors.dark().levels.size).isEqualTo(0)
    }

    @Test
    fun shimmerHighlightDefaultsToOnSurface() {
        assertThat(AppColors.light().shimmerHighlight).isEqualTo(AppColors.light().onSurface)
        assertThat(AppColors.dark().shimmerHighlight).isEqualTo(AppColors.dark().onSurface)
    }

    @Test
    fun shimmerHighlightDiffersBetweenLightAndDark() {
        assertThat(AppColors.light().shimmerHighlight).isNotEqualTo(AppColors.dark().shimmerHighlight)
    }
}
