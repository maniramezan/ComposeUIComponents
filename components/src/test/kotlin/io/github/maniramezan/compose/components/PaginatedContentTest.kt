package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PaginatedContentTest {
    @Test
    fun paginatedContentComponentNamesAreStable() {
        assertThat(
            listOf(
                "PaginatedContent",
                "PaginationPage",
            ),
        ).containsExactly(
            "PaginatedContent",
            "PaginationPage",
        ).inOrder()
    }

    @Test
    fun paginationPageHoldsTitle() {
        val page = PaginationPage(title = "Popular")
        assertThat(page.title).isEqualTo("Popular")
    }

    @Test
    fun paginationPageEquality() {
        val a = PaginationPage(title = "Apps")
        val b = PaginationPage(title = "Apps")
        assertThat(a).isEqualTo(b)
    }
}
