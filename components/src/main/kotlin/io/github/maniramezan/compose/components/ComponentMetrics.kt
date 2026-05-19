package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme

@Composable
@ReadOnlyComposable
internal fun minimumTouchTargetSize(): Dp = AppTheme.spacing.xl * 2f

@Composable
@ReadOnlyComposable
internal fun standardIconSize(): Dp = AppTheme.spacing.xl

@Composable
@ReadOnlyComposable
internal fun containerCornerRadius(): Dp = AppTheme.spacing.md
