package io.github.maniramezan.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Immutable
public data class IconToken(
    public val imageVector: ImageVector,
    public val autoMirror: Boolean = false,
)

@Immutable
public data class AppIcons(
    public val check: IconToken,
    public val close: IconToken,
) {
    public companion object {
        public fun default(): AppIcons =
            AppIcons(
                check = IconToken(EmptyIcon),
                close = IconToken(EmptyIcon),
            )
    }
}

private val EmptyIcon: ImageVector =
    ImageVector
        .Builder(
            name = "EmptyIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).build()
