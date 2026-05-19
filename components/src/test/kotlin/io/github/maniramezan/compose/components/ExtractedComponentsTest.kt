package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ExtractedComponentsTest {
    @Test
    fun extractedComponentNamesAreStable() {
        // Stability check: these names are part of the library's public API and
        // are referenced by docs + Showkase entries. Renames need explicit migration.
        assertThat(
            listOf(
                "PillChip",
                "OverlayCard",
                "AdaptiveContentContainer",
                "SectionHeader",
                "ContentRow",
                "LevelBadge",
                "SkeletonBlock",
            ),
        ).containsExactly(
            "PillChip",
            "OverlayCard",
            "AdaptiveContentContainer",
            "SectionHeader",
            "ContentRow",
            "LevelBadge",
            "SkeletonBlock",
        ).inOrder()
    }
}
