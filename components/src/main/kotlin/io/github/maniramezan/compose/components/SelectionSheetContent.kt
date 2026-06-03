package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme

/**
 * The stateful body of [SelectionSheet] without the surrounding modal bottom sheet.
 *
 * Exposed internally so previews, Showkase entries, and screenshot tests can render the
 * list on a plain surface (a `ModalBottomSheet` does not render in those contexts). It
 * owns the search query and inline-expansion state.
 */
@Composable
internal fun <ID : Any> SelectionSheetContent(
    title: String,
    nodes: List<SelectionSheetNode<ID>>,
    selectedIds: Set<ID>,
    onSelect: (ID) -> Unit,
    modifier: Modifier = Modifier,
    isSearchable: Boolean = false,
    searchPlaceholder: String = "",
    noResultsText: String = "",
    expandedDescription: String = "",
    collapsedDescription: String = "",
    confirmButton: (@Composable () -> Unit)? = null,
) {
    var query by remember { mutableStateOf("") }
    var expandedIds by remember {
        mutableStateOf(
            nodes
                .filter { parent -> parent.children.any { it.id in selectedIds } }
                .map { it.id }
                .toSet(),
        )
    }

    val isSearching = isSearchable && query.isNotBlank()
    val visibleNodes =
        remember(nodes, query, isSearchable) {
            filterSelectionSheetNodes(nodes, if (isSearchable) query else "")
        }

    Column(modifier = modifier.fillMaxWidth()) {
        SelectionSheetHeader(title = title, confirmButton = confirmButton)

        if (isSearchable) {
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = searchPlaceholder,
                modifier =
                    Modifier.padding(
                        horizontal = AppTheme.spacing.x2,
                        vertical = AppTheme.spacing.x1,
                    ),
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
        ) {
            if (visibleNodes.isEmpty() && isSearching && noResultsText.isNotBlank()) {
                Text(
                    text = noResultsText,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(AppTheme.spacing.x3),
                )
            }

            visibleNodes.forEach { node ->
                if (node.isLeaf) {
                    SelectionSheetRow(
                        title = node.title,
                        subtitle = node.subtitle,
                        leadingGlyph = node.leadingGlyph,
                        isSelected = node.id in selectedIds,
                        isIndented = false,
                        expanded = null,
                        onClick = { onSelect(node.id) },
                    )
                } else {
                    val expanded = isSearching || node.id in expandedIds
                    SelectionSheetRow(
                        title = node.title,
                        subtitle = selectedChildrenSummary(node, selectedIds) ?: node.subtitle,
                        leadingGlyph = node.leadingGlyph,
                        isSelected = false,
                        isIndented = false,
                        expanded = expanded,
                        onClick = {
                            expandedIds =
                                if (node.id in expandedIds) expandedIds - node.id else expandedIds + node.id
                        },
                        expandedDescription = expandedDescription,
                        collapsedDescription = collapsedDescription,
                    )
                    if (expanded) {
                        node.children.forEach { child ->
                            SelectionSheetRow(
                                title = child.title,
                                subtitle = child.subtitle,
                                leadingGlyph = child.leadingGlyph,
                                isSelected = child.id in selectedIds,
                                isIndented = true,
                                expanded = null,
                                onClick = { onSelect(child.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/** The sheet title, with an optional trailing confirm control. */
@Composable
private fun SelectionSheetHeader(
    title: String,
    confirmButton: (@Composable () -> Unit)?,
) {
    if (confirmButton != null) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.titleLarge,
                color = AppTheme.colors.onSurface,
                modifier =
                    Modifier
                        .weight(1f)
                        .semantics { heading() },
            )
            confirmButton()
        }
    } else {
        Text(
            text = title,
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colors.onSurface,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1)
                    .semantics { heading() },
        )
    }
}
