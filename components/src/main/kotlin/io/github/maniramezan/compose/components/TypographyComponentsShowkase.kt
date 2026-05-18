package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Typography", group = "Typography")
@Composable
public fun TypographyComponentsShowkase(): Unit = TypographyComponentsPreview()

@ShowkaseComposable(name = "App Text", group = "Typography")
@Composable
public fun AppTextShowkase(): Unit =
    AppTheme {
        AppText(text = "Body text for reading content.")
    }
