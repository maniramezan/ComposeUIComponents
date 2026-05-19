package io.github.maniramezan.compose.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

/**
 * Semantic shape slots. Components should prefer these tokens over inline
 * `RoundedCornerShape(…)` so corner radii stay consistent and can be themed.
 *
 * Slots are typed as [CornerBasedShape] so they can be projected straight into
 * Material 3's `Shapes` (which requires corner-based shapes).
 */
@Immutable
public data class AppShapes(
    public val standard: CornerBasedShape,
    public val small: CornerBasedShape,
    public val large: CornerBasedShape,
    public val image: CornerBasedShape,
    public val pill: CornerBasedShape,
    public val badge: CornerBasedShape,
) {
    public companion object {
        public fun default(): AppShapes =
            AppShapes(
                standard = RoundedCornerShape(12.dp),
                small = RoundedCornerShape(8.dp),
                large = RoundedCornerShape(16.dp),
                image = RoundedCornerShape(8.dp),
                pill = CircleShape,
                badge = RoundedCornerShape(4.dp),
            )
    }
}
