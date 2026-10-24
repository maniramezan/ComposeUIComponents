package io.github.maniramezan.compose.sample

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.ThumbsFeedbackRow
import io.github.maniramezan.compose.components.ThumbsRating

private val RATING_OPTIONS = ThumbsRating.entries.map { it.name }

@Composable
internal fun ThumbsFeedbackRowPage() {
    var rating by remember { mutableStateOf(ThumbsRating.None) }
    var enabled by remember { mutableStateOf(true) }
    var allowClearing by remember { mutableStateOf(true) }
    var describeButtons by remember { mutableStateOf(true) }
    var describeSelected by remember { mutableStateOf(true) }
    var reportCount by remember { mutableIntStateOf(0) }
    var lastReported by remember { mutableStateOf<ThumbsRating?>(null) }

    SamplePage(
        preview = {
            ThumbsFeedbackRow(
                rating = rating,
                onRatingChange = {
                    rating = it
                    lastReported = it
                    reportCount++
                },
                positiveContentDescription = if (describeButtons) "Helpful" else "",
                negativeContentDescription = if (describeButtons) "Not helpful" else "",
                enabled = enabled,
                allowClearing = allowClearing,
                selectedStateDescription = if (describeSelected) "Selected" else null,
            )
            Text("Rating changes: $reportCount (last: ${lastReported?.name ?: "none"})")
        },
        controls = {
            ControlSegmented(
                label = "Rating",
                options = RATING_OPTIONS,
                selectedIndex = rating.ordinal,
                onOptionSelected = { rating = ThumbsRating.entries[it] },
            )
            ControlSwitch(label = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ControlSwitch(label = "Allow clearing", checked = allowClearing, onCheckedChange = { allowClearing = it })
            ControlSwitch(label = "Button descriptions", checked = describeButtons, onCheckedChange = { describeButtons = it })
            ControlSwitch(label = "Selected state description", checked = describeSelected, onCheckedChange = { describeSelected = it })
        },
    )
}
