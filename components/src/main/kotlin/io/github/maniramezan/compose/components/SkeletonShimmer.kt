package io.github.maniramezan.compose.components

import android.provider.Settings
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import io.github.maniramezan.compose.theme.AppTheme

/** Length of one shimmer cycle: one sweep plus the rest before the next. */
private const val SHIMMER_PERIOD_MILLIS = 1_800L

/** Fraction of the cycle spent sweeping; the remainder the band rests off-screen. */
private const val SHIMMER_SWEEP_FRACTION = 0.65f

/** Band width as a fraction of the shimmered area's width. */
private const val SHIMMER_BAND_WIDTH_FRACTION = 0.45f

/** Peak alpha of the highlight at the centre of the band. */
private const val SHIMMER_PEAK_ALPHA = 0.10f

/**
 * Force-disables [skeletonShimmer] for the subtree below.
 *
 * The modifier is already static under the system reduce-motion / animator-duration-scale
 * setting and in `@Preview` inspection; this local is the explicit escape hatch for
 * deterministic screenshot tests and demo captures that run with animations otherwise on.
 */
public val LocalSkeletonShimmerEnabled: ProvidableCompositionLocal<Boolean> =
    staticCompositionLocalOf { true }

/**
 * Sweeps a soft highlight band across the loading placeholders in this subtree so a
 * skeleton reads as in-progress rather than frozen.
 *
 * Apply it **once at the root of a skeleton layout**, not per placeholder block. The band's
 * phase is a pure function of the animation frame clock ([skeletonShimmerSweep]), not
 * per-node animation state, so every subtree that uses this modifier — separate cards,
 * separate lists — sweeps in unison as one band crossing the screen, regardless of when
 * each entered composition.
 *
 * The highlight is [highlight] (defaulting to [AppColors.shimmerHighlight]) painted at a low
 * alpha and composited [BlendMode.SrcAtop] against an offscreen layer, so it lands only on
 * the placeholder silhouette and never tints the gaps between blocks.
 *
 * The sweep is **static** — the modifier is an inert pass-through — when the system
 * reduce-motion / animator-duration-scale setting is off, when [LocalSkeletonShimmerEnabled]
 * is `false`, or under `@Preview` inspection. It adds no pointer input and no semantics, so
 * it never hit-tests and stays out of the accessibility tree.
 *
 * @param highlight the swept colour; defaults to the theme's skeleton highlight role.
 */
@Composable
public fun Modifier.skeletonShimmer(highlight: Color = AppTheme.colors.shimmerHighlight): Modifier {
    val context = LocalContext.current
    val inInspection = LocalInspectionMode.current
    val enabledByLocal = LocalSkeletonShimmerEnabled.current
    val reduceMotion =
        remember(context) {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            ) == 0f
        }
    val animate = enabledByLocal && !inInspection && !reduceMotion

    // Call produceState unconditionally so the composable structure is stable even if
    // `animate` flips (e.g. LocalSkeletonShimmerEnabled toggled by a caller).
    val sweep by produceState(initialValue = 0f, animate) {
        if (!animate) {
            value = 0f
            return@produceState
        }
        while (true) {
            withInfiniteAnimationFrameMillis { frameMillis ->
                value = skeletonShimmerSweep(frameMillis)
            }
        }
    }

    if (!animate) return this

    return this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()
            val bandWidth = size.width * SHIMMER_BAND_WIDTH_FRACTION
            val bandStart = -bandWidth + (size.width + 2f * bandWidth) * sweep
            drawRect(
                brush =
                    Brush.linearGradient(
                        0f to highlight.copy(alpha = 0f),
                        0.5f to highlight.copy(alpha = SHIMMER_PEAK_ALPHA),
                        1f to highlight.copy(alpha = 0f),
                        start = Offset(bandStart, 0f),
                        end = Offset(bandStart + bandWidth, 0f),
                    ),
                blendMode = BlendMode.SrcAtop,
            )
        }
}

/**
 * Band position for [skeletonShimmer] at [frameMillis], in `0f..1f`, where `0f` is the band
 * entering at the leading edge and `1f` is it parked just past the trailing edge.
 *
 * The value ramps `0f -> 1f` over the first [SHIMMER_SWEEP_FRACTION] of each
 * [SHIMMER_PERIOD_MILLIS] cycle and then holds at `1f` for the remainder — that rest between
 * sweeps is part of the effect. It is periodic with [SHIMMER_PERIOD_MILLIS], so the phase is
 * identical across cycles for a phase-locked shimmer.
 */
internal fun skeletonShimmerSweep(frameMillis: Long): Float {
    val progress = (frameMillis.mod(SHIMMER_PERIOD_MILLIS)).toFloat() / SHIMMER_PERIOD_MILLIS
    return (progress / SHIMMER_SWEEP_FRACTION).coerceAtMost(1f)
}
