package io.github.maniramezan.compose.components

import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget

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
 * Both faces are composed continuously, each on its own rotated graphics layer,
 * so the card reads as a single solid surface turning through edge-on rather than
 * one face blinking out and the other blinking in. The face turned away from the
 * viewer is culled (alpha `0`) and removed from the accessibility tree, and the
 * back face is counter-rotated so its content is never mirrored.
 *
 * @param front Content for the front face.
 * @param back Content for the back face.
 * @param modifier Modifier applied to the card container.
 * @param flipped When non-null the card is controlled and shows the back face
 *   while `true`; when `null` the card is uncontrolled.
 * @param onFlippedChange Invoked with the requested face state when the user taps
 *   the card. Required for tap-to-flip in controlled mode; optional otherwise.
 * @param axis The [FlipAxis] the card rotates around.
 * @param animationSpec Animation driving the flip. Defaults to a token-driven
 *   [tween] using [AppTheme]'s medium duration and emphasized easing; pass any
 *   [AnimationSpec] (e.g. a faster tween or a spring) to customize the motion.
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
    animationSpec: AnimationSpec<Float> =
        tween(
            durationMillis = AppTheme.motion.mediumMillis,
            easing = AppTheme.motion.emphasizedEasing,
        ),
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
        animationSpec = animationSpec,
        label = "flipCardRotation",
    )

    val density = LocalDensity.current.density
    // The front faces the viewer for the first half of the rotation, the back for
    // the second half. At the 90° crossover both faces are edge-on (and invisible),
    // so toggling the alpha there is imperceptible.
    val frontVisible = rotation <= 90f
    val stateDesc = if (isFlipped) backStateDescription else frontStateDescription

    val toggleModifier =
        if (enabled) {
            Modifier
                .minimumTouchTarget(minimumTouchTargetSize())
                .toggleable(
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
                .then(toggleModifier)
                .semantics {
                    if (stateDesc != null) stateDescription = stateDesc
                    if (enabled && onClickLabel != null) onClick(label = onClickLabel, action = null)
                },
    ) {
        // Front face: rotates 0°→180° with the flip; culled once it turns away.
        FlipFace(
            rotation = rotation,
            axis = axis,
            density = density,
            visible = frontVisible,
            shape = shape,
            containerColor = containerColor,
            content = front,
        )
        // Back face: counter-rotated by 180° so it faces the viewer (un-mirrored)
        // exactly when the front has turned away.
        FlipFace(
            rotation = rotation - 180f,
            axis = axis,
            density = density,
            visible = !frontVisible,
            shape = shape,
            containerColor = containerColor,
            content = back,
        )
    }
}

/**
 * One rotated face of a [FlipCard]. Fills the card bounds, draws the themed
 * surface, and culls itself (alpha `0` + cleared semantics) when [visible] is
 * `false` so the away-facing side is neither seen nor announced.
 */
@Composable
private fun BoxScope.FlipFace(
    rotation: Float,
    axis: FlipAxis,
    density: Float,
    visible: Boolean,
    shape: Shape,
    containerColor: Color,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            Modifier
                .matchParentSize()
                .graphicsLayer {
                    when (axis) {
                        FlipAxis.Horizontal -> rotationY = rotation
                        FlipAxis.Vertical -> rotationX = rotation
                    }
                    cameraDistance = CAMERA_DISTANCE_MULTIPLIER * density
                    alpha = if (visible) 1f else 0f
                }.clip(shape)
                .background(containerColor)
                .then(if (visible) Modifier else Modifier.clearAndSetSemantics {}),
        content = content,
    )
}

/**
 * Multiplier applied to display density for [graphicsLayer]'s camera distance.
 * Smaller values deepen the perspective so the rotation reads as a 3D flip
 * (and the [FlipAxis] is clearly distinguishable) rather than a flat squish.
 */
private const val CAMERA_DISTANCE_MULTIPLIER = 8f
