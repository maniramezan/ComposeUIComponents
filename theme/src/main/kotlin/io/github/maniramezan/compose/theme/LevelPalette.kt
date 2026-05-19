package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * One tier of a tiered badge palette — a background color paired with the
 * foreground (label) color that reads correctly on top of it.
 */
@Immutable
public data class LevelTier(
    public val background: Color,
    public val foreground: Color,
)

/**
 * Ordered set of [LevelTier]s for components that show categorical "levels"
 * (skill, difficulty, priority, …). Apps supply their own tiers; the library
 * provides an empty default.
 */
@Immutable
public data class LevelPalette(
    public val tiers: List<LevelTier>,
) {
    public val size: Int get() = tiers.size

    /**
     * Returns the tier at [index], clamping to the last tier if [index] is
     * out of range. Returns a transparent tier if the palette is empty.
     */
    public fun tier(index: Int): LevelTier =
        when {
            tiers.isEmpty() -> EmptyTier
            index < 0 -> tiers.first()
            index >= tiers.size -> tiers.last()
            else -> tiers[index]
        }

    public companion object {
        private val EmptyTier = LevelTier(background = Color.Transparent, foreground = Color.Transparent)

        public fun empty(): LevelPalette = LevelPalette(emptyList())
    }
}
