package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable

@ShowkaseComposable(name = "Paginated Content – Leading", group = "Navigation")
@Composable
public fun PaginatedContentShowkase(): Unit = PaginatedContentPreview()

@ShowkaseComposable(name = "Paginated Content – Unidirectional", group = "Navigation")
@Composable
public fun PaginatedContentUnidirectionalShowkase(): Unit = PaginatedContentUnidirectionalPreview()
