package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import io.github.maniramezan.compose.components.CachedAsyncImage
import io.github.maniramezan.compose.components.ImageCachePolicy
import io.github.maniramezan.compose.theme.AppTheme

// ─────────────────────────────────────────────────────────────────────────────
// Media
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CachedAsyncImagePage() {
    var brokenSource by remember { mutableStateOf(false) }
    var crossfade by remember { mutableStateOf(true) }
    var cacheEnabled by remember { mutableStateOf(true) }
    var scaleIndex by remember { mutableIntStateOf(0) }
    val scales = listOf(ContentScale.Fit, ContentScale.Crop, ContentScale.FillBounds)
    val cachePolicy = if (cacheEnabled) ImageCachePolicy.ENABLED else ImageCachePolicy.DISABLED

    SamplePage(
        preview = {
            CachedAsyncImage(
                // A bundled framework drawable keeps the demo offline and deterministic; the
                // broken source exercises the error painter.
                data = if (brokenSource) "file:///missing-image.png" else android.R.drawable.ic_menu_gallery,
                contentDescription = "Sample image",
                modifier = Modifier.size(AppTheme.spacing.x9 * 2),
                memoryCachePolicy = cachePolicy,
                diskCachePolicy = cachePolicy,
                crossfade = crossfade,
                placeholder = ColorPainter(AppTheme.colors.surfaceVariant),
                error = ColorPainter(AppTheme.colors.error),
                contentScale = scales[scaleIndex],
            )
            Text(text = if (brokenSource) "Showing the error painter" else "Loaded from a local resource")
        },
        controls = {
            ControlSegmented(
                label = "Content scale",
                options = listOf("Fit", "Crop", "Fill"),
                selectedIndex = scaleIndex,
                onOptionSelected = { scaleIndex = it },
            )
            ControlSwitch(label = "Broken source", checked = brokenSource, onCheckedChange = { brokenSource = it })
            ControlSwitch(label = "Crossfade", checked = crossfade, onCheckedChange = { crossfade = it })
            ControlSwitch(label = "Memory + disk cache", checked = cacheEnabled, onCheckedChange = { cacheEnabled = it })
        },
    )
}
