package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "Media", group = "Media")
@Composable
public fun MediaComponentsShowkase(): Unit = MediaComponentsPreview()

@ShowkaseComposable(name = "Cached Async Image", group = "Media")
@Composable
public fun CachedAsyncImageShowkase(): Unit = MediaComponentsPreview()
