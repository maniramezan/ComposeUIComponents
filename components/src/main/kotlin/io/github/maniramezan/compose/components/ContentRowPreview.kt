package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.LevelTier
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "ContentRow", group = "Lists")
@Composable
public fun ContentRowPreview(): Unit =
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
