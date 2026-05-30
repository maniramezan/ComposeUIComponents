package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

private val threeShortItems =
    listOf(
        SegmentedItem(title = "Overview"),
        SegmentedItem(title = "Activity"),
        SegmentedItem(title = "Settings"),
    )

private val sevenLongItems =
    listOf(
        SegmentedItem(title = "Overview"),
        SegmentedItem(title = "Activity"),
        SegmentedItem(title = "Settings"),
        SegmentedItem(title = "Notifications"),
        SegmentedItem(title = "Permissions"),
        SegmentedItem(title = "Integrations"),
        SegmentedItem(title = "Billing"),
    )

@PreviewLightDark
@PreviewFontScale
@Preview(name = "SegmentedContent – Fits Evenly", group = "Navigation")
@Composable
public fun SegmentedContentPreview(): Unit =
    AppTheme {
        SegmentedContent(
            items = threeShortItems,
            initialSelectedIndex = 0,
            indicator = SegmentSelectionIndicator.Pill,
            fitMode = SegmentFitMode.EvenWhenFits,
        ) { _, item -> SegmentPlaceholder(item.title) }
    }

@PreviewLightDark
@Preview(name = "SegmentedContent – Overflow Scrollable", group = "Navigation")
@Composable
public fun SegmentedContentOverflowPreview(): Unit =
    AppTheme {
        SegmentedContent(
            items = sevenLongItems,
            initialSelectedIndex = 3,
            indicator = SegmentSelectionIndicator.Pill,
            fitMode = SegmentFitMode.EvenWhenFits,
        ) { _, item -> SegmentPlaceholder(item.title) }
    }

@PreviewLightDark
@Preview(name = "SegmentedContent – Underline", group = "Navigation")
@Composable
public fun SegmentedContentUnderlinePreview(): Unit =
    AppTheme {
        SegmentedContent(
            items = threeShortItems + SegmentedItem(title = "Members"),
            initialSelectedIndex = 1,
            indicator = SegmentSelectionIndicator.Underline,
            fitMode = SegmentFitMode.EvenWhenFits,
        ) { _, item -> SegmentPlaceholder(item.title) }
    }

@PreviewLightDark
@Preview(name = "SegmentedContent – Custom Title Slot", group = "Navigation")
@Composable
public fun SegmentedContentCustomTitlePreview(): Unit =
    AppTheme {
        SegmentedContent(
            items = threeShortItems + SegmentedItem(title = "Members"),
            initialSelectedIndex = 0,
            indicator = SegmentSelectionIndicator.Pill,
            fitMode = SegmentFitMode.EvenWhenFits,
            segmentTitle = { _, item, isSelected -> IconSegmentTitle(item.title, isSelected) },
        ) { _, item -> SegmentPlaceholder(item.title) }
    }

@PreviewLightDark
@Preview(name = "SegmentedContent – No Indicator", group = "Navigation")
@Composable
public fun SegmentedContentNoIndicatorPreview(): Unit =
    AppTheme {
        SegmentedContent(
            items = threeShortItems,
            initialSelectedIndex = 0,
            indicator = SegmentSelectionIndicator.None,
            fitMode = SegmentFitMode.EvenWhenFits,
        ) { _, item -> SegmentPlaceholder(item.title) }
    }

@Composable
private fun SegmentPlaceholder(title: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(AppTheme.spacing.xl * 4f)
                .padding(AppTheme.spacing.x2),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$title content",
            style = AppTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun IconSegmentTitle(
    title: String,
    isSelected: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.half),
    ) {
        val color =
            if (isSelected) AppTheme.colors.onPrimary else AppTheme.colors.onSurfaceVariant
        Icon(
            imageVector = AppTheme.icons.check.imageVector,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(AppTheme.spacing.x2),
        )
        Text(
            text = title,
            style = AppTheme.typography.labelLarge,
            color = color,
            maxLines = 1,
        )
    }
}
