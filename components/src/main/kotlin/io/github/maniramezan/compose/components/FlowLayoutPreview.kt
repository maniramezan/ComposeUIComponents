package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Flow Layout", group = "Layout")
@Composable
public fun FlowLayoutPreview(): Unit =
    AppTheme {
        FlowLayout {
            listOf("Synonym", "A longer related term", "Antonym", "Variant").forEach { label ->
                Text(
                    text = label,
                    modifier =
                        Modifier
                            .clip(AppTheme.shapes.pill)
                            .background(AppTheme.colors.surfaceVariant)
                            .padding(AppTheme.spacing.x1),
                )
            }
        }
    }

@ShowkaseComposable(name = "Flow Layout", group = "Layout")
@Composable
public fun FlowLayoutShowkase(): Unit = FlowLayoutPreview()
