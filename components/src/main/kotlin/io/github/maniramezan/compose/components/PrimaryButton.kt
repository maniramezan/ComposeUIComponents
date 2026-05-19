package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.theme.AppTheme

@Composable
public fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = minimumTouchTargetSize()),
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.primary,
                contentColor = AppTheme.colors.onPrimary,
            ),
        contentPadding =
            PaddingValues(
                horizontal = AppTheme.spacing.lg,
                vertical = AppTheme.spacing.sm,
            ),
    ) {
        Text(text = text)
    }
}
