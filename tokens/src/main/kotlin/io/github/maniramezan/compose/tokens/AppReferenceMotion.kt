package io.github.maniramezan.compose.tokens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing

public object AppReferenceMotion {
    public const val DurationShortMillis: Int = 150
    public const val DurationMediumMillis: Int = 300
    public const val DurationLongMillis: Int = 500

    public val EmphasizedEasing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
}
