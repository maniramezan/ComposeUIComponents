package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.LevelTier
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "LevelBadge", group = "Feedback")
@Composable
public fun LevelBadgePreview(): Unit =
    AppTheme {
        Row(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            LevelBadge(label = "Beginner", tier = LevelTier(Color(0xFF2E7D32), Color.White))
            LevelBadge(label = "Advanced", tier = LevelTier(Color(0xFF6A1B9A), Color.White))
            LevelBadge(label = "Expert", tier = LevelTier(Color(0xFFC2185B), Color.White))
        }
    }
