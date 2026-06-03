package io.github.maniramezan.compose.components

import androidx.compose.runtime.Immutable

/**
 * A node in a one- or two-level [SelectionSheet] hierarchy.
 *
 * A node with no [children] is a **leaf** that selects directly when tapped. A node
 * with children is an **expandable parent**: tapping it reveals its children inline,
 * and selecting one reports that child's [id]. Build a flat list of leaves for a
 * single-level picker, or mix leaves and parents for a two-level picker (for example,
 * languages whose pluricentric entries expand into regional variants).
 *
 * Identifiers must be unique across the whole tree, since they are used as list keys
 * and as the selection values reported by [SelectionSheet].
 *
 * @param ID the identifier type reported through selection.
 * @property id stable, unique identifier reported back through selection.
 * @property title primary row text (e.g. an autonym such as "Español" or a region name).
 * @property subtitle optional secondary text shown beneath the title (e.g. a locale tag).
 * @property leadingGlyph optional leading glyph rendered before the title, typically an emoji or flag.
 * @property children child nodes revealed when an expandable parent is tapped; empty for a leaf.
 */
@Immutable
public data class SelectionSheetNode<ID : Any>(
    public val id: ID,
    public val title: String,
    public val subtitle: String? = null,
    public val leadingGlyph: String? = null,
    public val children: List<SelectionSheetNode<ID>> = emptyList(),
) {
    /** Whether the node has no children and is therefore directly selectable. */
    public val isLeaf: Boolean get() = children.isEmpty()
}
