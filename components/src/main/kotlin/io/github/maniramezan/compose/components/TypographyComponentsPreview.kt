package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme

@Preview(name = "Typography", group = "Typography")
@Composable
public fun TypographyComponentsPreview(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            AppText(text = "Display", style = AppTextStyle.Display)
            AppText(text = "Title", style = AppTextStyle.Title)
            AppText(text = "Body text for reading content.", style = AppTextStyle.Body)
            AppText(text = "Label", style = AppTextStyle.Label)
        }
    }
