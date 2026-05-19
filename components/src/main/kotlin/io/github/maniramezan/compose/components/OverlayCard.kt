package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A subtle, rounded container intended to sit on top of media or photographic
 * content (video thumbnails, hero images, player overlays). Defaults to
 * [AppTheme.colors.overlaySubtle] and [AppTheme.shapes.large].
 */
@Composable
public fun OverlayCard(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    background: Color = AppTheme.colors.overlaySubtle,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            modifier
                .clip(AppTheme.shapes.large)
                .background(background)
                .padding(AppTheme.spacing.x2),
        contentAlignment = contentAlignment,
        content = content,
    )
}
