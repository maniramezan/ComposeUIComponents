package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Action Components", group = "Actions")
@Composable
public fun ActionComponentsPreview(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            PrimaryButton(text = "Primary", onClick = {})
            SecondaryButton(text = "Secondary", onClick = {})
            TextButton(text = "Text", onClick = {})
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                IconButton(
                    icon = AppTheme.icons.check,
                    contentDescription = "Confirm",
                    onClick = {},
                )
                FAB(
                    icon = AppTheme.icons.check,
                    contentDescription = "Create",
                    onClick = {},
                )
            }
            SegmentedControl(
                options = listOf("Day", "Week", "Month"),
                selectedIndex = 1,
                onOptionSelected = {},
            )
        }
    }
