package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class AdaptiveContentContainerTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun capsAndCentersContentOnWideScreens() {
        composeRule.setContent {
            AppTheme {
                Box(Modifier.requiredWidth(WIDE).testTag(PARENT)) {
                    AdaptiveContentContainer(maxWidth = MAX) {
                        Box(Modifier.fillMaxWidth().height(10.dp).testTag(CONTENT))
                    }
                }
            }
        }

        val parentLeft = composeRule.onNodeWithTag(PARENT).getUnclippedBoundsInRoot().left
        composeRule
            .onNodeWithTag(CONTENT)
            .assertWidthIsEqualTo(MAX)
            .assertLeftPositionInRootIsEqualTo(parentLeft + (WIDE - MAX) / 2)
    }

    private companion object {
        const val CONTENT = "content"
        const val PARENT = "parent"
        val WIDE = 1000.dp
        val MAX = 600.dp
    }
}
