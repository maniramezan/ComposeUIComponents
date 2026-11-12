package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Composable
public fun ExpandableFABPreview(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2)) {
            ExpandableFABPreviewContent(expanded = false)
            ExpandableFABPreviewContent()
            ExpandableFABPreviewContent(direction = FABExpansionDirection.Start)
            ExpandableFABPreviewContent(showActionLabels = false, triggerLabel = "Actions")
        }
    }

@Composable
internal fun ExpandableFABPreviewContent(
    expanded: Boolean = true,
    direction: FABExpansionDirection = FABExpansionDirection.Up,
    longLabels: Boolean = false,
    showActionLabels: Boolean = true,
    triggerLabel: String? = null,
) {
    ExpandableFAB(
        expanded = expanded,
        onExpandedChange = {},
        actions =
            listOf(
                FABAction(
                    id = "first",
                    label = if (longLabels) "First action with a longer label" else "First",
                    icon = AppTheme.icons.check,
                    onClick = {},
                ),
                FABAction(
                    id = "second",
                    label = if (longLabels) "Second action with a longer label" else "Second",
                    icon = AppTheme.icons.close,
                    onClick = {},
                ),
            ),
        icon = AppTheme.icons.expand,
        contentDescription = "Actions",
        expandedStateDescription = "Expanded",
        collapsedStateDescription = "Collapsed",
        direction = direction,
        showActionLabels = showActionLabels,
        triggerLabel = triggerLabel,
    )
}
