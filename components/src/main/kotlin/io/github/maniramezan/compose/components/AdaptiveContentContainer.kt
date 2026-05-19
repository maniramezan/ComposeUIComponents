package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centers content horizontally and caps it at [maxWidth] so screens don't
 * stretch uncomfortably across tablet form factors. On phones the constraint
 * is not hit and this behaves as a pass-through that fills available width.
 *
 * Apps typically pick a semantic max width per surface — narrow for forms,
 * wider for lists, in-between for media. Pass the value from your design
 * system's layout-dimension tokens.
 *
 * Mirrors Apple's adaptive-width view modifiers on iOS.
 */
@Composable
public fun AdaptiveContentContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 600.dp,
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = contentAlignment,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = maxWidth),
            content = content,
        )
    }
}
