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
                "PageTitleAlignment",
                "PageDirection",
                "PageFooterStyle",
            ),
        ).containsExactly(
            "PaginatedContent",
            "PaginationPage",
            "PageTitleAlignment",
            "PageDirection",
            "PageFooterStyle",
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

    @Test
    fun pageTitleAlignmentValues() {
        assertThat(PageTitleAlignment.entries)
            .containsExactly(
                PageTitleAlignment.Leading,
                PageTitleAlignment.Trailing,
                PageTitleAlignment.Center,
            ).inOrder()
    }

    @Test
    fun pageDirectionValues() {
        assertThat(PageDirection.entries)
            .containsExactly(
                PageDirection.Bidirectional,
                PageDirection.Unidirectional,
            ).inOrder()
    }

    @Test
    fun pageFooterStyleValues() {
        assertThat(PageFooterStyle.entries)
            .containsExactly(
                PageFooterStyle.Dots,
                PageFooterStyle.Progress,
                PageFooterStyle.None,
            ).inOrder()
    }
}
