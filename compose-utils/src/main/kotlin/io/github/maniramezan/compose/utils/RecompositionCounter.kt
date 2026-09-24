package io.github.maniramezan.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Counts how many times the composable that remembered it has (re)composed. A debugging and
 * test aid for verifying that state reads are deferred and recomposition stays scoped; obtain
 * one with [rememberRecompositionCounter].
 */
@Stable
public class RecompositionCounter internal constructor() {
    public var count: Int by mutableIntStateOf(0)
        private set

    internal fun increment(): Int {
        count += 1
        return count
    }
}

/**
 * Remembers a [RecompositionCounter] that increments after every successful composition of
 * the calling scope, invoking [onRecompose] with the new count.
 */
@Composable
public fun rememberRecompositionCounter(onRecompose: ((Int) -> Unit)? = null): RecompositionCounter {
    val counter = remember { RecompositionCounter() }
    SideEffect {
        onRecompose?.invoke(counter.increment())
    }
    return counter
}
