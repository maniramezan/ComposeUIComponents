package io.github.maniramezan.compose.components

/** Whether the node's title or subtitle contains [query], case-insensitively. */
internal fun SelectionSheetNode<*>.matches(query: String): Boolean =
    title.contains(query, ignoreCase = true) || (subtitle?.contains(query, ignoreCase = true) == true)

/**
 * Filters [nodes] against [query], case-insensitively, preserving order.
 *
 * Leaves are kept when their title or subtitle matches. A parent is kept whole when
 * its own title/subtitle matches; otherwise it is kept only if some child matches,
 * pruned to those children. An empty or blank query returns every node unchanged.
 */
internal fun <ID : Any> filterSelectionSheetNodes(
    nodes: List<SelectionSheetNode<ID>>,
    query: String,
): List<SelectionSheetNode<ID>> {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return nodes
    return nodes.mapNotNull { node ->
        when {
            node.isLeaf -> if (node.matches(trimmed)) node else null
            node.matches(trimmed) -> node
            else -> {
                val matching = node.children.filter { it.matches(trimmed) }
                if (matching.isEmpty()) null else node.copy(children = matching)
            }
        }
    }
}
