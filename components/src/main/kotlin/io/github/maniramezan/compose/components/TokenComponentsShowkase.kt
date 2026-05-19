package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Spacing Tokens", group = "Tokens")
@Composable
public fun SpacingTokensShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        ) {
            SpacingTokenRow(name = "xs", size = AppTheme.spacing.xs)
            SpacingTokenRow(name = "sm", size = AppTheme.spacing.sm)
            SpacingTokenRow(name = "md", size = AppTheme.spacing.md)
            SpacingTokenRow(name = "lg", size = AppTheme.spacing.lg)
            SpacingTokenRow(name = "xl", size = AppTheme.spacing.xl)
        }
    }

@ShowkaseComposable(name = "Motion Tokens", group = "Tokens")
@Composable
public fun MotionTokensShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        ) {
            AppText(text = "Short: ${AppTheme.motion.shortMillis}ms", style = AppTextStyle.Body)
            AppText(text = "Medium: ${AppTheme.motion.mediumMillis}ms", style = AppTextStyle.Body)
            AppText(text = "Long: ${AppTheme.motion.longMillis}ms", style = AppTextStyle.Body)
            AppText(text = "Emphasized easing: ${AppTheme.motion.emphasizedEasing}", style = AppTextStyle.Label)
        }
    }

@ShowkaseComposable(name = "Icon Tokens", group = "Tokens")
@Composable
public fun IconTokensShowkase(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        Row(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
        ) {
            IconTokenItem(name = "Check", icon = AppTheme.icons.check.imageVector)
            IconTokenItem(name = "Close", icon = AppTheme.icons.close.imageVector)
        }
    }

@Composable
private fun SpacingTokenRow(
    name: String,
    size: Dp,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        Text(text = name)
        Spacer(
            modifier =
                Modifier
                    .size(width = size, height = AppTheme.spacing.md),
        )
        Text(text = size.toString())
    }
}

@Composable
private fun IconTokenItem(
    name: String,
    icon: ImageVector,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
        Icon(imageVector = icon, contentDescription = "$name icon token")
        Text(text = name)
    }
}
