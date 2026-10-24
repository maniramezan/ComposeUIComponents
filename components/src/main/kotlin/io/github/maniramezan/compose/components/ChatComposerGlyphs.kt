package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.Dp

private const val GLYPH_VIEWPORT = 24f
private const val SEND_GLYPH_PATH = "M4 12l1.41 1.41L11 7.83V20h2V7.83l5.58 5.59L20 12l-8-8-8 8z"
private const val STOP_GLYPH_PATH = "M6 6h12v12H6z"
private const val MIC_GLYPH_PATH =
    "M12 14c1.66 0 2.99-1.34 2.99-3L15 5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z" +
        "m5.3-3c0 3-2.54 5.1-5.3 5.1S6.7 14 6.7 11H5c0 3.41 2.72 6.23 6 6.72V21h2v-3.28" +
        "c3.28-.48 6-3.3 6-6.72h-1.7z"

/** The glyphs drawn inside the [ChatComposer] trailing button. */
internal enum class ChatComposerGlyph(
    private val pathData: String,
) {
    Send(SEND_GLYPH_PATH),
    Mic(MIC_GLYPH_PATH),
    Stop(STOP_GLYPH_PATH),
    ;

    /** Builds this glyph as a vector with a default size of [size]. */
    fun toImageVector(size: Dp): ImageVector =
        ImageVector
            .Builder(
                name = "ChatComposer$name",
                defaultWidth = size,
                defaultHeight = size,
                viewportWidth = GLYPH_VIEWPORT,
                viewportHeight = GLYPH_VIEWPORT,
            ).addPath(pathData = addPathNodes(pathData), fill = SolidColor(Color.Black))
            .build()
}

/** Remembers the vector for [glyph] at [size]. */
@Composable
internal fun rememberChatComposerGlyph(
    glyph: ChatComposerGlyph,
    size: Dp,
): ImageVector = remember(glyph, size) { glyph.toImageVector(size) }
