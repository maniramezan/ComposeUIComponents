package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SegmentedContentTest {
    @Test
    fun segmentedContentComponentNamesAreStable() {
        assertThat(
            listOf(
                "SegmentedContent",
                "SegmentedItem",
                "SegmentSelectionIndicator",
                "SegmentFitMode",
                "SegmentWidthMode",
                "SegmentDensity",
            ),
        ).containsExactly(
            "SegmentedContent",
            "SegmentedItem",
            "SegmentSelectionIndicator",
            "SegmentFitMode",
            "SegmentWidthMode",
            "SegmentDensity",
        ).inOrder()
    }

    @Test
    fun segmentedItemHoldsTitle() {
        val item = SegmentedItem(title = "Overview")
        assertThat(item.title).isEqualTo("Overview")
    }

    @Test
    fun segmentedItemEquality() {
        val a = SegmentedItem(title = "Activity")
        val b = SegmentedItem(title = "Activity")
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun segmentSelectionIndicatorValues() {
        assertThat(SegmentSelectionIndicator.entries)
            .containsExactly(
                SegmentSelectionIndicator.Pill,
                SegmentSelectionIndicator.Underline,
                SegmentSelectionIndicator.None,
            ).inOrder()
    }

    @Test
    fun segmentFitModeValues() {
        assertThat(SegmentFitMode.entries)
            .containsExactly(
                SegmentFitMode.EvenWhenFits,
                SegmentFitMode.Intrinsic,
            ).inOrder()
    }

    @Test
    fun segmentWidthModeValues() {
        assertThat(SegmentWidthMode.entries)
            .containsExactly(
                SegmentWidthMode.Fill,
                SegmentWidthMode.Fit,
            ).inOrder()
    }

    @Test
    fun segmentDensityValues() {
        assertThat(SegmentDensity.entries)
            .containsExactly(
                SegmentDensity.Regular,
                SegmentDensity.Compact,
            ).inOrder()
    }
}
