package io.github.maniramezan.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme

/**
 * The axis a [FlipCard] rotates around.
 */
public enum class FlipAxis {
    /** Rotate around the vertical (Y) axis — like turning a page. */
    Horizontal,

    /** Rotate around the horizontal (X) axis — flips top over bottom. */
    Vertical,
}

/**
 * A two-sided card that animates a 3D flip between a [front] and [back] face,
 * suitable for flash-card style UIs.
 *
 * The component supports both **controlled** and **uncontrolled** usage:
 * - Pass [flipped] (and usually [onFlippedChange]) to drive the state yourself.
 * - Leave [flipped] as `null` to let the card manage its own flip state; taps
 *   then toggle between faces and [onFlippedChange] is still notified.
 *
 * Only the currently visible face is composed; the faces are swapped at the
 * edge-on midpoint of the rotation, which keeps the accessibility tree clean and
 * the back face readable (not mirrored).
 *
 * @param front Content for the front face.
 * @param back Content for the back face.
 * @param modifier Modifier applied to the card container.
 * @param flipped When non-null the card is controlled and shows the back face
 *   while `true`; when `null` the card is uncontrolled.
 * @param onFlippedChange Invoked with the requested face state when the user taps
 *   the card. Required for tap-to-flip in controlled mode; optional otherwise.
 * @param axis The [FlipAxis] the card rotates around.
 * @param enabled Whether tapping flips the card. Set `false` for display-only or
 *   fully externally driven flips.
 * @param shape Shape of the card container and clip.
 * @param containerColor Background color of each face.
 * @param frontStateDescription Caller-supplied TalkBack state description used
 *   while the front face is shown. Pair with [backStateDescription] to announce
 *   the flip to assistive technologies.
 * @param backStateDescription Caller-supplied TalkBack state description used
 *   while the back face is shown.
 * @param onClickLabel Caller-supplied accessibility label describing the flip
 *   action (e.g. "Flip card").
 */
@Composable
public fun FlipCard(
    front: @Composable BoxScope.() -> Unit,
    back: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    flipped: Boolean? = null,
    onFlippedChange: ((Boolean) -> Unit)? = null,
    axis: FlipAxis = FlipAxis.Horizontal,
    enabled: Boolean = true,
    shape: Shape = AppTheme.shapes.large,
    containerColor: Color = AppTheme.colors.surface,
    frontStateDescription: String? = null,
    backStateDescription: String? = null,
    onClickLabel: String? = null,
) {
    var internalFlipped by rememberSaveable { mutableStateOf(false) }
    val isFlipped = flipped ?: internalFlipped

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec =
            tween(
                durationMillis = AppTheme.motion.mediumMillis,
                easing = AppTheme.motion.emphasizedEasing,
            ),
        label = "flipCardRotation",
    )

    val density = LocalDensity.current.density
    val showingFront = rotation <= 90f
    val stateDesc = if (isFlipped) backStateDescription else frontStateDescription

    val toggleModifier =
        if (enabled) {
            Modifier.toggleable(
                value = isFlipped,
                role = Role.Switch,
                onValueChange = { requested ->
                    if (flipped == null) internalFlipped = requested
                    onFlippedChange?.invoke(requested)
                },
            )
        } else {
            Modifier
        }

    Box(
        modifier =
            modifier
                .clip(shape)
                .background(containerColor)
                .then(toggleModifier)
                .graphicsLayer {
                    when (axis) {
                        FlipAxis.Horizontal -> rotationY = rotation
                        FlipAxis.Vertical -> rotationX = rotation
                    }
                    cameraDistance = CAMERA_DISTANCE_MULTIPLIER * density
                }.semantics {
                    if (stateDesc != null) stateDescription = stateDesc
                    if (enabled && onClickLabel != null) onClick(label = onClickLabel, action = null)
                },
    ) {
        if (showingFront) {
            front()
        } else {
            // Counter-rotate the back face so its content is not mirrored.
            Box(
                modifier =
                    Modifier.graphicsLayer {
                        when (axis) {
                            FlipAxis.Horizontal -> rotationY = 180f
                            FlipAxis.Vertical -> rotationX = 180f
                        }
                    },
                content = back,
            )
        }
    }
}

/**
 * Multiplier applied to display density for [graphicsLayer]'s camera distance.
 * Smaller values deepen the perspective so the rotation reads as a 3D flip
 * (and the [FlipAxis] is clearly distinguishable) rather than a flat squish.
 */
private const val CAMERA_DISTANCE_MULTIPLIER = 8f
