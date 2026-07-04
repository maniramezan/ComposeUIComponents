package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

private val topApps = listOf("Focus", "Sky Notes", "Trailhead", "Loop", "Pixel Paint")
private val games = listOf("Nova Run", "Blocks!", "Chess+", "Dungeon", "Kart Rally")
private val editorPicks = listOf("Aperture", "Waves", "Ledger", "Verse")

@PreviewLightDark
@PreviewFontScale
@Preview(name = "ShowcaseFeed", group = "Layout")
@Composable
public fun ShowcaseFeedPreview(): Unit =
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.colors.background)) {
            ShowcaseFeed {
                section(
                    title = "Top Apps",
                    items = topApps,
                    actionLabel = "See all",
                    onAction = {},
                ) { app -> ShowcaseCard(label = app) }

                section(
                    title = "New Games",
                    items = games,
                ) { game -> ShowcaseCard(label = game) }

                customSection {
                    ShowcaseBanner(label = "Editor's Choice")
                }
            }
        }
    }

@PreviewLightDark
@Preview(name = "ShowcaseRow – Peek", group = "Layout")
@Composable
public fun ShowcaseRowPeekPreview(): Unit =
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.colors.background)) {
            ShowcaseRow(items = topApps) { app -> ShowcaseCard(label = app) }
        }
    }

@PreviewLightDark
@Preview(name = "ShowcaseRow – Fixed", group = "Layout")
@Composable
public fun ShowcaseRowFixedPreview(): Unit =
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.colors.background)) {
            ShowcaseRow(
                items = games,
                itemWidth = ShowcaseItemWidth.Fixed(140.dp),
            ) { game -> ShowcaseCard(label = game) }
        }
    }

@PreviewLightDark
@Preview(name = "ShowcaseFeed – Grid Section", group = "Layout")
@Composable
public fun ShowcaseGridSectionPreview(): Unit =
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.colors.background)) {
            ShowcaseFeed {
                section(
                    title = "Top Charts",
                    items = editorPicks + games,
                    itemWidth = ShowcaseItemWidth.Fixed(200.dp),
                    rows = 2,
                    rowHeight = 56.dp,
                ) { item ->
                    ShowcaseCard(label = item, modifier = Modifier.fillMaxHeight().fillMaxWidth())
                }
            }
        }
    }

@Composable
private fun ShowcaseCard(
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth().height(AppTheme.spacing.xl * 3.5f),
) {
    Box(
        modifier =
            modifier
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.titleSmall,
            color = AppTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun ShowcaseBanner(label: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.x2)
                .height(AppTheme.spacing.xl * 4f)
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.onPrimaryContainer,
        )
    }
}
