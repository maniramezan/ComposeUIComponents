package io.github.maniramezan.compose.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.Avatar
import io.github.maniramezan.compose.components.AvatarSize
import io.github.maniramezan.compose.components.Badge
import io.github.maniramezan.compose.components.CountBadge
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.theme.AppTheme
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────────────────────
// Indicators
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun BadgePage() {
    var text by remember { mutableStateOf("Beta") }
    var prominent by remember { mutableStateOf(false) }

    SamplePage(
        preview = {
            Badge(text = text, prominent = prominent)
        },
        controls = {
            TextField(value = text, onValueChange = { text = it }, label = "Text")
            ControlSwitch(label = "Prominent", checked = prominent, onCheckedChange = { prominent = it })
        },
    )
}

@Composable
internal fun CountBadgePage() {
    var dot by remember { mutableStateOf(false) }
    var count by remember { mutableFloatStateOf(3f) }
    var maxIndex by remember { mutableIntStateOf(1) }
    val maxOptions = listOf(9, 99, 999)

    SamplePage(
        preview = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val value = if (dot) null else count.roundToInt()
                CountBadge(
                    count = value,
                    contentDescription = if (value == null) "New activity" else "$value unread",
                    maxCount = maxOptions[maxIndex],
                )
                // Pinned to an icon-sized placeholder's corner, as on a tab icon.
                Box {
                    Box(
                        modifier =
                            Modifier
                                .size(AppTheme.spacing.x3)
                                .background(AppTheme.colors.onSurfaceVariant, CircleShape),
                    )
                    CountBadge(
                        count = value,
                        maxCount = maxOptions[maxIndex],
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = AppTheme.spacing.x1, y = -AppTheme.spacing.half),
                    )
                }
            }
        },
        controls = {
            ControlSwitch(label = "Dot (count = null)", checked = dot, onCheckedChange = { dot = it })
            ControlSlider(
                label = "Count: ${count.roundToInt()} (0 hides the badge)",
                value = count,
                onValueChange = { count = it },
                valueRange = 0f..1200f,
            )
            ControlSegmented(
                label = "maxCount",
                options = maxOptions.map { it.toString() },
                selectedIndex = maxIndex,
                onOptionSelected = { maxIndex = it },
            )
        },
    )
}

@Composable
internal fun AvatarPage() {
    var name by remember { mutableStateOf("Avery Quinn") }
    var sizeIndex by remember { mutableIntStateOf(1) }
    var withImage by remember { mutableStateOf(false) }

    SamplePage(
        preview = {
            Avatar(
                name = name,
                size = AvatarSize.entries[sizeIndex],
                image =
                    if (withImage) {
                        {
                            Box(
                                modifier =
                                    Modifier
                                        .size(AppTheme.spacing.x6)
                                        .background(AppTheme.colors.primary),
                            )
                        }
                    } else {
                        null
                    },
            )
        },
        controls = {
            TextField(value = name, onValueChange = { name = it }, label = "Name")
            ControlSegmented(
                label = "Size",
                options = AvatarSize.entries.map { it.name },
                selectedIndex = sizeIndex,
                onOptionSelected = { sizeIndex = it },
            )
            ControlSwitch(label = "Image slot", checked = withImage, onCheckedChange = { withImage = it })
        },
    )
}
