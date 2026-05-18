package io.github.maniramezan.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
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

@Composable
public fun AppTheme(
    colors: AppColors = AppColors.light(),
    spacing: AppSpacing = AppSpacing.default(),
    motion: AppMotion = AppMotion.default(),
    icons: AppIcons = AppIcons.default(),
    typography: AppTypography = AppTypography.default(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        appColorScheme(
            colors = colors,
            darkTheme = darkTheme,
            dynamicColor = dynamicColor,
        )
    val appColors = colorScheme.toAppColors()

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalAppSpacing provides spacing,
        LocalAppMotion provides motion,
        LocalAppIcons provides icons,
        LocalAppTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography.toMaterialTypography(),
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
}

@Composable
private fun appColorScheme(
    colors: AppColors,
    darkTheme: Boolean,
    dynamicColor: Boolean,
): ColorScheme {
    val context = LocalContext.current
    val resolvedColors = if (darkTheme && colors == AppColors.light()) AppColors.dark() else colors
    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> resolvedColors.toDarkMaterialColorScheme()
        else -> resolvedColors.toLightMaterialColorScheme()
    }
}

private fun AppColors.toLightMaterialColorScheme(): ColorScheme =
    lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
    )

private fun ColorScheme.toAppColors(): AppColors =
    AppColors(
        primary = primary,
        onPrimary = onPrimary,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
    )

internal fun AppColors.toDarkMaterialColorScheme(): ColorScheme =
    darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
    )
