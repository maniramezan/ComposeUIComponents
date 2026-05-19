package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.theme.AppTheme

@Composable
public fun ListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.lg, vertical = AppTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.invoke()
        Column(modifier = Modifier.weight(1f)) {
            Text(text = headline)
            if (supportingText != null) {
                Text(text = supportingText)
            }
        }
        trailingContent?.invoke()
    }
}

@Composable
public fun LazyList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = AppTheme.spacing.sm),
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
public fun EmptyState(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    action: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    StateColumn(modifier = modifier) {
        leadingContent?.invoke()
        Text(text = title)
        if (message != null) {
            Text(text = message)
        }
        action?.invoke()
    }
}

@Composable
public fun LoadingState(
    label: String? = null,
    modifier: Modifier = Modifier,
) {
    StateColumn(modifier = modifier) {
        CircularProgressIndicator()
        if (label != null) {
            Text(text = label)
        }
    }
}

@Composable
public fun ErrorState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    StateColumn(modifier = modifier) {
        leadingContent?.invoke()
        Text(text = title)
        Text(text = message)
        action?.invoke()
    }
}

@Composable
private fun StateColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        content = content,
    )
}
