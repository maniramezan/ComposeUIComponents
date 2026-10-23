package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Indicators", group = "Indicators")
@Composable
public fun IndicatorComponentsShowkase(): Unit = IndicatorComponentsPreview()

@ShowkaseComposable(name = "Badge", group = "Indicators")
@Composable
public fun BadgeShowkase(): Unit =
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
            Badge(text = "Beta")
            Badge(text = "New", prominent = true)
        }
    }

@ShowkaseComposable(name = "Count Badge", group = "Indicators")
@Composable
public fun CountBadgeShowkase(): Unit =
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
            CountBadge(count = null)
            CountBadge(count = 7)
            CountBadge(count = 120)
        }
    }

@ShowkaseComposable(name = "Avatar", group = "Indicators")
@Composable
public fun AvatarShowkase(): Unit =
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
            Avatar(name = "Avery Quinn", size = AvatarSize.Small)
            Avatar(name = "Blake Rivera")
            Avatar(name = "Casey Morgan", size = AvatarSize.Large)
        }
    }
