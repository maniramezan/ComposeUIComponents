package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme
import androidx.compose.material3.Card as MaterialCard
import androidx.compose.material3.Snackbar as MaterialSnackbar
import androidx.compose.material3.Surface as MaterialSurface
import androidx.compose.material3.TextButton as MaterialTextButton

/** A themed, elevated container for grouping related content, optionally tappable via [onClick]. */
@Composable
public fun Card(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(AppTheme.spacing.lg),
    content: @Composable ColumnScope.() -> Unit,
) {
    val columnContent: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            content = content,
        )
    }
    if (onClick != null) {
        MaterialCard(modifier = modifier.fillMaxWidth(), onClick = onClick, content = columnContent)
    } else {
        MaterialCard(modifier = modifier.fillMaxWidth(), content = columnContent)
    }
}

/** A themed, flat (non-elevated) container for grouping related content. */
@Composable
public fun Surface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AppTheme.spacing.lg),
    content: @Composable ColumnScope.() -> Unit,
) {
    MaterialSurface(
        modifier = modifier.fillMaxWidth(),
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            content = content,
        )
    }
}

/**
 * A labeled content group: a heading row (with optional trailing [actions]) above [content].
 */
@Composable
public fun Section(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = title, modifier = Modifier.semantics { heading() })
            actions()
        }
        content()
    }
}

/** A themed modal bottom sheet that is always fully expanded (never partially). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            content = content,
        )
    }
}

/**
 * A themed alert dialog with a required confirm action and an optional dismiss action.
 *
 * @param dismissText Label for a secondary dismiss button; omit (`null`) for a
 *   confirm-only dialog.
 */
@Composable
public fun Dialog(
    title: String,
    text: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            MaterialTextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton =
            dismissText?.let {
                {
                    MaterialTextButton(onClick = onDismissRequest) {
                        Text(text = it)
                    }
                }
            },
    )
}

/**
 * A themed snackbar message, usable standalone or as `SnackbarHost`'s `snackbar` slot.
 *
 * @param actionLabel Label for an optional trailing action button; supply together
 *   with [onAction], or omit both for a message-only snackbar.
 */
@Composable
public fun Snackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    MaterialSnackbar(
        // Announce when shown standalone (outside a SnackbarHost, which would
        // otherwise supply the live region) without stealing focus.
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        action =
            if (actionLabel != null && onAction != null) {
                {
                    MaterialTextButton(onClick = onAction) {
                        Text(text = actionLabel)
                    }
                }
            } else {
                null
            },
    ) {
        Text(text = message)
    }
}
