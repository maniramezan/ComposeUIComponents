package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import io.github.maniramezan.compose.theme.AppTheme

/** Diameter presets for [Avatar]. */
public enum class AvatarSize {
    /** `AppTheme.spacing.x4` (32dp by default), for dense rows and inline mentions. */
    Small,

    /** `AppTheme.spacing.x5` (40dp by default), for list rows. */
    Medium,

    /** `AppTheme.spacing.x6` (48dp by default), for headers and profile cards. */
    Large,
}

/**
 * A circular avatar for a person or entity: the caller's [image] when provided, otherwise the
 * initials derived from [name] on a tinted circle.
 *
 * Initials take the first letter of the first and last words of [name] (one letter for a
 * single word), skipping digits and punctuation. When [name] yields no letters and there is no
 * [image], the tinted circle renders empty.
 *
 * The avatar is a single accessibility node with an image role, announced as [name]. Pass a
 * blank [name] with an [image] for a decorative avatar beside text that already names the
 * person.
 *
 * @param name the caller-supplied display name, used for initials and the accessibility label.
 * @param size the avatar diameter.
 * @param image optional image content (for example a `CachedAsyncImage` with
 *   `ContentScale.Crop`). It is clipped to the circle and sized to fill it.
 */
@Composable
public fun Avatar(
    name: String,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Medium,
    image: (@Composable () -> Unit)? = null,
) {
    val initials = remember(name) { avatarInitials(name) }
    Box(
        modifier =
            modifier
                .size(size.diameter())
                .clip(CircleShape)
                .background(AppTheme.colors.primaryContainer)
                .border(AppTheme.spacing.strokeThin, AppTheme.colors.outlineVariant, CircleShape)
                .clearAndSetSemantics {
                    if (name.isNotBlank()) {
                        contentDescription = name
                        role = Role.Image
                    }
                },
        contentAlignment = Alignment.Center,
    ) {
        when {
            image != null -> {
                Box(modifier = Modifier.matchParentSize(), propagateMinConstraints = true) {
                    image()
                }
            }
            initials.isNotEmpty() -> {
                val style = size.initialsStyle()
                Text(
                    text = initials,
                    style = style,
                    color = AppTheme.colors.onPrimaryContainer,
                    maxLines = 1,
                    autoSize =
                        TextAutoSize.StepBased(
                            minFontSize = style.fontSize / 2,
                            maxFontSize = style.fontSize,
                        ),
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
private fun AvatarSize.diameter(): Dp =
    when (this) {
        AvatarSize.Small -> AppTheme.spacing.x4
        AvatarSize.Medium -> AppTheme.spacing.x5
        AvatarSize.Large -> AppTheme.spacing.x6
    }

@Composable
@ReadOnlyComposable
private fun AvatarSize.initialsStyle(): TextStyle =
    when (this) {
        AvatarSize.Small -> AppTheme.typography.labelMediumSemibold
        AvatarSize.Medium -> AppTheme.typography.labelLargeBold
        AvatarSize.Large -> AppTheme.typography.titleMedium
    }

/**
 * Up to two uppercase initials from [name]: the first letter of the first and last words,
 * where words are separated by Unicode whitespace or punctuation and only letters count.
 */
internal fun avatarInitials(name: String): String {
    val letters =
        name
            .split(avatarWordSeparator)
            .mapNotNull { word -> avatarInitialPattern.find(word)?.value }
    val first = letters.firstOrNull() ?: return ""
    val last = letters.drop(1).lastOrNull()
    return buildString {
        append(first)
        if (last != null) append(last)
    }.uppercase()
}

private val avatarWordSeparator = Regex("[\\s\\p{Z}\\p{P}]+")
private val avatarInitialPattern = Regex("^\\p{L}\\p{M}*")
