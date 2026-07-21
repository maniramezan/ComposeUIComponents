package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Dividers", group = "Dividers")
@Composable
public fun DividerComponentsShowkase(): Unit = DividerComponentsPreview()

@ShowkaseComposable(name = "Horizontal Divider", group = "Dividers")
@Composable
public fun HorizontalDividerShowkase(): Unit =
    AppTheme {
        HorizontalDivider()
    }

@ShowkaseComposable(name = "Vertical Divider", group = "Dividers")
@Composable
public fun VerticalDividerShowkase(): Unit =
    AppTheme {
        VerticalDivider(modifier = Modifier.height(AppTheme.spacing.x3))
    }
