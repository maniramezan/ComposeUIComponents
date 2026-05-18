package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.github.maniramezan.compose.theme.AppTheme

public enum class AppTextStyle {
    Display,
    Title,
    Body,
    Label,
}

@Composable
public fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: AppTextStyle = AppTextStyle.Body,
    color: Color = AppTheme.colors.onSurface,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style.toTextStyle(),
    )
}

@Composable
private fun AppTextStyle.toTextStyle(): TextStyle =
    when (this) {
        AppTextStyle.Display -> AppTheme.typography.display
        AppTextStyle.Title -> AppTheme.typography.title
        AppTextStyle.Body -> AppTheme.typography.body
        AppTextStyle.Label -> AppTheme.typography.label
    }
