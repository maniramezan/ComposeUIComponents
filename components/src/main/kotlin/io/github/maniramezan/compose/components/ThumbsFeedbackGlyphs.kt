package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.Dp

private const val GLYPH_VIEWPORT = 24f
private const val THUMB_UP_GLYPH_PATH =
    "M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1" +
        " 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05" +
        "c.09-.23.14-.47.14-.73v-2z"
private const val THUMB_DOWN_GLYPH_PATH =
    "M15 3H6c-.83 0-1.54.5-1.84 1.22l-3.02 7.05c-.09.23-.14.47-.14.73v2c0 1.1.9 2 2 2h6.31" +
        "l-.95 4.57-.03.32c0 .41.17.79.44 1.06L9.83 23l6.59-6.59c.36-.36.58-.86.58-1.41V5" +
        "c0-1.1-.9-2-2-2zm4 0v12h4V3h-4z"

/** The glyphs drawn inside a [ThumbsFeedbackRow]. */
internal enum class ThumbsFeedbackGlyph(
    private val pathData: String,
) {
    ThumbUp(THUMB_UP_GLYPH_PATH),
    ThumbDown(THUMB_DOWN_GLYPH_PATH),
    ;

    /** Builds this glyph as a vector with a default size of [size]. */
    fun toImageVector(size: Dp): ImageVector =
        ImageVector
            .Builder(
                name = "ThumbsFeedback$name",
                defaultWidth = size,
                defaultHeight = size,
                viewportWidth = GLYPH_VIEWPORT,
                viewportHeight = GLYPH_VIEWPORT,
            ).addPath(pathData = addPathNodes(pathData), fill = SolidColor(Color.Black))
            .build()
}

/** Remembers the vector for [glyph] at [size]. */
@Composable
internal fun rememberThumbsFeedbackGlyph(
    glyph: ThumbsFeedbackGlyph,
    size: Dp,
): ImageVector = remember(glyph, size) { glyph.toImageVector(size) }
