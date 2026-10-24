package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import io.github.maniramezan.compose.utils.buttonRole
import io.github.maniramezan.compose.utils.minimumTouchTarget

/**
 * Severity of a [NoticeBanner]. Drives the tint, outline, and live-region behavior.
 *
 * Color is never the only signal: pass an `icon` and `iconContentDescription` (for example
 * "Error") so the severity is also available without color vision or a screen reader.
 */
public enum class NoticeSeverity {
    /** Neutral information; tinted with the theme's primary color. Not announced on appearance. */
    Info,

    /** A completed or healthy outcome; tinted with the success color. Not announced on appearance. */
    Success,

    /** Something needs attention; tinted with the warning color. Announced politely. */
    Warning,

    /** Something failed or is blocked; tinted with the error color. Announced assertively. */
    Error,
}

/** Share of the severity color mixed into the surface to form the tonal container. */
internal const val NOTICE_CONTAINER_ALPHA: Float = 0.12f

/** Accent color for this severity, read from [AppTheme] status tokens. */
@Composable
@ReadOnlyComposable
internal fun NoticeSeverity.accentColor(): Color =
    when (this) {
        NoticeSeverity.Info -> AppTheme.colors.primary
        NoticeSeverity.Success -> AppTheme.colors.success
        NoticeSeverity.Warning -> AppTheme.colors.warning
        NoticeSeverity.Error -> AppTheme.colors.error
    }

/** Live-region mode used when the banner appears, or `null` when it should not announce itself. */
internal val NoticeSeverity.liveRegionMode: LiveRegionMode?
    get() =
        when (this) {
            NoticeSeverity.Error -> LiveRegionMode.Assertive
            NoticeSeverity.Warning -> LiveRegionMode.Polite
            NoticeSeverity.Info, NoticeSeverity.Success -> null
        }

/**
 * An inline, full-width tonal banner that reports a status in the flow of the screen, with an
 * optional leading icon, action, and dismiss button.
 *
 * Unlike [Toast] and [ToastHost], the banner is part of the layout and stays until the caller
 * removes it. Hoist visibility: render the banner while it applies, and remove it from
 * composition in [onDismiss] or after [onAction].
 *
 * The container is the severity color at a low alpha over `AppTheme.colors.surface`, outlined in
 * the full severity color. Text and the icon use `AppTheme.colors.onSurface` so they stay legible
 * for every severity in light and dark themes.
 *
 * Accessibility: the icon and message are one TalkBack focus stop. [NoticeSeverity.Error] is an
 * assertive live region and [NoticeSeverity.Warning] a polite one, so they are announced when they
 * appear; [NoticeSeverity.Info] and [NoticeSeverity.Success] are not. The action and dismiss
 * controls are separate buttons with at least 48dp touch targets. The action sits below the
 * message and the dismiss button at the trailing edge, so the message keeps most of the width at
 * large font scales.
 *
 * All text is caller-supplied; there are no English defaults.
 *
 * @param message visible banner text.
 * @param severity tint and announcement behavior; defaults to [NoticeSeverity.Info].
 * @param icon optional leading icon.
 * @param iconContentDescription accessibility label for [icon], for example the severity name.
 *   Leave null or blank when [message] already conveys the meaning, which makes the icon decorative.
 * @param actionLabel label for the action button. The button is shown only when this is non-blank
 *   and [onAction] is non-null.
 * @param onAction called when the action button is tapped.
 * @param dismissContentDescription accessibility label for the dismiss button. The button is shown
 *   only when this is non-blank and [onDismiss] is non-null, so it never renders unlabeled.
 * @param onDismiss called when the dismiss button is tapped.
 */
@Composable
public fun NoticeBanner(
    message: String,
    modifier: Modifier = Modifier,
    severity: NoticeSeverity = NoticeSeverity.Info,
    icon: IconToken? = null,
    iconContentDescription: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    dismissContentDescription: String? = null,
    onDismiss: (() -> Unit)? = null,
) {
    val accent = severity.accentColor()
    val container = accent.copy(alpha = NOTICE_CONTAINER_ALPHA).compositeOver(AppTheme.colors.surface)
    val shape = AppTheme.shapes.small
    val resolvedActionLabel = actionLabel?.takeIf { it.isNotBlank() && onAction != null }
    val resolvedDismissLabel = dismissContentDescription?.takeIf { it.isNotBlank() && onDismiss != null }
    val hasDismiss = resolvedDismissLabel != null

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(container)
                .border(AppTheme.spacing.strokeThin, accent, shape)
                .padding(
                    start = AppTheme.spacing.lg,
                    end = if (hasDismiss) AppTheme.spacing.xs else AppTheme.spacing.lg,
                ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
        ) {
            NoticeBannerMessage(
                message = message,
                severity = severity,
                icon = icon,
                iconContentDescription = iconContentDescription?.takeIf { it.isNotBlank() },
                hasAction = resolvedActionLabel != null,
            )
            if (resolvedActionLabel != null && onAction != null) {
                TextButton(
                    onClick = onAction,
                    modifier = Modifier.minimumTouchTarget(minimumTouchTargetSize()),
                ) {
                    Text(
                        text = resolvedActionLabel,
                        color = AppTheme.colors.onSurface,
                        style = AppTheme.typography.labelLarge,
                    )
                }
            }
        }
        if (resolvedDismissLabel != null && onDismiss != null) {
            Box(
                modifier = Modifier.buttonRole(onClick = onDismiss, minimumTouchTarget = minimumTouchTargetSize()),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppTheme.icons.close.imageVector,
                    contentDescription = resolvedDismissLabel,
                    tint = AppTheme.colors.onSurface,
                    modifier = Modifier.size(standardIconSize()),
                )
            }
        }
    }
}

/**
 * The icon and message as a single merged focus stop. The live region lives here rather than on
 * the whole banner so an announcement reads the message, not the action and dismiss labels.
 */
@Composable
private fun NoticeBannerMessage(
    message: String,
    severity: NoticeSeverity,
    icon: IconToken?,
    iconContentDescription: String?,
    hasAction: Boolean,
) {
    val liveRegionMode = severity.liveRegionMode
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.md, bottom = if (hasAction) AppTheme.spacing.xs else AppTheme.spacing.md)
                .semantics(mergeDescendants = true) {
                    if (liveRegionMode != null) liveRegion = liveRegionMode
                },
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        verticalAlignment = Alignment.Top,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = iconContentDescription,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(standardIconSize()),
            )
        }
        Text(
            text = message,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}
