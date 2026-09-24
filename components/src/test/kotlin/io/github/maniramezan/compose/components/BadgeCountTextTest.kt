package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class BadgeCountTextTest {
    @Test
    fun countsUpToTheCapAreShownVerbatim() {
        assertThat(badgeCountText(count = 0, maxCount = 99)).isEqualTo("0")
        assertThat(badgeCountText(count = 7, maxCount = 99)).isEqualTo("7")
        assertThat(badgeCountText(count = 99, maxCount = 99)).isEqualTo("99")
    }

    @Test
    fun countsAboveTheCapCollapseToCapPlus() {
        assertThat(badgeCountText(count = 100, maxCount = 99)).isEqualTo("99+")
        assertThat(badgeCountText(count = 12, maxCount = 9)).isEqualTo("9+")
    }

    @Test
    fun negativeCountsAndNonPositiveCapsAreClamped() {
        assertThat(badgeCountText(count = -3, maxCount = 99)).isEqualTo("0")
        assertThat(badgeCountText(count = 5, maxCount = 0)).isEqualTo("1+")
    }
}
