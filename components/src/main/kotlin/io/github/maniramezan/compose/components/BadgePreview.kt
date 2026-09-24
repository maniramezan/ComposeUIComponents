package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Badge", group = "Feedback")
@Composable
public fun BadgePreview(): Unit =
    AppTheme {
        Row(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Badge()
            Badge(count = 3)
            Badge(count = 42)
            Badge(count = 1_000)
        }
    }

@ShowkaseComposable(name = "Badge", group = "Feedback")
@Composable
public fun BadgeShowkase(): Unit = BadgePreview()
