package io.github.maniramezan.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import io.github.maniramezan.compose.theme.AppTheme

/** Scrim opacity: dims the content enough to read as inert while keeping it legible. */
private const val LOADING_OVERLAY_SCRIM_ALPHA = 0.32f

/**
 * Hosts [content] and, while [loading] is `true`, covers it with a scrim that blocks
 * interaction — the blocking-progress treatment for work the person started and must wait
 * for (signing in, submitting a form, a purchase).
 *
 * While loading:
 * - every pointer gesture over [content] is swallowed, including on controls that have no
 *   `enabled` flag of their own;
 * - [content] is removed from the accessibility tree and the overlay announces
 *   [contentDescription] as a polite live region instead, so TalkBack users hear the
 *   operation rather than landing on controls that no longer respond;
 * - with [showIndicator], a centered card shows a spinner and the optional [message].
 *
 * Pass `showIndicator = false` when the control that started the work already shows its own
 * inline spinner, so the screen doesn't stack two. Prefer a non-blocking indicator
 * ([LoadingState], [ProgressIndicator], `Modifier.skeletonShimmer`) for loads the person
 * can keep interacting through.
 *
 * The scrim fades in and out with [AppTheme]'s short motion duration. The container sizes to
 * [content]; pass `Modifier.fillMaxSize()` to cover a whole screen.
 *
 * @param loading whether the overlay is shown.
 * @param contentDescription announced to screen readers while loading, e.g. "Signing in".
 *   Caller-supplied; there is no default text.
 * @param message optional supporting text under the spinner. Ignored when [showIndicator] is
 *   `false`. Blank renders nothing. It is visual only; TalkBack announces
 *   [contentDescription], so include the message there if screen-reader users need it.
 * @param showIndicator whether to show the centered spinner card on top of the scrim.
 */
@Composable
public fun LoadingOverlay(
    loading: Boolean,
    contentDescription: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    showIndicator: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    // propagateMinConstraints lets the content box fill a caller-sized container (e.g.
    // fillMaxSize()) so BoxScope alignment inside content behaves as it would at the root.
    Box(modifier = modifier, propagateMinConstraints = true) {
        Box(
            modifier = if (loading) Modifier.clearAndSetSemantics {} else Modifier,
            content = content,
        )
        AnimatedVisibility(
            visible = loading,
            modifier = Modifier.matchParentSize(),
            enter = fadeIn(tween(AppTheme.motion.shortMillis)),
            exit = fadeOut(tween(AppTheme.motion.shortMillis)),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.scrim.copy(alpha = LOADING_OVERLAY_SCRIM_ALPHA))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent().changes.forEach { it.consume() }
                                }
                            }
                        }.semantics {
                            if (contentDescription.isNotBlank()) {
                                this.contentDescription = contentDescription
                            }
                            liveRegion = LiveRegionMode.Polite
                        },
                contentAlignment = Alignment.Center,
            ) {
                if (showIndicator) {
                    LoadingOverlayCard(message = message?.takeIf { it.isNotBlank() })
                }
            }
        }
    }
}

@Composable
private fun LoadingOverlayCard(
    message: String?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(AppTheme.spacing.x4),
        shape = AppTheme.shapes.large,
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
    ) {
        Column(
            // The scrim already announces the operation; keep the card's spinner and text
            // from producing a second announcement.
            modifier = Modifier.padding(AppTheme.spacing.x3).clearAndSetSemantics {},
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1_5),
        ) {
            CircularProgressIndicator(color = AppTheme.colors.primary)
            if (message != null) {
                Text(
                    text = message,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
