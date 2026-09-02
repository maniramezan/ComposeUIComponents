package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.maniramezan.compose.tokens.AppReferenceColors

/**
 * Semantic color slots for an app theme. Apps supply their own light + dark
 * instances of [AppColors] to [AppTheme]; the library ships sensible defaults
 * so consumers can get a usable theme out of the box.
 *
 * Slots are organized into four groups:
 *  - **Core / Material 3 standard** — the M3 ColorScheme slots most components
 *    read from. New components should prefer these.
 *  - **Status** — semantic success / warning / error colors.
 *  - **Overlay** — colors for media-overlay and scrim use cases.
 *  - **Levels** — a [LevelPalette] for tiered-badge components.
 *  - **Skeleton** — the highlight swept across loading placeholders.
 */
@Immutable
public data class AppColors(
    // ===== Core (Material 3 standard) =====
    public val primary: Color,
    public val onPrimary: Color,
    public val primaryContainer: Color,
    public val onPrimaryContainer: Color,
    public val secondary: Color,
    public val onSecondary: Color,
    public val background: Color,
    public val onBackground: Color,
    public val surface: Color,
    public val onSurface: Color,
    public val surfaceVariant: Color,
    public val onSurfaceVariant: Color,
    public val surfaceContainer: Color,
    public val outline: Color,
    public val outlineVariant: Color,
    public val error: Color,
    public val onError: Color,
    public val scrim: Color,
    // ===== Status (extensions) =====
    public val success: Color,
    public val onSuccess: Color,
    public val warning: Color,
    public val onWarning: Color,
    // ===== Overlay (extensions) =====
    public val overlayHeavy: Color,
    public val overlayMedium: Color,
    public val overlaySubtle: Color,
    public val onOverlay: Color,
    public val overlayShadowStart: Color,
    public val overlayShadowEnd: Color,
    public val overlayBottomTint: Color,
    // ===== Tiered badge palette (extension) =====
    public val levels: LevelPalette,
    /**
     * Skeleton highlight (extension) — the colour swept across skeleton placeholders by
     * `Modifier.skeletonShimmer`.
     *
     * Defaults to [onSurface]. The shimmer paints it at a low alpha and masks it to the
     * placeholder silhouette, so it needs to be the theme's "ink" color — dark on a light
     * theme, light on a dark theme — for the sweep to read against the neutral
     * [surfaceContainer] / [surfaceVariant] fills the placeholders use. [onSurface] is that
     * color by construction, which is why it is the default; a theme whose skeletons sit on
     * an unusual surface (e.g. an always-dark media area) should override it.
     */
    public val shimmerHighlight: Color = onSurface,
) {
    public companion object {
        public fun light(): AppColors =
            AppColors(
                // Core
                primary = AppReferenceColors.Blue40,
                onPrimary = AppReferenceColors.White,
                primaryContainer = Color(0xFFD2E4FF),
                onPrimaryContainer = Color(0xFF001D36),
                secondary = Color(0xFF535F70),
                onSecondary = AppReferenceColors.White,
                background = AppReferenceColors.Neutral99,
                onBackground = AppReferenceColors.Neutral10,
                surface = AppReferenceColors.Neutral99,
                onSurface = AppReferenceColors.Neutral10,
                surfaceVariant = AppReferenceColors.Neutral90,
                onSurfaceVariant = Color(0xFF43474E),
                surfaceContainer = Color(0xFFF2F2F7),
                outline = Color(0xFF73777F),
                outlineVariant = Color(0xFFC3C7CF),
                error = Color(0xFFBA1A1A),
                onError = AppReferenceColors.White,
                scrim = Color(0xFF000000),
                // Status
                success = Color(0xFF2E7D32),
                onSuccess = AppReferenceColors.White,
                warning = Color(0xFFE08534),
                onWarning = AppReferenceColors.White,
                // Overlay
                overlayHeavy = Color(0xBF000000), // 75% black
                overlayMedium = Color(0x99000000), // 60% black
                overlaySubtle = Color(0x1A000000), // 10% black
                onOverlay = AppReferenceColors.White,
                overlayShadowStart = Color(0x24000000), // 14% black
                overlayShadowEnd = Color(0x0D000000), // 5% black
                overlayBottomTint = Color(0x38000000), // 22% black
                // Levels
                levels = LevelPalette.empty(),
            )

        public fun dark(): AppColors =
            AppColors(
                // Core
                primary = AppReferenceColors.Blue80,
                onPrimary = AppReferenceColors.Neutral10,
                primaryContainer = Color(0xFF00497D),
                onPrimaryContainer = Color(0xFFD2E4FF),
                secondary = Color(0xFFBBC7DB),
                onSecondary = Color(0xFF253140),
                background = AppReferenceColors.Neutral10,
                onBackground = AppReferenceColors.Neutral90,
                surface = AppReferenceColors.Neutral10,
                onSurface = AppReferenceColors.Neutral90,
                surfaceVariant = AppReferenceColors.Neutral20,
                onSurfaceVariant = Color(0xFFC3C7CF),
                surfaceContainer = Color(0xFF1C1C1E),
                outline = Color(0xFF8D9199),
                outlineVariant = Color(0xFF43474E),
                error = Color(0xFFFFB4AB),
                onError = Color(0xFF690005),
                scrim = Color(0xFF000000),
                // Status
                success = Color(0xFFA5D6A7),
                onSuccess = Color(0xFF003910),
                warning = Color(0xFFFFCA8A),
                onWarning = Color(0xFF4A2700),
                // Overlay
                overlayHeavy = Color(0xBF000000),
                overlayMedium = Color(0x99000000),
                overlaySubtle = Color(0x1A000000),
                onOverlay = AppReferenceColors.White,
                overlayShadowStart = Color(0x24000000),
                overlayShadowEnd = Color(0x0D000000),
                overlayBottomTint = Color(0x38000000),
                // Levels
                levels = LevelPalette.empty(),
            )
    }
}
