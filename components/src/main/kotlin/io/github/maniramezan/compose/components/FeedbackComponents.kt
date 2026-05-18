package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.theme.AppTheme

@Composable
public fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    label: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (progress == null) {
            CircularProgressIndicator()
        } else {
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (label != null) {
            Text(text = label)
        }
    }
}

@Composable
public fun Skeleton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.colors.surfaceVariant),
    )
}

@Composable
public fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.colors.onSurface)
            .padding(horizontal = AppTheme.spacing.lg, vertical = AppTheme.spacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = message,
            color = AppTheme.colors.surface,
        )
        if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
            ) {
                Text(
                    text = actionLabel,
                    color = AppTheme.colors.surface,
                )
            }
        }
    }
}
