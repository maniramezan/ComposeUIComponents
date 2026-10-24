package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@Composable
internal fun SelectableCardPreviewContent() {
    Column(
        modifier = Modifier.padding(AppTheme.spacing.x2).selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        SelectableCard(selected = true, onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Monthly", style = AppTheme.typography.titleSmall)
            Text(text = "Billed every month")
        }
        SelectableCard(selected = false, onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Yearly", style = AppTheme.typography.titleSmall)
            Text(text = "Billed once a year")
        }
        SelectableCard(selected = false, onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Lifetime", style = AppTheme.typography.titleSmall)
            Text(text = "Not available")
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Preview(name = "SelectableCard", group = "Containers")
@Composable
public fun SelectableCardPreview(): Unit = AppTheme { SelectableCardPreviewContent() }
