package io.github.maniramezan.compose.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A themed selection list presented as a modal bottom sheet, with optional search and
 * inline two-level disclosure. This single-choice overload marks one row as selected;
 * see the [SelectionSheet] overload taking `selectedIds` for multiple choice.
 *
 * Rows come from a [SelectionSheetNode] tree: leaf nodes are selectable, while parent nodes
 * expand inline to reveal their children. Dismissal depends on [confirmButton]:
 * - **No [confirmButton]** (default): tapping an item reports it through [onSelect] **and**
 *   dismisses via [onDismissRequest]. Just update your selection in [onSelect].
 * - **With a [confirmButton]**: tapping items only updates the selection (the sheet stays
 *   open); dismissal happens when the confirm control is used.
 *
 * ```kotlin
 * if (showSheet) {
 *     SelectionSheet(
 *         title = "Category",
 *         nodes = nodes,
 *         selectedId = choice,
 *         isSearchable = true,
 *         onSelect = { choice = it },          // sheet dismisses automatically on tap
 *         onDismissRequest = { showSheet = false },
 *     )
 * }
 * ```
 *
 * @param selectedId identifier of the selected leaf or child, marked with a check; `null` for none.
 * @param onSelect invoked with the tapped leaf or child identifier; just update your selection — dismissal is handled per [confirmButton].
 * @param onDismissRequest invoked when the sheet is dismissed by scrim tap, drag, back, item tap (when there is no [confirmButton]), or the confirm control.
 * @param confirmButton optional slot for a confirm/close control shown at the trailing edge of the header — supply your own localized text button, icon, or image and wire its `onClick` (e.g. to [onDismissRequest]). When provided, item taps no longer dismiss; only the confirm control does.
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
        // Without a confirm control, single choice dismisses on tap; with one, only the
        // confirm control dismisses so the user can revise the selection first.
        onSelect =
            if (confirmButton == null) {
                { id ->
                    onSelect(id)
                    onDismissRequest()
                }
            } else {
                onSelect
            },
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
