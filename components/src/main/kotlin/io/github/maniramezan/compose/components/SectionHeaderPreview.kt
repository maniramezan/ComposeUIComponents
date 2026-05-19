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
@Preview(name = "SectionHeader", group = "Containers")
@Composable
public fun SectionHeaderPreview(): Unit =
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
        ) {
            SectionHeader(title = "Recently played")
            SectionHeader(title = "Saved words", actionLabel = "See all", onAction = {})
        }
    }
