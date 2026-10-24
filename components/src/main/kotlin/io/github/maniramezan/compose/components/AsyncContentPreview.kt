package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Async Content", group = "Lists")
@Composable
public fun AsyncContentPreview(): Unit = AppTheme { AsyncContentPreviewContent() }

/** The preview rows without a theme, so callers can supply their own [AppTheme]. */
@Composable
internal fun AsyncContentPreviewContent() {
    Column(
        modifier = Modifier.padding(AppTheme.spacing.x2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
    ) {
        AsyncContentPreviewRow(state = AsyncContentState.Loading)
        AsyncContentPreviewRow(state = AsyncContentState.Empty)
        AsyncContentPreviewRow(state = AsyncContentState.Error())
        AsyncContentPreviewRow(state = AsyncContentState.Content(data = listOf("Alpha", "Bravo")))
    }
}

@Composable
private fun AsyncContentPreviewRow(state: AsyncContentState<List<String>>) {
    AsyncContent(
        state = state,
        loadingLabel = "Loading projects",
        emptyTitle = "No projects",
        emptyMessage = "Create your first project to get started.",
        emptyAction = { TextButton(text = "Create project", onClick = {}) },
        errorTitle = "Could not load",
        errorMessage = "Check your connection and retry.",
        errorAction = { TextButton(text = "Retry", onClick = {}) },
    ) { items ->
        Column {
            items.forEach { item -> ListItem(headline = item) }
        }
    }
}
