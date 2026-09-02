package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import io.github.maniramezan.compose.components.AppText
import io.github.maniramezan.compose.components.AppTextStyle
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Typography
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun AppTextPage() {
    val styles = listOf("Display", "Title", "Body", "Label")
    var styleIndex by remember { mutableIntStateOf(2) }
    val style =
        when (styleIndex) {
            0 -> AppTextStyle.Display
            1 -> AppTextStyle.Title
            3 -> AppTextStyle.Label
            else -> AppTextStyle.Body
        }

    SamplePage(
        preview = {
            AppText(
                text = "The quick brown fox jumps over the lazy dog.",
                style = style,
            )
        },
        controls = {
            ControlSegmented(
                label = "Style",
                options = styles,
                selectedIndex = styleIndex,
                onOptionSelected = { styleIndex = it },
            )
        },
    )
}

/** One weight-variant slot paired with the M3 base slot it is re-cut from. */
private data class WeightVariantSlot(
    val name: String,
    val base: TextStyle,
    val variant: TextStyle,
)

/**
 * Demonstrates the `AppTypography` weight-variant slots: each M3 base slot
 * (`labelSmall`, `bodyLarge`, …) re-cut at a heavier weight, with size and line
 * height kept on the scale. Toggle the ghost base row to see the weight bump.
 */
@Composable
internal fun TypographyWeightsPage() {
    val typography = AppTheme.typography
    val families = listOf("Label", "Body")
    var familyIndex by remember { mutableIntStateOf(1) }
    var showBase by remember { mutableStateOf(true) }

    val labelVariants =
        listOf(
            WeightVariantSlot("labelSmallSemibold", typography.labelSmall, typography.labelSmallSemibold),
            WeightVariantSlot("labelSmallBold", typography.labelSmall, typography.labelSmallBold),
            WeightVariantSlot("labelMediumBold", typography.labelMedium, typography.labelMediumBold),
            WeightVariantSlot("labelLargeBold", typography.labelLarge, typography.labelLargeBold),
        )
    val bodyVariants =
        listOf(
            WeightVariantSlot("bodySmallMedium", typography.bodySmall, typography.bodySmallMedium),
            WeightVariantSlot("bodySmallSemibold", typography.bodySmall, typography.bodySmallSemibold),
            WeightVariantSlot("bodyMediumSemibold", typography.bodyMedium, typography.bodyMediumSemibold),
            WeightVariantSlot("bodyLargeSemibold", typography.bodyLarge, typography.bodyLargeSemibold),
        )
    val shown = if (familyIndex == 0) labelVariants else bodyVariants

    SamplePage(
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                shown.forEach { slot ->
                    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
                        if (showBase) {
                            Text(
                                text = "base · The quick brown fox",
                                style = slot.base,
                                color = AppTheme.colors.onSurfaceVariant,
                            )
                        }
                        Text(
                            text = "${slot.name} · The quick brown fox",
                            style = slot.variant,
                            color = AppTheme.colors.onSurface,
                        )
                    }
                }
            }
        },
        controls = {
            ControlSegmented(
                label = "Slot family",
                options = families,
                selectedIndex = familyIndex,
                onOptionSelected = { familyIndex = it },
            )
            ControlSwitch(
                label = "Show base slot for comparison",
                checked = showBase,
                onCheckedChange = { showBase = it },
            )
        },
    )
}
