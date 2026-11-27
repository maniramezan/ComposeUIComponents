package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppSpacing
import io.github.maniramezan.compose.theme.AppStroke
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class LayoutComponentsTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    @Test
    public fun adaptiveContentContainerPassesThroughOnNarrowScreens() {
        composeRule.setContent {
            Box(Modifier.requiredWidth(NARROW)) {
                AdaptiveContentContainer(maxWidth = MAX) {
                    Box(Modifier.fillMaxWidth().height(10.dp).testTag(CONTENT))
                }
            }
        }

        composeRule
            .onNodeWithTag(CONTENT)
            .assertWidthIsEqualTo(NARROW)
            .assertLeftPositionInRootIsEqualTo(0.dp)
    }

    @Test
    public fun overlayCardSurfaceAndDividerRenderCallerContent() {
        composeRule.setContent {
            Column {
                OverlayCard { Text("Overlay content") }
                HorizontalDivider(modifier = Modifier.testTag(DIVIDER))
                Surface { Text("Surface content") }
            }
        }

        composeRule.onNodeWithText("Overlay content").assertIsDisplayed()
        composeRule.onNodeWithText("Surface content").assertIsDisplayed()
        composeRule.onNodeWithTag(DIVIDER).assertIsDisplayed()
    }

    @Test
    public fun dividersReadLegacyStrokeOverridesInLightTheme() {
        val spacing = AppSpacing.default().copy(strokeThin = 3.dp)
        composeRule.setContent {
            AppTheme(spacing = spacing, darkTheme = false) {
                HorizontalDivider(modifier = Modifier.testTag(DIVIDER))
            }
        }

        composeRule.onNodeWithTag(DIVIDER).assertHeightIsEqualTo(3.dp)
    }

    @Test
    public fun explicitStrokeOverridesLegacyWidthsInDarkTheme() {
        val spacing = AppSpacing.default().copy(strokeThin = 6.dp, strokeRegular = 8.dp, strokeThick = 10.dp)
        val stroke = AppStroke(thin = 2.dp, regular = 4.dp, thick = 5.dp)
        composeRule.setContent {
            AppTheme(stroke = stroke, spacing = spacing, darkTheme = true) {
                Column {
                    HorizontalDivider(modifier = Modifier.testTag(DIVIDER))
                    VerticalDivider(modifier = Modifier.height(16.dp).testTag(VERTICAL_DIVIDER))
                    LabeledDivider(label = "", modifier = Modifier.testTag(LABELED_DIVIDER))
                    Text(
                        "${AppTheme.spacing.strokeThin}, ${AppTheme.spacing.strokeRegular}, ${AppTheme.spacing.strokeThick}",
                    )
                }
            }
        }

        composeRule.onNodeWithTag(DIVIDER).assertHeightIsEqualTo(stroke.thin)
        composeRule.onNodeWithTag(VERTICAL_DIVIDER).assertWidthIsEqualTo(stroke.thin)
        composeRule.onNodeWithTag(LABELED_DIVIDER).assertHeightIsEqualTo(stroke.thin)
        composeRule.onNodeWithText("${stroke.thin}, ${stroke.regular}, ${stroke.thick}").assertIsDisplayed()
    }

    private companion object {
        const val CONTENT = "content"
        const val DIVIDER = "divider"
        const val VERTICAL_DIVIDER = "vertical-divider"
        const val LABELED_DIVIDER = "labeled-divider"
        val NARROW = 320.dp
        val MAX = 600.dp
    }
}
