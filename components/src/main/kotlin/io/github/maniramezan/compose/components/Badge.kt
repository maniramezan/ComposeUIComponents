package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import io.github.maniramezan.compose.theme.AppTheme
import java.text.NumberFormat

/**
 * A compact status label such as "Beta" or "New", drawn as a capsule.
 *
 * Non-interactive. The text is read as-is by screen readers. For a tappable or selectable
 * capsule use [PillChip]; for an unread count on an icon use [CountBadge].
 *
 * @param text the caller-localized label.
 * @param prominent `true` fills with `AppTheme.colors.primary` for emphasis; `false` (default)
 *   uses the quieter `surfaceVariant` fill.
 */
@Composable
public fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    prominent: Boolean = false,
) {
    Text(
        text = text,
        modifier =
            modifier
                .pillSurface(if (prominent) AppTheme.colors.primary else AppTheme.colors.surfaceVariant)
                .padding(horizontal = AppTheme.spacing.x1, vertical = AppTheme.spacing.half),
        style = AppTheme.typography.labelSmallSemibold,
        color = if (prominent) AppTheme.colors.onPrimary else AppTheme.colors.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * A small notification marker for an icon corner — pass it to [TabBarItemData.badge] or place
 * it over any icon.
 *
 * A `null` [count] draws a dot. A positive count draws the number, capped as "[maxCount]+"
 * (formatted for the current locale). A count of `0` or less draws nothing, so callers can pass
 * an unread count straight through.
 *
 * @param count the number to show, or `null` for a dot.
 * @param contentDescription caller-localized description such as "3 unread". Blank (the
 *   default) hides the badge from accessibility — use that when the host (for example a tab's
 *   own `contentDescription`) already announces the count.
 * @param maxCount the largest number shown before collapsing to "[maxCount]+".
 */
@Composable
public fun CountBadge(
    count: Int?,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    maxCount: Int = DEFAULT_MAX_BADGE_COUNT,
) {
    if (count != null && count <= 0) return
    val semanticsModifier =
        if (contentDescription.isNotBlank()) {
            Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
        } else {
            Modifier.clearAndSetSemantics {}
        }
    if (count == null) {
        Box(
            modifier =
                modifier
                    .then(semanticsModifier)
                    .size(AppTheme.spacing.x1)
                    .pillSurface(AppTheme.colors.error),
        )
        return
    }
    Box(
        modifier =
            modifier
                .then(semanticsModifier)
                .defaultMinSize(minWidth = AppTheme.spacing.x2, minHeight = AppTheme.spacing.x2)
                .pillSurface(AppTheme.colors.error)
                .padding(horizontal = AppTheme.spacing.half),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = formatBadgeCount(count, maxCount),
            style = AppTheme.typography.labelSmallSemibold,
            color = AppTheme.colors.onError,
            maxLines = 1,
        )
    }
}

/** Default cap for [CountBadge]; larger counts render as "99+". */
private const val DEFAULT_MAX_BADGE_COUNT = 99

/** Formats [count] for [CountBadge], collapsing anything above [maxCount] to "[maxCount]+". */
internal fun formatBadgeCount(
    count: Int,
    maxCount: Int,
    format: NumberFormat = NumberFormat.getIntegerInstance(),
): String = if (count > maxCount) "${format.format(maxCount)}+" else format.format(count)
