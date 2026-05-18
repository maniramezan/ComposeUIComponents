package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TypographyComponentsTest {
    @Test
    fun typographyComponentNamesAreStable() {
        assertThat(
            listOf(
                "AppText",
                "AppTextStyle",
            ),
        ).containsExactly(
            "AppText",
            "AppTextStyle",
        ).inOrder()
    }
}
