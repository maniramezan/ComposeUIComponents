package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TypographyComponentsTest {
    @Test
    fun typographyComponentNamesAreStable() {
        val expected = listOf("AppText", "AppTextStyle")
        assertThat(
            listOf(
                "AppText",
                "AppTextStyle",
            ),
        ).containsExactlyElementsIn(expected).inOrder()
    }
}
