package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
@Preview(name = "FlipCard", group = "Containers")
@Composable
public fun FlipCardPreview(): Unit =
    AppTheme {
        FlipCard(
            modifier =
                Modifier
                    .padding(AppTheme.spacing.x2)
                    .size(200.dp, 120.dp),
            onClickLabel = "Flip card",
            frontStateDescription = "Showing question",
            backStateDescription = "Showing answer",
            front = {
                Box(Modifier.padding(AppTheme.spacing.lg), contentAlignment = Alignment.Center) {
                    Text("What is Compose?")
                }
            },
            back = {
                Box(Modifier.padding(AppTheme.spacing.lg), contentAlignment = Alignment.Center) {
                    Text("A declarative UI toolkit.")
                }
            },
        )
    }
