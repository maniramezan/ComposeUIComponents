package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.tokens.AppReferenceSpacing

@Immutable
public data class AppSpacing(
    public val xs: Dp,
    public val sm: Dp,
    public val md: Dp,
    public val lg: Dp,
    public val xl: Dp,
) {
    public companion object {
        public fun default(): AppSpacing = AppSpacing(
            xs = AppReferenceSpacing.Space4,
            sm = AppReferenceSpacing.Space8,
            md = AppReferenceSpacing.Space12,
            lg = AppReferenceSpacing.Space16,
            xl = AppReferenceSpacing.Space24,
        )
    }
}
