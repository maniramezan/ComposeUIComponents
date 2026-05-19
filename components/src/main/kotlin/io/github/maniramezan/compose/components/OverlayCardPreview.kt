package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "OverlayCard", group = "Containers")
@Composable
public fun OverlayCardPreview(): Unit =
    AppTheme {
        OverlayCard(modifier = Modifier.padding(AppTheme.spacing.x2)) {
            Text(text = "On a hero image")
        }
    }
