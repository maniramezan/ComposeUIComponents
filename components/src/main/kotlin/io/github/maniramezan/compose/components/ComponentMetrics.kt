package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme

/**
 * Material's standard opacity for disabled content (text and icons). Shared so every
 * component dims disabled content identically instead of re-inlining the literal.
 */
internal const val DISABLED_CONTENT_ALPHA: Float = 0.38f

@Composable
@ReadOnlyComposable
internal fun minimumTouchTargetSize(): Dp = AppTheme.spacing.x6

@Composable
@ReadOnlyComposable
internal fun standardIconSize(): Dp = AppTheme.spacing.xl

@Composable
@ReadOnlyComposable
internal fun containerCornerRadius(): Dp = AppTheme.spacing.md
