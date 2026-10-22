package io.github.maniramezan.compose.sample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.CachedAsyncImage
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.theme.AppTheme
import java.io.ByteArrayOutputStream

// ─────────────────────────────────────────────────────────────────────────────
// Media
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun CachedAsyncImagePage() {
    val scales = listOf("Fit", "Crop", "FillBounds")
    var scaleIndex by remember { mutableIntStateOf(1) }
    var crossfade by remember { mutableStateOf(true) }
    var reloadKey by remember { mutableIntStateOf(0) }
    val startColor = AppTheme.colors.primary.toArgb()
    val endColor = AppTheme.colors.primaryContainer.toArgb()
    // The sample has no network permission, so it loads locally generated PNG bytes. Real callers
    // pass a URL, Uri, File, or resource; the component API is the same.
    val imageBytes = remember(startColor, endColor) { sampleGradientPng(startColor, endColor) }

    SamplePage(
        preview = {
            CachedAsyncImage(
                data = imageBytes,
                contentDescription = "Sample gradient",
                modifier = Modifier.width(SAMPLE_IMAGE_WIDTH).height(SAMPLE_IMAGE_HEIGHT),
                memoryCacheKey = "sample-gradient-$reloadKey",
                crossfade = crossfade,
                contentScale =
                    when (scaleIndex) {
                        0 -> ContentScale.Fit
                        2 -> ContentScale.FillBounds
                        else -> ContentScale.Crop
                    },
            )
        },
        controls = {
            ControlSegmented(
                label = "contentScale",
                options = scales,
                selectedIndex = scaleIndex,
                onOptionSelected = { scaleIndex = it },
            )
            ControlSwitch(label = "Crossfade", checked = crossfade, onCheckedChange = { crossfade = it })
            TextButton(text = "Reload (new cache key)", onClick = { reloadKey++ })
        },
    )
}

private fun sampleGradientPng(
    startColor: Int,
    endColor: Int,
): ByteArray {
    val bitmap = Bitmap.createBitmap(SAMPLE_BITMAP_WIDTH, SAMPLE_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888)
    val paint =
        Paint().apply {
            shader =
                LinearGradient(
                    0f,
                    0f,
                    SAMPLE_BITMAP_WIDTH.toFloat(),
                    SAMPLE_BITMAP_HEIGHT.toFloat(),
                    startColor,
                    endColor,
                    Shader.TileMode.CLAMP,
                )
        }
    Canvas(bitmap).drawRect(0f, 0f, SAMPLE_BITMAP_WIDTH.toFloat(), SAMPLE_BITMAP_HEIGHT.toFloat(), paint)
    return ByteArrayOutputStream().use { stream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, stream)
        bitmap.recycle()
        stream.toByteArray()
    }
}

private const val SAMPLE_BITMAP_WIDTH = 640
private const val SAMPLE_BITMAP_HEIGHT = 360
private const val PNG_QUALITY = 100
private val SAMPLE_IMAGE_WIDTH = 240.dp
private val SAMPLE_IMAGE_HEIGHT = 240.dp
