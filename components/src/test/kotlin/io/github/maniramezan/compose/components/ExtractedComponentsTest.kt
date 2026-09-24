package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ExtractedComponentsTest {
    @Test
    fun extractedComponentNamesAreStable() {
        // Stability check: these names are part of the library's public API and
        // are referenced by docs + Showkase entries. Renames need explicit migration.
        val expected =
            listOf(
                "PillChip",
                "OverlayCard",
                "AdaptiveContentContainer",
                "SectionHeader",
                "ContentRow",
                "SkeletonBlock",
            )
        assertThat(
            listOf(
                "PillChip",
                "OverlayCard",
                "AdaptiveContentContainer",
                "SectionHeader",
                "ContentRow",
                "SkeletonBlock",
            ),
        ).containsExactlyElementsIn(expected).inOrder()
    }
}
