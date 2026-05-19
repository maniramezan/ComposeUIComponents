package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.LevelTier

@ShowkaseComposable(name = "Level Badge", group = "Feedback")
@Composable
public fun LevelBadgeShowkase(): Unit =
    AppTheme {
        Row(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            LevelBadge(label = "Beginner", tier = LevelTier(Color(0xFF2E7D32), Color.White))
            LevelBadge(label = "Intermediate", tier = LevelTier(Color(0xFFE08534), Color.White))
            LevelBadge(label = "Advanced", tier = LevelTier(Color(0xFF6A1B9A), Color.White))
        }
    }
