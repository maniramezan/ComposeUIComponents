package io.github.maniramezan.compose.theme

import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Immutable
import io.github.maniramezan.compose.tokens.AppReferenceMotion

@Immutable
public data class AppMotion(
    public val shortMillis: Int,
    public val mediumMillis: Int,
    public val longMillis: Int,
    public val emphasizedEasing: Easing,
) {
    public companion object {
        public fun default(): AppMotion =
            AppMotion(
                shortMillis = AppReferenceMotion.DurationShortMillis,
                mediumMillis = AppReferenceMotion.DurationMediumMillis,
                longMillis = AppReferenceMotion.DurationLongMillis,
                emphasizedEasing = AppReferenceMotion.EmphasizedEasing,
            )
    }
}
