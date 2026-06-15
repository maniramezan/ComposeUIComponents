package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Flip Card", group = "Containers")
@Composable
public fun FlipCardShowkase(): Unit =
    AppTheme {
        FlipCard(
            modifier =
                Modifier
                    .padding(AppTheme.spacing.x2)
                    .size(200.dp, 120.dp),
            onClickLabel = "Flip card",
            frontStateDescription = "Showing question",
            backStateDescription = "Showing answer",
            front = {
                Box(Modifier.padding(AppTheme.spacing.lg), contentAlignment = Alignment.Center) {
                    Text("What is Compose?")
                }
            },
            back = {
                Box(Modifier.padding(AppTheme.spacing.lg), contentAlignment = Alignment.Center) {
                    Text("A declarative UI toolkit.")
                }
            },
        )
    }
