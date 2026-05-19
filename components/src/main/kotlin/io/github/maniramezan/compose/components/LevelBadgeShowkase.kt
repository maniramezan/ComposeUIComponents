package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Level Badge", group = "Feedback")
@Composable
public fun LevelBadgeShowkase(): Unit =
    AppTheme {
        Row(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            LevelBadge(label = "Beginner", tier = AppTheme.colors.levels.tier(0))
            LevelBadge(label = "Intermediate", tier = AppTheme.colors.levels.tier(1))
            LevelBadge(label = "Advanced", tier = AppTheme.colors.levels.tier(2))
        }
    }
