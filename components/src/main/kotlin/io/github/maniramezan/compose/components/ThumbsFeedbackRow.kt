package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.minimumTouchTarget

/** The rating shown by a [ThumbsFeedbackRow]. */
public enum class ThumbsRating {
    /** Neither choice is selected. */
    None,

    /** The positive (thumbs up) choice is selected. */
    Positive,

    /** The negative (thumbs down) choice is selected. */
    Negative,
}

/**
 * The rating that results from tapping [tapped] while the row shows [current], or `null` when the
 * tap changes nothing and should not be reported.
 */
internal fun resolveThumbsRatingTap(
    current: ThumbsRating,
    tapped: ThumbsRating,
    allowClearing: Boolean,
): ThumbsRating? =
    when {
        tapped == ThumbsRating.None -> null
        tapped != current -> tapped
        allowClearing -> ThumbsRating.None
        else -> null
    }

/**
 * A row with two icon buttons, thumbs up and thumbs down, for rating a piece of content such as a
 * generated answer.
 *
 * The row only displays [rating] and reports taps. It does not submit anything, decide whether to
 * ask a follow-up question, or retry failed submissions; collect those decisions in
 * [onRatingChange]. The two buttons form a single-choice group: at most one is selected, and each
 * exposes its selected state to accessibility services.
 *
 * Tapping the unselected choice reports it. Tapping the choice that is already selected reports
 * [ThumbsRating.None] when [allowClearing] is `true`, and does nothing when it is `false`.
 * [onRatingChange] is never called with the rating that is already shown.
 *
 * Every string is caller-supplied, so pass already-localized values.
 *
 * ```kotlin
 * var rating by remember { mutableStateOf(ThumbsRating.None) }
 *
 * ThumbsFeedbackRow(
 *     rating = rating,
 *     onRatingChange = { rating = it },
 *     positiveContentDescription = "Good response",
 *     negativeContentDescription = "Bad response",
 * )
 * ```
 *
 * @param rating The current selection.
 * @param onRatingChange Called with the new rating when the user changes the selection.
 * @param positiveContentDescription Accessibility label of the thumbs up button. Blank values
 *   leave the button unlabeled, so always supply one.
 * @param negativeContentDescription Accessibility label of the thumbs down button. Blank values
 *   leave the button unlabeled, so always supply one.
 * @param enabled Whether the buttons respond to taps. Disabled buttons are dimmed and keep
 *   showing [rating].
 * @param allowClearing Whether tapping the selected choice clears the rating. When `false`, the
 *   only way back to [ThumbsRating.None] is for the caller to set it.
 * @param selectedStateDescription Optional state description announced for the selected button,
 *   for example "Selected". `null` or blank keeps the platform's default selected announcement.
 */
@Composable
public fun ThumbsFeedbackRow(
    rating: ThumbsRating,
    onRatingChange: (ThumbsRating) -> Unit,
    positiveContentDescription: String,
    negativeContentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    allowClearing: Boolean = true,
    selectedStateDescription: String? = null,
) {
    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ThumbsFeedbackButton(
            glyph = ThumbsFeedbackGlyph.ThumbUp,
            selected = rating == ThumbsRating.Positive,
            enabled = enabled,
            contentDescription = positiveContentDescription,
            selectedStateDescription = selectedStateDescription,
            onClick = {
                resolveThumbsRatingTap(rating, ThumbsRating.Positive, allowClearing)?.let(onRatingChange)
            },
        )
        ThumbsFeedbackButton(
            glyph = ThumbsFeedbackGlyph.ThumbDown,
            selected = rating == ThumbsRating.Negative,
            enabled = enabled,
            contentDescription = negativeContentDescription,
            selectedStateDescription = selectedStateDescription,
            onClick = {
                resolveThumbsRatingTap(rating, ThumbsRating.Negative, allowClearing)?.let(onRatingChange)
            },
        )
    }
}

/**
 * One selectable thumbs button. A selected button also gets a filled container, so the selection
 * is not conveyed by color alone.
 */
@Composable
private fun ThumbsFeedbackButton(
    glyph: ThumbsFeedbackGlyph,
    selected: Boolean,
    enabled: Boolean,
    contentDescription: String,
    selectedStateDescription: String?,
    onClick: () -> Unit,
) {
    val glyphSize = standardIconSize()
    val painter = rememberVectorPainter(rememberThumbsFeedbackGlyph(glyph, glyphSize))
    val baseGlyphColor = if (selected) AppTheme.colors.onPrimaryContainer else AppTheme.colors.onSurfaceVariant
    val glyphColor = if (enabled) baseGlyphColor else baseGlyphColor.copy(alpha = DISABLED_CONTENT_ALPHA)
    val containerModifier =
        if (selected) Modifier.pillSurface(AppTheme.colors.primaryContainer) else Modifier

    Box(
        modifier =
            Modifier
                .minimumTouchTarget(minimumTouchTargetSize())
                .then(containerModifier)
                .selectable(selected = selected, enabled = enabled, role = Role.Button, onClick = onClick)
                .semantics {
                    if (contentDescription.isNotBlank()) this.contentDescription = contentDescription
                    if (selected && !selectedStateDescription.isNullOrBlank()) {
                        stateDescription = selectedStateDescription
                    }
                },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(glyphSize)
                    .paint(painter = painter, colorFilter = ColorFilter.tint(glyphColor)),
        )
    }
}
