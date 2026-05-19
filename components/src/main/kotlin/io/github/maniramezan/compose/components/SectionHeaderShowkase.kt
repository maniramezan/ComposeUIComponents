package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Section Header", group = "Containers")
@Composable
public fun SectionHeaderShowkase(): Unit =
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
        ) {
            SectionHeader(title = "Recently played")
            SectionHeader(title = "Saved words", actionLabel = "See all", onAction = {})
        }
    }
