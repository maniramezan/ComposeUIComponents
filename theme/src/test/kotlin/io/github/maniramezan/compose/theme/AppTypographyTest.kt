package io.github.maniramezan.compose.theme

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppTypographyTest {
    @Test
    fun defaultTypographyHasSemanticStyles() {
        val typography = AppTypography.default()

        assertThat(typography.display.fontSize.value).isGreaterThan(typography.title.fontSize.value)
        assertThat(typography.body.fontSize.value).isGreaterThan(typography.label.fontSize.value)
    }
}
