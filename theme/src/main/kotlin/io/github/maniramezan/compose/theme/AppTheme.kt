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
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val LocalAppColors = staticCompositionLocalOf { AppColors.light() }
private val LocalAppSpacing = staticCompositionLocalOf { AppSpacing.default() }
private val LocalAppMotion = staticCompositionLocalOf { AppMotion.default() }
private val LocalAppIcons = staticCompositionLocalOf { AppIcons.default() }
private val LocalAppTypography = staticCompositionLocalOf { AppTypography.default() }
private val LocalAppShapes = staticCompositionLocalOf { AppShapes.default() }

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
    val resolvedColors = if (darkTheme) darkColors else lightColors
    val colorScheme =
        appColorScheme(
            colors = resolvedColors,
            darkTheme = darkTheme,
            dynamicColor = dynamicColor,
        )
    val appColors = colorScheme.toAppColors(base = resolvedColors)

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalAppSpacing provides spacing,
        LocalAppMotion provides motion,
        LocalAppIcons provides icons,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography.toMaterialTypography(),
            shapes = shapes.toMaterialShapes(),
            content = content,
        )
    }
}

public object AppTheme {
    public val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    public val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalAppSpacing.current

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
    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> colors.toDarkMaterialColorScheme()
        else -> colors.toLightMaterialColorScheme()
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
