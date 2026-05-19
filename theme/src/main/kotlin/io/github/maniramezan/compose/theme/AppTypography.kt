package io.github.maniramezan.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography slots exposed by [AppTheme]. Three groups of slots are provided:
 *
 *  - **Original 4-slot scale** (`display`, `title`, `body`, `label`) — kept for
 *    back-compat with the library's first cut. New components should prefer
 *    the Material 3 slots below.
 *  - **Material 3 standard scale** (`displayLarge` … `labelSmall`) — matches
 *    the Material 3 type system slot-for-slot.
 *  - **Custom extensions** — semantic styles common in product UIs but not in
 *    the M3 scale (`subtitle`, monospaced styles, oversized icon glyphs, …).
 */
@Immutable
public data class AppTypography(
    // ===== Original 4-slot scale (back-compat) =====
    public val display: TextStyle,
    public val title: TextStyle,
    public val body: TextStyle,
    public val label: TextStyle,
    // ===== Material 3 standard scale =====
    public val displayLarge: TextStyle,
    public val displayMedium: TextStyle,
    public val displaySmall: TextStyle,
    public val headlineLarge: TextStyle,
    public val headlineMedium: TextStyle,
    public val headlineSmall: TextStyle,
    public val titleLarge: TextStyle,
    public val titleMedium: TextStyle,
    public val titleSmall: TextStyle,
    public val bodyLarge: TextStyle,
    public val bodyMedium: TextStyle,
    public val bodySmall: TextStyle,
    public val labelLarge: TextStyle,
    public val labelMedium: TextStyle,
    public val labelSmall: TextStyle,
    // ===== Custom extensions =====
    public val subtitle: TextStyle,
    public val bodyMonospaced: TextStyle,
    public val captionMonospaced: TextStyle,
    public val displayRounded: TextStyle,
    public val iconSmall: TextStyle,
    public val iconMedium: TextStyle,
    public val iconLarge: TextStyle,
    public val iconExtraLarge: TextStyle,
) {
    public companion object {
        public fun default(): AppTypography {
            // Material 3 default type scale.
            val displayLarge = TextStyle(fontSize = 57.sp, lineHeight = 64.sp, fontWeight = FontWeight.Normal)
            val displayMedium = TextStyle(fontSize = 45.sp, lineHeight = 52.sp, fontWeight = FontWeight.Normal)
            val displaySmall = TextStyle(fontSize = 36.sp, lineHeight = 44.sp, fontWeight = FontWeight.Normal)
            val headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Normal)
            val headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.Normal)
            val headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Normal)
            val titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Normal)
            val titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium)
            val titleSmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)
            val bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal)
            val bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal)
            val bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal)
            val labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)
            val labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
            val labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)

            return AppTypography(
                // Original 4-slot scale — kept distinct so library components that
                // already reference them don't shift.
                display = TextStyle(fontSize = 40.sp, lineHeight = 48.sp, fontWeight = FontWeight.Bold),
                title = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.SemiBold),
                body = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
                label = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
                // M3 standard scale
                displayLarge = displayLarge,
                displayMedium = displayMedium,
                displaySmall = displaySmall,
                headlineLarge = headlineLarge,
                headlineMedium = headlineMedium,
                headlineSmall = headlineSmall,
                titleLarge = titleLarge,
                titleMedium = titleMedium,
                titleSmall = titleSmall,
                bodyLarge = bodyLarge,
                bodyMedium = bodyMedium,
                bodySmall = bodySmall,
                labelLarge = labelLarge,
                labelMedium = labelMedium,
                labelSmall = labelSmall,
                // Custom extensions
                subtitle = TextStyle(fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium),
                bodyMonospaced = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontFamily = FontFamily.Monospace),
                captionMonospaced =
                    TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                    ),
                displayRounded = TextStyle(fontSize = 44.sp, lineHeight = 52.sp, fontWeight = FontWeight.Bold),
                iconSmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
                iconMedium = TextStyle(fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Normal),
                iconLarge = TextStyle(fontSize = 44.sp, lineHeight = 52.sp, fontWeight = FontWeight.Normal),
                iconExtraLarge = TextStyle(fontSize = 50.sp, lineHeight = 56.sp, fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

internal fun AppTypography.toMaterialTypography(): Typography =
    Typography(
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        displaySmall = displaySmall,
        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,
        labelLarge = labelLarge,
        labelMedium = labelMedium,
        labelSmall = labelSmall,
    )
