package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Indicator Components", group = "Indicators")
@Composable
public fun IndicatorComponentsPreview(): Unit =
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
                Badge(text = "Beta")
                Badge(text = "New", prominent = true)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CountBadge(count = null)
                CountBadge(count = 3)
                CountBadge(count = 42)
                CountBadge(count = 250)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1_5),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(name = "Avery Quinn", size = AvatarSize.Small)
                Avatar(name = "Blake Rivera")
                Avatar(name = "Casey Morgan", size = AvatarSize.Large)
                Avatar(name = "", size = AvatarSize.Large)
            }
        }
    }
