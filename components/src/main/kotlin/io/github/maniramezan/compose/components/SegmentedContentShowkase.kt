package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "Segmented Content – Fits Evenly", group = "Navigation")
@Composable
public fun SegmentedContentShowkase(): Unit = SegmentedContentPreview()

@ShowkaseComposable(name = "Segmented Content – Overflow Scrollable", group = "Navigation")
@Composable
public fun SegmentedContentOverflowShowkase(): Unit = SegmentedContentOverflowPreview()

@ShowkaseComposable(name = "Segmented Content – Underline", group = "Navigation")
@Composable
public fun SegmentedContentUnderlineShowkase(): Unit = SegmentedContentUnderlinePreview()

@ShowkaseComposable(name = "Segmented Content – Custom Title Slot", group = "Navigation")
@Composable
public fun SegmentedContentCustomTitleShowkase(): Unit = SegmentedContentCustomTitlePreview()

@ShowkaseComposable(name = "Segmented Content – No Indicator", group = "Navigation")
@Composable
public fun SegmentedContentNoIndicatorShowkase(): Unit = SegmentedContentNoIndicatorPreview()

@ShowkaseComposable(name = "Segmented Content – Fit Width", group = "Navigation")
@Composable
public fun SegmentedContentFitWidthShowkase(): Unit = SegmentedContentFitWidthPreview()
