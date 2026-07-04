package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "Showcase Feed", group = "Layout")
@Composable
public fun ShowcaseFeedShowkase(): Unit = ShowcaseFeedPreview()

@ShowkaseComposable(name = "Showcase Row – Peek", group = "Layout")
@Composable
public fun ShowcaseRowPeekShowkase(): Unit = ShowcaseRowPeekPreview()

@ShowkaseComposable(name = "Showcase Feed – Grid Section", group = "Layout")
@Composable
public fun ShowcaseGridSectionShowkase(): Unit = ShowcaseGridSectionPreview()
