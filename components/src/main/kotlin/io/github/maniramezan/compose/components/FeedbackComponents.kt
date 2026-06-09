package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

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
public fun Skeleton(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                // Default block height; this is decorative, not a tap target.
                .minimumTouchTargetHeight(minimumTouchTargetSize())
                .clip(RoundedCornerShape(containerCornerRadius()))
                .background(AppTheme.colors.surfaceVariant)
                // Loading placeholder carries no information; keep it out of the
                // a11y tree so screen readers don't stop on an empty node.
                .clearAndSetSemantics {},
    )
}

/**
 * Sized placeholder block for skeleton/ghost loading states. Pass an explicit
 * [height] and an optional [width]; when [width] is null the block expands to
 * the constraints supplied via [modifier]. Defaults pick neutral surface
 * colors so the placeholder reads as "empty content" against any theme.
 */
@Composable
public fun SkeletonBlock(
    height: Dp,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    shape: Shape = AppTheme.shapes.small,
    background: Color = AppTheme.colors.surfaceContainer,
) {
    val sizeModifier =
        if (width != null) {
            Modifier.width(width).height(height)
        } else {
            Modifier.height(height)
        }
    Box(
        modifier =
            modifier
                .then(sizeModifier)
                .clip(shape)
                .background(background)
                // Decorative loading placeholder; hide from screen readers.
                .clearAndSetSemantics {},
    )
}

/**
 * Transient message surface with an optional leading [icon] and action button.
 *
 * @param iconTint color applied to [icon]. Defaults to the surface color so a
 *   monochrome icon reads against the dark container. Pass [Color.Unspecified]
 *   to render a multi-color (colorful) icon with its own colors untouched.
 */
@Composable
public fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    icon: IconToken? = null,
    iconContentDescription: String? = null,
    iconTint: Color? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(containerCornerRadius()))
                .background(AppTheme.colors.onSurface)
                .padding(horizontal = AppTheme.spacing.lg, vertical = AppTheme.spacing.md)
                // Transient message: announce when it appears without stealing focus.
                .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        // Top-align so the leading icon and action sit beside the first line
        // when the message wraps to multiple lines, rather than floating against
        // the vertical center of the whole text block.
        verticalAlignment = Alignment.Top,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon.imageVector,
                // Null by default: the message text already conveys the meaning,
                // so the icon is decorative unless the caller labels it.
                contentDescription = iconContentDescription,
                // Color.Unspecified keeps a colorful icon's own colors.
                tint = iconTint ?: AppTheme.colors.surface,
                modifier = Modifier.size(standardIconSize()),
            )
        }
        Text(
            text = message,
            color = AppTheme.colors.surface,
            // Take the remaining width so a long message wraps between the icon
            // and the action instead of pushing the action button off-screen.
            // Align by baseline so the action label lines up with the first line.
            modifier = Modifier.weight(1f).alignByBaseline(),
        )
        if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
                modifier = Modifier.alignByBaseline(),
            ) {
                Text(
                    text = actionLabel,
                    color = AppTheme.colors.surface,
                )
            }
        }
    }
}
