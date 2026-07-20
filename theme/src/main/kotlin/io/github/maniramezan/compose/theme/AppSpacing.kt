package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.tokens.AppReferenceSpacing

/**
 * Spacing scale on an 8-dp grid (with a 4-dp "half" step). Apps can override
 * any slot when constructing [AppSpacing]; defaults are provided so the
 * library is usable out of the box.
 *
 * Two parallel naming schemes are exposed:
 *  - **Multiplier scale** (`half`, `x1`, `x1_5`, `x2`, …): names that encode
 *    the value's multiplier of the 8-dp base. Preferred for new code.
 *  - **T-shirt scale** (`xs`, `sm`, `md`, `lg`, `xl`): kept for back-compat
 *    with the original library API and Material-style component sizing.
 *
 * Layout-dimension slots (`gridItemMinWidth`, `maxContentWidthForm`, …)
 * default to `0.dp`. Apps that use those layouts must supply real values.
 */
@Immutable
@Suppress("ConstructorParameterNaming")
public data class AppSpacing(
    // ===== Multiplier scale =====
    public val half: Dp,
    public val x1: Dp,
    public val x1_5: Dp,
    public val x2: Dp,
    public val x3: Dp,
    public val x4: Dp,
    public val x5: Dp,
    public val x6: Dp,
    public val x7: Dp,
    public val x8: Dp,
    public val x9: Dp,
    // ===== T-shirt scale (back-compat) =====
    public val xs: Dp,
    public val sm: Dp,
    public val md: Dp,
    public val lg: Dp,
    public val xl: Dp,
    // ===== Semantic aliases =====
    public val padding: Dp,
    public val contentPadding: Dp,
    public val itemSpacing: Dp,
    public val gridSpacing: Dp,
    // ===== Strokes =====
    public val strokeThin: Dp,
    public val strokeRegular: Dp,
    public val strokeThick: Dp,
    // ===== Interaction =====
    public val minTapTarget: Dp,
    // ===== Layout dimensions (apps override) =====
    public val gridItemMinWidth: Dp = 0.dp,
    public val gridImageHeight: Dp = 0.dp,
    public val videoCardThumbnailHeight: Dp = 0.dp,
    public val maxContentWidthForm: Dp = 0.dp,
    public val maxContentWidthList: Dp = 0.dp,
    public val maxContentWidthPlayer: Dp = 0.dp,
) {
    public companion object {
        public fun default(): AppSpacing =
            AppSpacing(
                half = AppReferenceSpacing.Space4,
                x1 = AppReferenceSpacing.Space8,
                x1_5 = AppReferenceSpacing.Space12,
                x2 = AppReferenceSpacing.Space16,
                x3 = AppReferenceSpacing.Space24,
                x4 = AppReferenceSpacing.Space32,
                x5 = AppReferenceSpacing.Space40,
                x6 = AppReferenceSpacing.Space48,
                x7 = AppReferenceSpacing.Space56,
                x8 = AppReferenceSpacing.Space64,
                x9 = AppReferenceSpacing.Space72,
                xs = AppReferenceSpacing.Space4,
                sm = AppReferenceSpacing.Space8,
                md = AppReferenceSpacing.Space12,
                lg = AppReferenceSpacing.Space16,
                xl = AppReferenceSpacing.Space24,
                padding = AppReferenceSpacing.Space16,
                contentPadding = AppReferenceSpacing.Space24,
                itemSpacing = AppReferenceSpacing.Space8,
                gridSpacing = AppReferenceSpacing.Space16,
                strokeThin = AppReferenceSpacing.Space1,
                strokeRegular = AppReferenceSpacing.Space2,
                strokeThick = AppReferenceSpacing.Space4,
                minTapTarget = AppReferenceSpacing.Space44,
                maxContentWidthForm = AppReferenceSpacing.Space600,
            )
    }
}

/**
 * Non-composable, static counterpart of [AppSpacing.default]. Lets non-`@Composable`
 * code (initializer blocks, top-level extensions, helper objects) reference the
 * same default values without needing a composition context.
 *
 * For themable, theme-aware values inside composables, prefer `AppTheme.spacing`.
 */
public object AppSpacingDefaults {
    public val half: Dp = AppReferenceSpacing.Space4
    public val x1: Dp = AppReferenceSpacing.Space8
    public val x1_5: Dp = AppReferenceSpacing.Space12
    public val x2: Dp = AppReferenceSpacing.Space16
    public val x3: Dp = AppReferenceSpacing.Space24
    public val x4: Dp = AppReferenceSpacing.Space32
    public val x5: Dp = AppReferenceSpacing.Space40
    public val x6: Dp = AppReferenceSpacing.Space48
    public val x7: Dp = AppReferenceSpacing.Space56
    public val x8: Dp = AppReferenceSpacing.Space64
    public val x9: Dp = AppReferenceSpacing.Space72
    public val padding: Dp = AppReferenceSpacing.Space16
    public val contentPadding: Dp = AppReferenceSpacing.Space24
    public val itemSpacing: Dp = AppReferenceSpacing.Space8
    public val gridSpacing: Dp = AppReferenceSpacing.Space16
    public val strokeThin: Dp = AppReferenceSpacing.Space1
    public val strokeRegular: Dp = AppReferenceSpacing.Space2
    public val strokeThick: Dp = AppReferenceSpacing.Space4
    public val minTapTarget: Dp = AppReferenceSpacing.Space44
    public val maxContentWidthForm: Dp = AppReferenceSpacing.Space600
}
