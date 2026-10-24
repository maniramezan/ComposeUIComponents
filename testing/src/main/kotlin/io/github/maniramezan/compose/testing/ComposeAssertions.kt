package io.github.maniramezan.compose.testing

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.testcommons.compose.MinimumTouchTargetSize as TestCommonsMinimumTouchTargetSize
import io.github.maniramezan.testcommons.compose.assertMinimumTouchTarget as testCommonsAssertMinimumTouchTarget
import io.github.maniramezan.testcommons.compose.onNodeWithRequiredContentDescription as testCommonsOnNodeWithRequiredContentDescription

/**
 * The minimum touch target from the Material accessibility guidelines.
 *
 * Kept for binary compatibility; the helper now lives in KotlinTestCommons.
 */
@Deprecated(
    message = "Moved to KotlinTestCommons; use io.github.maniramezan.testcommons.compose.MinimumTouchTargetSize.",
    replaceWith =
        ReplaceWith(
            expression = "MinimumTouchTargetSize",
            imports = ["io.github.maniramezan.testcommons.compose.MinimumTouchTargetSize"],
        ),
)
public val MinimumTouchTargetSize: Dp = TestCommonsMinimumTouchTargetSize

/**
 * Asserts the node is at least [minSize] wide and tall.
 *
 * Kept for binary compatibility; delegates to KotlinTestCommons.
 */
@Deprecated(
    message = "Moved to KotlinTestCommons; use io.github.maniramezan.testcommons.compose.assertMinimumTouchTarget.",
    replaceWith =
        ReplaceWith(
            expression = "assertMinimumTouchTarget(minSize)",
            imports = ["io.github.maniramezan.testcommons.compose.assertMinimumTouchTarget"],
        ),
)
public fun SemanticsNodeInteraction.assertMinimumTouchTarget(minSize: Dp = TestCommonsMinimumTouchTargetSize): SemanticsNodeInteraction =
    testCommonsAssertMinimumTouchTarget(minSize)

/**
 * Finds the node whose content description exactly equals [label].
 *
 * Kept for binary compatibility; delegates to KotlinTestCommons.
 */
@Deprecated(
    message =
        "Moved to KotlinTestCommons; use " +
            "io.github.maniramezan.testcommons.compose.onNodeWithRequiredContentDescription.",
    replaceWith =
        ReplaceWith(
            expression = "onNodeWithRequiredContentDescription(label, useUnmergedTree)",
            imports = ["io.github.maniramezan.testcommons.compose.onNodeWithRequiredContentDescription"],
        ),
)
public fun SemanticsNodeInteractionsProvider.onNodeWithRequiredContentDescription(
    label: String,
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteraction = testCommonsOnNodeWithRequiredContentDescription(label, useUnmergedTree)
