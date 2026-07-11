package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A section header for list-style screens — a title on the leading edge with
 * an optional, trailing text action. Pass both [actionLabel] and [onAction]
 * to show the action; pass neither to render a header-only variant.
 *
 * @param titleStyle Optional override for the title's text style. When `null`
 *   (the default), the theme's `typography.titleSmall` is used.
 */
@Composable
public fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    titleStyle: TextStyle? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.x2),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            style = titleStyle ?: AppTheme.typography.titleSmall,
            color = AppTheme.colors.onSurface,
        )
        if (actionLabel != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = actionLabel,
                    style = AppTheme.typography.labelMedium,
                    color = AppTheme.colors.primary,
                )
            }
        }
    }
}
