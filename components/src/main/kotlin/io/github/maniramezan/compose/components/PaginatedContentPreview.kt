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
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

private val samplePages =
    listOf(
        PaginationPage(title = "Popular"),
        PaginationPage(title = "New Releases"),
        PaginationPage(title = "Top Rated"),
    )

@PreviewLightDark
@PreviewFontScale
@Preview(name = "PaginatedContent – Leading", group = "Navigation")
@Composable
public fun PaginatedContentPreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages = samplePages,
            titleAlignment = PageTitleAlignment.Leading,
            direction = PageDirection.Bidirectional,
        ) { _, page ->
            PagePlaceholder(page.title)
        }
    }

@PreviewLightDark
@Preview(name = "PaginatedContent – Trailing Bidirectional", group = "Navigation")
@Composable
public fun PaginatedContentTrailingPreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages = samplePages,
            titleAlignment = PageTitleAlignment.Trailing,
            direction = PageDirection.Bidirectional,
        ) { _, page ->
            PagePlaceholder(page.title)
        }
    }

@PreviewLightDark
@Preview(name = "PaginatedContent – Center", group = "Navigation")
@Composable
public fun PaginatedContentCenterPreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages = samplePages,
            titleAlignment = PageTitleAlignment.Center,
            direction = PageDirection.Bidirectional,
        ) { _, page ->
            PagePlaceholder(page.title)
        }
    }

@PreviewLightDark
@Preview(name = "PaginatedContent – Unidirectional", group = "Navigation")
@Composable
public fun PaginatedContentUnidirectionalPreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages = samplePages,
            titleAlignment = PageTitleAlignment.Leading,
            direction = PageDirection.Unidirectional,
        ) { _, page ->
            PagePlaceholder(page.title)
        }
    }

@PreviewLightDark
@Preview(name = "PaginatedContent – Progress Footer", group = "Navigation")
@Composable
public fun PaginatedContentProgressPreview(): Unit =
    AppTheme {
        PaginatedContent(
            pages = samplePages,
            titleAlignment = PageTitleAlignment.Leading,
            footerStyle = PageFooterStyle.Progress,
        ) { _, page ->
            PagePlaceholder(page.title)
        }
    }

@Composable
private fun PagePlaceholder(title: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(AppTheme.spacing.xl * 5f)
                .padding(AppTheme.spacing.x2),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$title content",
            style = AppTheme.typography.titleSmall,
        )
    }
}
