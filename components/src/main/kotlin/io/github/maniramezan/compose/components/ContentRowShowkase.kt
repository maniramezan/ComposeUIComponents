package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.LevelTier

@ShowkaseComposable(name = "Content Row", group = "Lists")
@Composable
public fun ContentRowShowkase(): Unit =
    AppTheme {
        ContentRow(
            title = "ephemeral",
            secondaryText = "/əˈfemərəl/",
            supportingText = "Lasting for a very short time.",
            onClick = {},
            trailingContent = {
                LevelBadge(label = "C1", tier = LevelTier(Color(0xFF6A1B9A), Color.White))
            },
        )
    }
