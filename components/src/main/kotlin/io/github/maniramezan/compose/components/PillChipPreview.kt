package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "PillChip", group = "Actions")
@Composable
public fun PillChipPreview(): Unit =
    AppTheme {
        Row(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            PillChip(label = "All", isSelected = true, onClick = {})
            PillChip(label = "Beginner", isSelected = false, onClick = {})
            PillChip(label = "Advanced", isSelected = false, onClick = {})
        }
    }
