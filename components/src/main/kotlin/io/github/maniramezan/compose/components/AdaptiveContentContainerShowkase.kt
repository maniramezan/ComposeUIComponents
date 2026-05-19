package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Adaptive Content Container", group = "Containers")
@Composable
public fun AdaptiveContentContainerShowkase(): Unit =
    AppTheme {
        AdaptiveContentContainer(maxWidth = AppTheme.spacing.maxContentWidthForm) {
            Text(
                text = "Capped content area",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.surfaceContainer)
                        .padding(AppTheme.spacing.x2),
            )
        }
    }
