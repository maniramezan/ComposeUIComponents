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
}
