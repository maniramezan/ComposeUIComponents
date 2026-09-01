package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Action Pill", group = "Actions")
@Composable
public fun ActionPillPreview(): Unit =
    AppTheme {
        ActionPill(onClick = {}) { Text(text = "Open related word") }
    }

@ShowkaseComposable(name = "Action Pill", group = "Actions")
@Composable
public fun ActionPillShowkase(): Unit = ActionPillPreview()
