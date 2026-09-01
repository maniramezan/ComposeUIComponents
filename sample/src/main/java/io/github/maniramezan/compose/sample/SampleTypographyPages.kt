package io.github.maniramezan.compose.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle

// ─────────────────────────────────────────────────────────────────────────────
// Typography
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun AppTextPage() {
    val styles = listOf("Display", "Title", "Body", "Label")
    var styleIndex by remember { mutableIntStateOf(2) }
    val style =
        when (styleIndex) {
            0 -> AppTextStyle.Display
            1 -> AppTextStyle.Title
            3 -> AppTextStyle.Label
            else -> AppTextStyle.Body
        }

    SamplePage(
        preview = {
            AppText(
                text = "The quick brown fox jumps over the lazy dog.",
                style = style,
            )
        },
        controls = {
            ControlSegmented(
                label = "Style",
                options = styles,
                selectedIndex = styleIndex,
                onOptionSelected = { styleIndex = it },
            )
        },
    )
}
