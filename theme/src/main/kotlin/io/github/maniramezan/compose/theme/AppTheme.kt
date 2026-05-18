package io.github.maniramezan.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalAppColors = staticCompositionLocalOf { AppColors.light() }
private val LocalAppSpacing = staticCompositionLocalOf { AppSpacing.default() }
private val LocalAppMotion = staticCompositionLocalOf { AppMotion.default() }
private val LocalAppIcons = staticCompositionLocalOf { AppIcons.default() }

@Composable
public fun AppTheme(
    colors: AppColors = AppColors.light(),
    spacing: AppSpacing = AppSpacing.default(),
    motion: AppMotion = AppMotion.default(),
    icons: AppIcons = AppIcons.default(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppSpacing provides spacing,
        LocalAppMotion provides motion,
        LocalAppIcons provides icons,
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(dynamicColor),
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
}

private fun AppColors.toMaterialColorScheme(dynamicColor: Boolean): ColorScheme {
    val selectedColors = if (dynamicColor) AppColors.light() else this
    return lightColorScheme(
        primary = selectedColors.primary,
        onPrimary = selectedColors.onPrimary,
        surface = selectedColors.surface,
        onSurface = selectedColors.onSurface,
        surfaceVariant = selectedColors.surfaceVariant,
    )
}

internal fun AppColors.toDarkMaterialColorScheme(): ColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
)
