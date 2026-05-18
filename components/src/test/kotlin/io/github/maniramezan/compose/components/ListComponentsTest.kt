package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ListComponentsTest {
    @Test
    fun listComponentNamesAreStable() {
        assertThat(
            listOf(
                "ListItem",
                "LazyList",
                "EmptyState",
                "LoadingState",
                "ErrorState",
            ),
        ).containsExactly(
            "ListItem",
            "LazyList",
            "EmptyState",
            "LoadingState",
            "ErrorState",
        ).inOrder()
    }
}
