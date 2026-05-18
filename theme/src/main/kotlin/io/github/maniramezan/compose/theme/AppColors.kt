package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.maniramezan.compose.tokens.AppReferenceColors

@Immutable
public data class AppColors(
    public val primary: Color,
    public val onPrimary: Color,
    public val surface: Color,
    public val onSurface: Color,
    public val surfaceVariant: Color,
) {
    public companion object {
        public fun light(): AppColors =
            AppColors(
                primary = AppReferenceColors.Blue40,
                onPrimary = AppReferenceColors.White,
                surface = AppReferenceColors.Neutral99,
                onSurface = AppReferenceColors.Neutral10,
                surfaceVariant = AppReferenceColors.Neutral90,
            )

        public fun dark(): AppColors =
            AppColors(
                primary = AppReferenceColors.Blue80,
                onPrimary = AppReferenceColors.Neutral10,
                surface = AppReferenceColors.Neutral10,
                onSurface = AppReferenceColors.Neutral90,
                surfaceVariant = AppReferenceColors.Neutral20,
            )
    }
}
