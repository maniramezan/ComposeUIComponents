package io.github.maniramezan.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val LocalAppColors = staticCompositionLocalOf { AppColors.light() }
private val LocalAppSpacing = staticCompositionLocalOf { AppSpacing.default() }
private val LocalAppStroke = staticCompositionLocalOf { AppStroke() }
private val LocalAppMotion = staticCompositionLocalOf { AppMotion.default() }
private val LocalAppIcons = staticCompositionLocalOf { AppIcons.default() }
private val LocalAppTypography = staticCompositionLocalOf { AppTypography.default() }
private val LocalAppShapes = staticCompositionLocalOf { AppShapes.default() }

/**
 * Provides the design system's semantic tokens to [content] and bridges them into
 * [MaterialTheme], so both `AppTheme.*` and stock Material 3 components pick up the
 * same colors, typography, and shapes.
 *
 * @param lightColors Colors used when [darkTheme] is `false`.
 * @param darkColors Colors used when [darkTheme] is `true`.
 * @param spacing Spacing and layout tokens. Its legacy stroke values supply the stroke widths.
 * @param motion Duration and easing tokens.
 * @param icons Icon contracts; pass `defaultAppIcons()` from `:icons` for the curated set.
 * @param typography Typography scale, including weight-variant slots.
 * @param shapes Corner-shape tokens.
 * @param darkTheme Whether to resolve [darkColors]; follows the system setting by default.
 * @param dynamicColor Whether to use the platform's wallpaper-derived color scheme on
 *   Android 12+; the non-Material [AppColors] roles still come from the token bundle.
 */
@Composable
public fun AppTheme(
    lightColors: AppColors = AppColors.light(),
    darkColors: AppColors = AppColors.dark(),
    spacing: AppSpacing = AppSpacing.default(),
    motion: AppMotion = AppMotion.default(),
    icons: AppIcons = AppIcons.default(),
    typography: AppTypography = AppTypography.default(),
    shapes: AppShapes = AppShapes.default(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(
        stroke = AppStroke(thin = spacing.strokeThin, regular = spacing.strokeRegular, thick = spacing.strokeThick),
        lightColors = lightColors,
        darkColors = darkColors,
        spacing = spacing,
        motion = motion,
        icons = icons,
        typography = typography,
        shapes = shapes,
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content,
    )
}

/**
 * Provides semantic tokens with a dedicated [stroke] scale. Its widths also resolve
 * through the legacy stroke properties read by older components.
 *
 * @param stroke Stroke widths supplied to the hierarchy.
 * @param lightColors Colors used when [darkTheme] is `false`.
 * @param darkColors Colors used when [darkTheme] is `true`.
 * @param spacing Spacing and layout-dimension tokens.
 * @param motion Duration and easing tokens.
 * @param icons Icon contracts.
 * @param typography Typography scale, including weight-variant slots.
 * @param shapes Corner-shape tokens.
 * @param darkTheme Whether to resolve [darkColors]; follows the system setting by default.
 * @param dynamicColor Whether to use the platform's wallpaper-derived color scheme on Android 12+.
 * @param content The themed hierarchy.
 */
@Composable
public fun AppTheme(
    stroke: AppStroke,
    lightColors: AppColors = AppColors.light(),
    darkColors: AppColors = AppColors.dark(),
    spacing: AppSpacing = AppSpacing.default(),
    motion: AppMotion = AppMotion.default(),
    icons: AppIcons = AppIcons.default(),
    typography: AppTypography = AppTypography.default(),
    shapes: AppShapes = AppShapes.default(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val resolvedColors = if (darkTheme) darkColors else lightColors
    val colorScheme =
        appColorScheme(
            colors = resolvedColors,
            darkTheme = darkTheme,
            dynamicColor = dynamicColor,
        )
    val appColors = remember(colorScheme, resolvedColors) { colorScheme.toAppColors(base = resolvedColors) }
    val materialTypography = remember(typography) { typography.toMaterialTypography() }
    val materialShapes = remember(shapes) { shapes.toMaterialShapes() }
    val resolvedSpacing =
        remember(spacing, stroke) {
            spacing.copy(strokeThin = stroke.thin, strokeRegular = stroke.regular, strokeThick = stroke.thick)
        }

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalAppSpacing provides resolvedSpacing,
        LocalAppStroke provides stroke,
        LocalAppMotion provides motion,
        LocalAppIcons provides icons,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content,
        )
    }
}

/** Read access to the tokens provided by the nearest enclosing [AppTheme] composable. */
public object AppTheme {
    public val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    public val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalAppSpacing.current

    /** Stroke widths for the current theme. */
    public val stroke: AppStroke
        @Composable
        @ReadOnlyComposable
        get() = LocalAppStroke.current

    public val motion: AppMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalAppMotion.current

    public val icons: AppIcons
        @Composable
        @ReadOnlyComposable
        get() = LocalAppIcons.current

    public val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    public val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current
}

private fun AppShapes.toMaterialShapes(): Shapes =
    Shapes(
        extraSmall = badge,
        small = small,
        medium = standard,
        large = large,
        extraLarge = large,
    )

@Composable
private fun appColorScheme(
    colors: AppColors,
    darkTheme: Boolean,
    dynamicColor: Boolean,
): ColorScheme {
    val context = LocalContext.current
    if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        return if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    return remember(colors, darkTheme) {
        if (darkTheme) colors.toDarkMaterialColorScheme() else colors.toLightMaterialColorScheme()
    }
}

private fun AppColors.toLightMaterialColorScheme(): ColorScheme =
    lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceContainer = surfaceContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
        scrim = scrim,
    )

internal fun AppColors.toDarkMaterialColorScheme(): ColorScheme =
    darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceContainer = surfaceContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
        scrim = scrim,
    )

private fun ColorScheme.toAppColors(base: AppColors): AppColors =
    base.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceContainer = surfaceContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
        scrim = scrim,
    )
