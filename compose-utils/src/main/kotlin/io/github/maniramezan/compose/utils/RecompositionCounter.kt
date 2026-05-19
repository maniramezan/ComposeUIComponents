package io.github.maniramezan.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
public class RecompositionCounter internal constructor() {
    public var count: Int by mutableIntStateOf(0)
        private set

    internal fun increment(): Int {
        count += 1
        return count
    }
}

@Composable
public fun rememberRecompositionCounter(onRecompose: ((Int) -> Unit)? = null): RecompositionCounter {
    val counter = remember { RecompositionCounter() }
    SideEffect {
        onRecompose?.invoke(counter.increment())
    }
    return counter
}
