package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DividerComponentsTest {
    @Test
    fun dividerComponentNamesAreStable() {
        val expected = listOf("HorizontalDivider", "VerticalDivider")
        assertThat(
            listOf(
                "HorizontalDivider",
                "VerticalDivider",
            ),
        ).containsExactlyElementsIn(expected).inOrder()
    }
}
