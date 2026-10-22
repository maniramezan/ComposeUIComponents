package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class LayoutComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun adaptiveContentContainerPassesThroughOnNarrowScreens() {
        composeRule.setContent {
            AppTheme {
                Box(Modifier.requiredWidth(NARROW)) {
                    AdaptiveContentContainer(maxWidth = MAX) {
                        Box(Modifier.fillMaxWidth().height(10.dp).testTag(CONTENT))
                    }
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
            AppTheme {
                Column {
                    OverlayCard { Text("Overlay content") }
                    HorizontalDivider(modifier = Modifier.testTag(DIVIDER))
                    Surface { Text("Surface content") }
                }
            }
        }

        composeRule.onNodeWithText("Overlay content").assertIsDisplayed()
        composeRule.onNodeWithText("Surface content").assertIsDisplayed()
        composeRule.onNodeWithTag(DIVIDER).assertIsDisplayed()
    }

    private companion object {
        const val CONTENT = "content"
        const val DIVIDER = "divider"
        val NARROW = 320.dp
        val MAX = 600.dp
    }
}
