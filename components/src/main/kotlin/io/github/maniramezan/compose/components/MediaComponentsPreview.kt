package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppSpacingDefaults
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Media Components", group = "Media")
@Composable
public fun MediaComponentsPreview(): Unit =
    AppTheme {
        CachedAsyncImage(
            data = "https://example.com/thumbnail.jpg",
            contentDescription = "Example thumbnail",
            modifier =
                Modifier
                    .width(AppSpacingDefaults.x9)
                    .height(AppSpacingDefaults.x6),
            contentScale = ContentScale.Crop,
        )
    }
