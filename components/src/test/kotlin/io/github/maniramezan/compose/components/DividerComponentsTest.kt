package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DividerComponentsTest {
    @Test
    fun dividerComponentNamesAreStable() {
        assertThat(
            listOf(
                "HorizontalDivider",
                "VerticalDivider",
            ),
        ).containsExactly(
            "HorizontalDivider",
            "VerticalDivider",
        ).inOrder()
    }
}
