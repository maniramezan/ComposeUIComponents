package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Lists", group = "Lists")
@Composable
public fun ListComponentsShowkase(): Unit = ListComponentsPreview()

@ShowkaseComposable(name = "List Item", group = "Lists")
@Composable
public fun ListItemShowkase(): Unit = AppTheme {
    ListItem(
        headline = "Compose Pro",
        supportingText = "Active subscription",
        trailingContent = { Text(text = "Active") },
    )
}

@ShowkaseComposable(name = "Empty State", group = "Lists")
@Composable
public fun EmptyStateShowkase(): Unit = AppTheme {
    EmptyState(
        title = "No projects",
        message = "Create your first project to get started.",
    )
}

@ShowkaseComposable(name = "Loading State", group = "Lists")
@Composable
public fun LoadingStateShowkase(): Unit = AppTheme {
    LoadingState(label = "Loading projects")
}

@ShowkaseComposable(name = "Error State", group = "Lists")
@Composable
public fun ErrorStateShowkase(): Unit = AppTheme {
    ErrorState(
        title = "Could not load",
        message = "Check your connection and retry.",
    )
}
