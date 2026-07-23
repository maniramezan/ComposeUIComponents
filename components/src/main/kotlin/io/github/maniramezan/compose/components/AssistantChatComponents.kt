package io.github.maniramezan.compose.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTargetHeight

private const val DisabledQuickActionAlpha = 0.45f

/** Optional body line for [AssistantContextCard]. */
@Immutable
public data class AssistantContextBody(
    val text: String,
    val isQuoted: Boolean = false,
)

/** State for one quick-action chip in [AssistantQuickActionChips]. */
@Immutable
public data class AssistantQuickActionState(
    val isSelected: Boolean,
    val isEnabled: Boolean,
)

/** Caller-supplied copy for [AssistantLimitPromptCard]. */
@Immutable
public data class AssistantLimitPromptCopy(
    val message: String,
    val supportingText: String,
    val primaryActionLabel: String,
    val secondaryActionLabel: String,
)

/**
 * Context card for assistant-style flows: a title, optional highlighted value,
 * optional body line, and optional footnote.
 */
@Composable
public fun AssistantContextCard(
    title: String,
    modifier: Modifier = Modifier,
    highlight: String? = null,
    body: AssistantContextBody? = null,
    footnote: String? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.titleLarge,
                color = AppTheme.colors.onSurface,
            )
            if (!highlight.isNullOrBlank()) {
                Text(
                    text = highlight,
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colors.primary,
                )
            }
            if (body != null && body.text.isNotBlank()) {
                Text(
                    text = if (body.isQuoted) "\"${body.text}\"" else body.text,
                    style = AppTheme.typography.bodyMedium,
                    fontStyle = if (body.isQuoted) FontStyle.Italic else FontStyle.Normal,
                    color = AppTheme.colors.onSurfaceVariant,
                )
            }
            if (!footnote.isNullOrBlank()) {
                Text(
                    text = footnote,
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Generic horizontal quick-action chip row for assistant/chat flows.
 *
 * Callers own the action type, labels, selection rules, and enablement rules.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun <T> AssistantQuickActionChips(
    actions: List<T>,
    actionState: (T) -> AssistantQuickActionState,
    label: @Composable (T) -> String,
    onAction: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
    ) {
        actions.forEach { action ->
            val state = actionState(action)
            FilterChip(
                selected = state.isSelected,
                enabled = state.isEnabled,
                onClick = { onAction(action) },
                label = { Text(label(action)) },
                modifier = Modifier.minimumTouchTargetHeight(minimumTouchTargetSize()),
                colors =
                    FilterChipDefaults.filterChipColors(
                        containerColor = AppTheme.colors.surface,
                        labelColor = AppTheme.colors.onSurfaceVariant,
                        selectedContainerColor = AppTheme.colors.primaryContainer,
                        selectedLabelColor = AppTheme.colors.onPrimaryContainer,
                        disabledContainerColor = AppTheme.colors.surfaceVariant,
                        disabledLabelColor = AppTheme.colors.onSurfaceVariant.copy(alpha = DisabledQuickActionAlpha),
                        disabledSelectedContainerColor =
                            AppTheme.colors.primaryContainer.copy(alpha = DisabledQuickActionAlpha),
                        disabledLeadingIconColor =
                            AppTheme.colors.onSurfaceVariant.copy(alpha = DisabledQuickActionAlpha),
                    ),
            )
        }
    }
}

/** Status banner for unavailable providers, degraded service, or blocking errors. */
@Composable
public fun AssistantStatusBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.error),
    ) {
        Text(
            text = message,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.onError,
            modifier = Modifier.padding(AppTheme.spacing.x2),
        )
    }
}

/** Prompt card for assistant limits, quotas, upgrades, or other gated actions. */
@Composable
public fun AssistantLimitPromptCard(
    copy: AssistantLimitPromptCopy,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        ) {
            Text(
                text = copy.message,
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onSurface,
            )
            Text(
                text = copy.supportingText,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.onSurfaceVariant,
            )
            Button(onClick = onPrimaryAction, modifier = Modifier.fillMaxWidth()) {
                Text(text = copy.primaryActionLabel)
            }
            TextButton(onClick = onSecondaryAction, modifier = Modifier.fillMaxWidth()) {
                Text(text = copy.secondaryActionLabel)
            }
        }
    }
}

/** Persistent low-emphasis assistant disclaimer or policy footer. */
@Composable
public fun AssistantDisclaimerFooter(
    text: String,
    modifier: Modifier = Modifier,
) {
    if (text.isBlank()) return

    Text(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.x2, vertical = AppTheme.spacing.x1_5),
        text = text,
        style = AppTheme.typography.labelSmall,
        color = AppTheme.colors.onSurfaceVariant,
    )
}
