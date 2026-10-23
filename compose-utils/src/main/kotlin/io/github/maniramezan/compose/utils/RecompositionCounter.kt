package io.github.maniramezan.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Counts how many times the composable that created it has been (re)composed. Obtain one with
 * [rememberRecompositionCounter]. Intended for local debugging and tests that guard against
 * per-frame recomposition, not for production logic.
 */
@Stable
public class RecompositionCounter internal constructor() {
    /** Number of successful compositions so far, starting at `1` after the first composition. */
    public var count: Int by mutableIntStateOf(0)
        private set

    internal fun increment(): Int {
        count += 1
        return count
    }
}

/**
 * Remembers a [RecompositionCounter] for the calling composable and increments it after every
 * successful composition of that scope.
 *
 * @param onRecompose optional callback invoked with the new count after each composition, e.g.
 *   to log while investigating a hot composable. See `docs/performance.md`.
 */
@Composable
public fun rememberRecompositionCounter(onRecompose: ((Int) -> Unit)? = null): RecompositionCounter {
    val counter = remember { RecompositionCounter() }
    SideEffect {
        onRecompose?.invoke(counter.increment())
    }
    return counter
}
