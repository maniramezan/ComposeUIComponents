package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "List Components", group = "Lists")
@Composable
public fun ListComponentsPreview(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            ListItem(
                headline = "Compose Pro",
                supportingText = "Active subscription",
                trailingContent = { Text(text = "Active") },
            )
            EmptyState(
                title = "No projects",
                message = "Create your first project to get started.",
            )
            LoadingState(label = "Loading projects")
            ErrorState(
                title = "Could not load",
                message = "Check your connection and retry.",
            )
        }
    }

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Lazy List", group = "Lists")
@Composable
public fun LazyListPreview(): Unit =
    AppTheme {
        LazyList {
            items(previewLazyListRows) { row ->
                ListItem(headline = row)
            }
        }
    }

private val previewLazyListRows = listOf("Alpha", "Bravo", "Charlie", "Delta")
