package io.github.maniramezan.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

/**
 * A themed selection list presented as a modal bottom sheet, with optional search and
 * inline two-level disclosure. This single-choice overload marks one row as selected;
 * see the [SelectionSheet] overload taking `selectedIds` for multiple choice.
 *
 * Rows come from a [SelectionSheetNode] tree: leaf nodes are selectable, while parent nodes
 * expand inline to reveal their children. The sheet reports taps through [onSelect] and
 * does not change the selection itself — replace the selection and dismiss in your
 * callback (typically by calling [onDismissRequest] from inside [onSelect]).
 *
 * ```kotlin
 * if (showSheet) {
 *     SelectionSheet(
 *         title = "Category",
 *         nodes = nodes,
 *         selectedId = choice,
 *         isSearchable = true,
 *         onSelect = { choice = it; showSheet = false },
 *         onDismissRequest = { showSheet = false },
 *     )
 * }
 * ```
 *
 * @param selectedId identifier of the selected leaf or child, marked with a check; `null` for none.
 * @param onSelect invoked with the tapped leaf or child identifier.
 * @param onDismissRequest invoked when the sheet is dismissed by scrim tap, drag, or back.
 * @param confirmButton optional slot for a confirm/close control shown at the trailing edge of the header — supply your own localized text button, icon, or image and wire its `onClick` (e.g. to [onDismissRequest]). Usually omitted for single choice, which dismisses on tap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun <ID : Any> SelectionSheet(
    title: String,
    nodes: List<SelectionSheetNode<ID>>,
    selectedId: ID?,
    onSelect: (ID) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    isSearchable: Boolean = false,
    searchPlaceholder: String = "Search",
    noResultsText: String = "No results",
    confirmButton: (@Composable () -> Unit)? = null,
) {
    SelectionSheet(
        title = title,
        nodes = nodes,
        selectedIds = selectedId?.let { setOf(it) } ?: emptySet(),
        onSelect = onSelect,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        isSearchable = isSearchable,
        searchPlaceholder = searchPlaceholder,
        noResultsText = noResultsText,
        confirmButton = confirmButton,
    )
}

/**
 * A themed selection list presented as a modal bottom sheet, with optional search and
 * inline two-level disclosure. This multiple-choice overload marks every row in
 * [selectedIds] as selected; see the [SelectionSheet] overload taking `selectedId` for
 * single choice.
 *
 * Rows come from a [SelectionSheetNode] tree: leaf nodes are selectable, while parent nodes
 * expand inline to reveal their children. The sheet reports taps through [onSelect] and
 * does not change the selection itself — toggle membership in your callback and keep the
 * sheet open. A collapsed parent lists its selected children as its subtitle, and the
 * parent that contains a selection starts expanded.
 *
 * ```kotlin
 * if (showSheet) {
 *     SelectionSheet(
 *         title = "Categories",
 *         nodes = nodes,
 *         selectedIds = choices,
 *         onSelect = { id -> choices = choices.toggling(id) },
 *         onDismissRequest = { showSheet = false },
 *         confirmButton = { TextButton(text = doneLabel, onClick = { showSheet = false }) },
 *     )
 * }
 * ```
 *
 * @param selectedIds identifiers of the selected leaves or children, each marked with a check.
 * @param onSelect invoked with the tapped leaf or child identifier; toggle membership yourself.
 * @param onDismissRequest invoked when the sheet is dismissed by scrim tap, drag, or back.
 * @param isSearchable when `true`, shows a search field that filters across both levels.
 * @param searchPlaceholder hint shown in the search field when it is empty.
 * @param noResultsText shown when a search matches nothing.
 * @param confirmButton optional slot for a confirm/close control shown at the trailing edge of the header — supply your own localized text button, icon, or image and wire its `onClick` (e.g. to [onDismissRequest]). Recommended for multiple choice, which does not dismiss on tap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun <ID : Any> SelectionSheet(
    title: String,
    nodes: List<SelectionSheetNode<ID>>,
    selectedIds: Set<ID>,
    onSelect: (ID) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    isSearchable: Boolean = false,
    searchPlaceholder: String = "Search",
    noResultsText: String = "No results",
    confirmButton: (@Composable () -> Unit)? = null,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        SelectionSheetContent(
            title = title,
            nodes = nodes,
            selectedIds = selectedIds,
            onSelect = onSelect,
            isSearchable = isSearchable,
            searchPlaceholder = searchPlaceholder,
            noResultsText = noResultsText,
            confirmButton = confirmButton,
            modifier = Modifier.weight(1f, fill = false),
        )
    }
}

/**
 * The stateful body of [SelectionSheet] without the surrounding modal bottom sheet.
 *
 * Exposed internally so previews, Showkase entries, and screenshot tests can render the
 * list on a plain surface (a [ModalBottomSheet] does not render in those contexts). It
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
    searchPlaceholder: String = "Search",
    noResultsText: String = "No results",
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
            if (visibleNodes.isEmpty() && isSearching) {
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

@Composable
private fun SelectionSheetRow(
    title: String,
    subtitle: String?,
    leadingGlyph: String?,
    isSelected: Boolean,
    isIndented: Boolean,
    expanded: Boolean?,
    onClick: () -> Unit,
) {
    val interaction =
        if (expanded == null) {
            Modifier.selectable(selected = isSelected, role = Role.Button, onClick = onClick)
        } else {
            Modifier.clickable(role = Role.Button, onClick = onClick)
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .minimumTouchTargetHeight(minimumTouchTargetSize())
                .then(interaction)
                .padding(
                    start = if (isIndented) AppTheme.spacing.x4 else AppTheme.spacing.x2,
                    end = AppTheme.spacing.x2,
                    top = AppTheme.spacing.x1,
                    bottom = AppTheme.spacing.x1,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1_5),
    ) {
        if (leadingGlyph != null) {
            Text(text = leadingGlyph, style = AppTheme.typography.titleLarge)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onSurface,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        when {
            expanded != null ->
                Icon(
                    imageVector = AppTheme.icons.expand.imageVector,
                    contentDescription = null,
                    tint = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f),
                )
            isSelected ->
                Icon(
                    imageVector = AppTheme.icons.check.imageVector,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                )
        }
    }
}

/** The comma-joined titles of [node]'s selected children, or `null` when none are selected. */
private fun <ID : Any> selectedChildrenSummary(
    node: SelectionSheetNode<ID>,
    selectedIds: Set<ID>,
): String? {
    val titles = node.children.filter { it.id in selectedIds }.map { it.title }
    return if (titles.isEmpty()) null else titles.joinToString(", ")
}
