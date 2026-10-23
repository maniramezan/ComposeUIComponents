package io.github.maniramezan.compose.components

import androidx.compose.animation.core.animate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

/** Collapsed fraction at or above which a settling bar snaps hidden rather than shown. */
private const val SETTLE_HIDE_THRESHOLD = 0.5f

/**
 * Hoisted state that lets a [TabBar] hide itself as the caller's scrollable content scrolls
 * down, and reappear as it scrolls up — the same pattern used by Material 3's
 * `TopAppBarScrollBehavior`, applied to a bottom bar.
 *
 * Attach [nestedScrollConnection] to the scrollable content via `Modifier.nestedScroll(...)`,
 * and pass this instance to [TabBar]'s `scrollBehavior` parameter. Obtain an instance via
 * [rememberTabBarScrollBehavior].
 *
 * When a scroll gesture or fling ends with the bar partially hidden, it settles to whichever
 * of fully shown or fully hidden is closer, so it never rests half-visible.
 */
@Stable
public class TabBarScrollBehavior internal constructor() {
    /**
     * The bar's current vertical translation in pixels. `0f` means fully shown; negative
     * values mean the bar is translated downward (hidden) by that many pixels.
     */
    public var heightOffset: Float by mutableFloatStateOf(0f)
        internal set

    /**
     * The most negative value [heightOffset] may reach — i.e. the bar's own measured height in
     * pixels, negated. Kept in sync automatically by [TabBar] as it measures itself.
     */
    public var heightOffsetLimit: Float = 0f
        internal set

    /** `0f` when the bar is fully shown, `1f` when it is fully hidden. */
    public val collapsedFraction: Float
        get() = if (heightOffsetLimit == 0f) 0f else (heightOffset / heightOffsetLimit).coerceIn(0f, 1f)

    internal fun updateHeightOffsetLimit(limit: Float) {
        heightOffsetLimit = limit
        heightOffset = heightOffset.coerceIn(limit, 0f)
    }

    /** Wire this to the scrollable content that should drive the bar's visibility. */
    public val nestedScrollConnection: NestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val limit = heightOffsetLimit
                if (limit == 0f) return Offset.Zero
                heightOffset = (heightOffset + consumed.y).coerceIn(limit, 0f)
                return Offset.Zero
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity,
            ): Velocity {
                settle()
                return Velocity.Zero
            }
        }

    /** Animates a partially hidden bar to the nearer of fully shown or fully hidden. */
    private suspend fun settle() {
        val limit = heightOffsetLimit
        if (limit == 0f || heightOffset == 0f || heightOffset == limit) return
        val target = if (collapsedFraction >= SETTLE_HIDE_THRESHOLD) limit else 0f
        animate(initialValue = heightOffset, targetValue = target) { value, _ ->
            heightOffset = value.coerceIn(limit, 0f)
        }
    }
}

/** Creates and remembers a [TabBarScrollBehavior] for use with [TabBar]. */
@Composable
public fun rememberTabBarScrollBehavior(): TabBarScrollBehavior = remember { TabBarScrollBehavior() }
