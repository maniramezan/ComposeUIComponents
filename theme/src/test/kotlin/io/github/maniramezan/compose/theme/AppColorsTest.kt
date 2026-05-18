package io.github.maniramezan.compose.theme

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppColorsTest {
    @Test
    fun lightAndDarkColorsDiffer() {
        assertThat(AppColors.light().surface).isNotEqualTo(AppColors.dark().surface)
    }
}
