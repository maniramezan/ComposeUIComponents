package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Paginated Content", group = "Navigation")
@Composable
public fun PaginatedContentPreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages =
                listOf(
                    PaginationPage(title = "Popular"),
                    PaginationPage(title = "New"),
                    PaginationPage(title = "Top Rated"),
                ),
        ) { pageIndex, page ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(AppTheme.spacing.x2),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${page.title} content (page $pageIndex)",
                    style = AppTheme.typography.titleSmall,
                )
            }
        }
    }

@PreviewLightDark
@Preview(name = "Paginated Content - Custom Title", group = "Navigation")
@Composable
public fun PaginatedContentCustomTitlePreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages =
                listOf(
                    PaginationPage(title = "Games"),
                    PaginationPage(title = "Apps"),
                ),
            pageTitle = { _, page, selected ->
                Text(
                    text = if (selected) "★ ${page.title}" else page.title,
                    style = AppTheme.typography.labelLarge,
                    color = if (selected) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant,
                )
            },
        ) { _, page ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(AppTheme.spacing.x2),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "${page.title} section")
            }
        }
    }
