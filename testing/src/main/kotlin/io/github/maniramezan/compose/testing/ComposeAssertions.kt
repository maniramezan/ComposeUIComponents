package io.github.maniramezan.compose.testing

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public val MinimumTouchTargetSize: Dp = 48.dp

public fun SemanticsNodeInteraction.assertMinimumTouchTarget(minSize: Dp = MinimumTouchTargetSize): SemanticsNodeInteraction =
    assertWidthIsAtLeast(minSize)
        .assertHeightIsAtLeast(minSize)

public fun SemanticsNodeInteractionsProvider.onNodeWithRequiredContentDescription(
    label: String,
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteraction =
    onNodeWithContentDescription(
        label = label,
        substring = false,
        ignoreCase = false,
        useUnmergedTree = useUnmergedTree,
    )
