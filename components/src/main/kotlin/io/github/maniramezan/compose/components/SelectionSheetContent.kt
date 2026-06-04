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
 * The embeddable body of [SelectionSheet] — the searchable two-level selection list
 * without the surrounding `ModalBottomSheet`.
 *
 * Use this directly to place the list inside your own screen (for example a full-screen
 * onboarding step that already has its own header and primary button); use [SelectionSheet]
 * when you want it presented as a modal bottom sheet. It owns the search query and
 * inline-expansion state. A blank [title] with no [confirmButton] renders no header, so the
 * host screen can supply its own.
 */
@Composable
public fun <ID : Any> SelectionSheetContent(
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
    when {
        confirmButton != null ->
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
        // Blank title with no confirm control: render no header so the host screen
        // (e.g. an inline onboarding step) can supply its own.
        title.isNotBlank() ->
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
