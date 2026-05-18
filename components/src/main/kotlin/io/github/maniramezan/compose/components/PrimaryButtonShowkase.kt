package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Primary Button", group = "Actions")
@Composable
public fun PrimaryButtonShowkase(): Unit = AppTheme {
    PrimaryButton(
        text = "Continue",
        onClick = {},
    )
}
