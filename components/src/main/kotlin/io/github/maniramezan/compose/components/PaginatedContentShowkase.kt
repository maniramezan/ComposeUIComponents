package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Paginated Content", group = "Navigation")
@Composable
public fun PaginatedContentShowkase(): Unit =
    AppTheme {
        PaginatedContent(
            pages =
                listOf(
                    PaginationPage(title = "Popular"),
                    PaginationPage(title = "New"),
                    PaginationPage(title = "Top Rated"),
                ),
        ) { _, page ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(AppTheme.spacing.x2),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "${page.title} content")
            }
        }
    }
