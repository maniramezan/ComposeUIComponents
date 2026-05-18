package io.github.maniramezan.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
public data class AppTypography(
    public val display: TextStyle,
    public val title: TextStyle,
    public val body: TextStyle,
    public val label: TextStyle,
) {
    public companion object {
        public fun default(): AppTypography = AppTypography(
            display = TextStyle(
                fontSize = 40.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
            ),
            title = TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            body = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
            ),
            label = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

internal fun AppTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = display,
    titleLarge = title,
    bodyLarge = body,
    labelLarge = label,
)
