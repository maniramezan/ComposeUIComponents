package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.material3.TextButton as MaterialTextButton

@Composable
public fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.colors.primary,
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

@Composable
public fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MaterialTextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        colors =
            ButtonDefaults.textButtonColors(
                contentColor = AppTheme.colors.primary,
            ),
        contentPadding =
            PaddingValues(
                horizontal = AppTheme.spacing.md,
                vertical = AppTheme.spacing.sm,
            ),
    ) {
        Text(text = text)
    }
}

@Composable
public fun IconButton(
    icon: IconToken,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MaterialIconButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp),
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            tint = AppTheme.colors.primary,
        )
    }
}

@Composable
public fun FAB(
    icon: IconToken,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp),
        containerColor = AppTheme.colors.primary,
        contentColor = AppTheme.colors.onPrimary,
    ) {
        Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
public fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            if (selected) {
                Button(
                    onClick = { onOptionSelected(index) },
                    enabled = enabled,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.primary,
                            contentColor = AppTheme.colors.onPrimary,
                        ),
                ) {
                    Text(text = option)
                }
            } else {
                OutlinedButton(
                    onClick = { onOptionSelected(index) },
                    enabled = enabled,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = AppTheme.colors.primary,
                        ),
                ) {
                    Text(text = option)
                }
            }
        }
    }
}
