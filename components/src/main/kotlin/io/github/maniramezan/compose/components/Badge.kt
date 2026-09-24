package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme

/** Default values used by [Badge]. */
public object BadgeDefaults {
    /** Largest count a [Badge] spells out before collapsing to `"$MaxCount+"`. */
    public const val MaxCount: Int = 99
}

/**
 * A small status indicator, typically pinned to an icon's corner (for example via the
 * `badge` slot of [TabBarItemData]) to flag unread or pending items.
 *
 * - With [count] `null` it renders a small dot that only signals "something new".
 * - With a [count] it renders a capsule showing the number, collapsing values above
 *   [maxCount] to `"$maxCount+"`. The capsule grows with font scale, so the count stays
 *   legible at 200%.
 *
 * Accessibility: a badge usually annotates another control, so describe it there when you
 * can (e.g. include "3 unread" in the destination's own label). When the badge must speak
 * for itself, pass a localized [contentDescription] such as `"3 unread messages"`; it
 * replaces the bare number. With a blank [contentDescription], a dot is decorative (kept
 * out of the accessibility tree) and a count is read as its visible digits.
 *
 * @param modifier Modifier applied to the badge.
 * @param count Number to show, or `null` for a dot. Negative values render as `0`.
 * @param maxCount Largest value shown verbatim; larger counts show as `"$maxCount+"`.
 * @param contentDescription Caller-supplied, localized description announced instead of
 *   the visible content. Leave blank to fall back to the behavior described above.
 * @param containerColor Fill color; defaults to the theme's attention color (`error`).
 * @param contentColor Color of the count text.
 */
@Composable
public fun Badge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    maxCount: Int = BadgeDefaults.MaxCount,
    contentDescription: String = "",
    containerColor: Color = AppTheme.colors.error,
    contentColor: Color = AppTheme.colors.onError,
) {
    val semanticsModifier =
        when {
            contentDescription.isNotBlank() -> Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
            count == null -> Modifier.clearAndSetSemantics {}
            else -> Modifier.semantics(mergeDescendants = true) {}
        }

    if (count == null) {
        Box(
            modifier =
                modifier
                    .then(semanticsModifier)
                    .size(AppTheme.spacing.x1)
                    .pillSurface(containerColor),
        )
        return
    }

    Box(
        modifier =
            modifier
                .then(semanticsModifier)
                // A single digit renders as a circle; longer counts stretch into a capsule.
                .defaultMinSize(minWidth = AppTheme.spacing.x2, minHeight = AppTheme.spacing.x2)
                .pillSurface(containerColor)
                .padding(horizontal = AppTheme.spacing.half),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = badgeCountText(count = count, maxCount = maxCount),
            style = AppTheme.typography.labelSmall,
            color = contentColor,
            maxLines = 1,
        )
    }
}

/**
 * The visible text for a [Badge] count: the number itself up to [maxCount], otherwise
 * `"$maxCount+"`. Negative counts clamp to `0`, and a [maxCount] below `1` is treated as `1`.
 */
internal fun badgeCountText(
    count: Int,
    maxCount: Int,
): String {
    val cap = maxCount.coerceAtLeast(1)
    val clamped = count.coerceAtLeast(0)
    return if (clamped > cap) "$cap+" else clamped.toString()
}
