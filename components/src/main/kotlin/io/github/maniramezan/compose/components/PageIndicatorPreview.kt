package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Page Indicator", group = "Navigation")
@Composable
public fun PageIndicatorPreview(): Unit =
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
        ) {
            PageIndicator(pageCount = 3, currentPage = 0)
            PageIndicator(pageCount = 5, currentPage = 2)
        }
    }

@ShowkaseComposable(name = "Page Indicator", group = "Navigation")
@Composable
public fun PageIndicatorShowkase(): Unit = PageIndicatorPreview()
