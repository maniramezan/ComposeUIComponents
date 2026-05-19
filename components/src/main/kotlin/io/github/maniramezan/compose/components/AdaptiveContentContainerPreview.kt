package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@Preview(name = "AdaptiveContentContainer", group = "Containers")
@Composable
public fun AdaptiveContentContainerPreview(): Unit =
    AppTheme {
        AdaptiveContentContainer(maxWidth = AppTheme.spacing.maxContentWidthForm) {
            Text(
                text = "Capped content area",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.surfaceContainer)
                        .padding(AppTheme.spacing.x2),
            )
        }
    }
