package io.github.maniramezan.compose.theme

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class LevelPaletteTest {
    private val red = LevelTier(background = Color.Red, foreground = Color.White)
    private val green = LevelTier(background = Color.Green, foreground = Color.Black)
    private val blue = LevelTier(background = Color.Blue, foreground = Color.White)

    @Test
    fun emptyPaletteHasNoTiers() {
        assertThat(LevelPalette.empty().size).isEqualTo(0)
        assertThat(LevelPalette.empty().tiers).isEmpty()
    }

    @Test
    fun emptyPaletteReturnsTransparentTier() {
        val tier = LevelPalette.empty().tier(0)
        assertThat(tier.background).isEqualTo(Color.Transparent)
        assertThat(tier.foreground).isEqualTo(Color.Transparent)
    }

    @Test
    fun tierAccessReturnsCorrectTierByIndex() {
        val palette = LevelPalette(listOf(red, green, blue))
        assertThat(palette.tier(0)).isEqualTo(red)
        assertThat(palette.tier(1)).isEqualTo(green)
        assertThat(palette.tier(2)).isEqualTo(blue)
    }

    @Test
    fun tierAccessClampsHighIndexToLastTier() {
        val palette = LevelPalette(listOf(red, green, blue))
        assertThat(palette.tier(99)).isEqualTo(blue)
    }

    @Test
    fun tierAccessClampsNegativeIndexToFirstTier() {
        val palette = LevelPalette(listOf(red, green, blue))
        assertThat(palette.tier(-1)).isEqualTo(red)
    }
}
