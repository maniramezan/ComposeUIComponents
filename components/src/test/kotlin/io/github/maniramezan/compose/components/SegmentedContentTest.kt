package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SegmentedContentTest {
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

    @Test
    fun evenRowWhenEverySegmentFitsItsShare() {
        val layout =
            segmentRowLayout(
                intrinsicWidths = listOf(90, 80, 90),
                spacing = 4,
                maxWidth = 300,
                fitMode = SegmentFitMode.EvenWhenFits,
                fillWidth = true,
            )
        assertThat(layout).isEqualTo(SegmentRowLayout(useEven = true, useScrollable = false))
    }

    @Test
    fun unevenTitlesUseIntrinsicWidthsWhenTheirCombinedWidthFits() {
        val layout =
            segmentRowLayout(
                intrinsicWidths = listOf(150, 60, 70),
                spacing = 4,
                maxWidth = 300,
                fitMode = SegmentFitMode.EvenWhenFits,
                fillWidth = true,
            )
        assertThat(layout).isEqualTo(SegmentRowLayout(useEven = false, useScrollable = true))
    }

    @Test
    fun overflowingRowScrolls() {
        val layout =
            segmentRowLayout(
                intrinsicWidths = listOf(150, 120, 140),
                spacing = 4,
                maxWidth = 300,
                fitMode = SegmentFitMode.EvenWhenFits,
                fillWidth = true,
            )
        assertThat(layout).isEqualTo(SegmentRowLayout(useEven = false, useScrollable = true))
    }

    @Test
    fun fitWidthRowThatFitsNeitherSpreadsNorScrolls() {
        val layout =
            segmentRowLayout(
                intrinsicWidths = listOf(150, 60, 70),
                spacing = 4,
                maxWidth = 300,
                fitMode = SegmentFitMode.EvenWhenFits,
                fillWidth = false,
            )
        assertThat(layout).isEqualTo(SegmentRowLayout(useEven = false, useScrollable = false))
    }
}
