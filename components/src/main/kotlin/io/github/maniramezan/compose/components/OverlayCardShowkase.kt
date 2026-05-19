package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Overlay Card", group = "Containers")
@Composable
public fun OverlayCardShowkase(): Unit =
    AppTheme {
        OverlayCard(modifier = Modifier.padding(AppTheme.spacing.x2)) {
            Text(text = "Overlay")
        }
    }
