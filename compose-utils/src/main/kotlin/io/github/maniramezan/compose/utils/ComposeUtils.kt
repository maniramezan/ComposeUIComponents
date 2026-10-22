package io.github.maniramezan.compose.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp

/**
 * Ensures this node is at least [size] wide and tall without shrinking larger content, so a
 * small visual (an icon, a dot) still gets an accessible tap target. Use 48dp for interactive
 * controls; see `docs/accessibility.md`.
 */
public fun Modifier.minimumTouchTarget(size: Dp): Modifier = defaultMinSize(minWidth = size, minHeight = size)

/** Height-only variant of [minimumTouchTarget], for full-width rows whose width is already large. */
public fun Modifier.minimumTouchTargetHeight(height: Dp): Modifier = defaultMinSize(minHeight = height)

/** Width-only variant of [minimumTouchTarget], for tall, narrow controls. */
public fun Modifier.minimumTouchTargetWidth(width: Dp): Modifier = defaultMinSize(minWidth = width)

/**
 * Enforces a minimum [minimumTouchTarget] and marks the node with [Role.Button]
 * semantics in a single modifier chain.
 *
 * This is the shared interaction surface for single-action controls (links,
 * pills, tappable rows, buttons). Applying the touch target and role together
 * keeps every interactive component at the accessibility-minimum tap target
 * with consistent button semantics.
 */
public fun Modifier.buttonRole(
    onClick: () -> Unit,
    minimumTouchTarget: Dp,
): Modifier = minimumTouchTarget(minimumTouchTarget).clickable(role = Role.Button, onClick = onClick)

/**
 * Enforces a minimum [minimumTouchTarget] and marks the node with button
 * selection semantics as a single-choice option.
 *
 * Mirrors [buttonRole] for selection semantics (e.g. filter chips and choice rows).
 */
public fun Modifier.selectableRole(
    selected: Boolean,
    onClick: () -> Unit,
    minimumTouchTarget: Dp,
): Modifier = minimumTouchTarget(minimumTouchTarget).selectable(selected = selected, role = Role.Button, onClick = onClick)
