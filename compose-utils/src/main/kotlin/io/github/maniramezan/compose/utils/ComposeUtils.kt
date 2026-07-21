package io.github.maniramezan.compose.utils

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

public fun Modifier.minimumTouchTarget(size: Dp): Modifier = defaultMinSize(minWidth = size, minHeight = size)

public fun Modifier.minimumTouchTargetHeight(height: Dp): Modifier = defaultMinSize(minHeight = height)

public fun Modifier.minimumTouchTargetWidth(width: Dp): Modifier = defaultMinSize(minWidth = width)
