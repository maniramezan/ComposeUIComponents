package io.github.maniramezan.compose.components

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ContainerComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun sectionHeaderActionInvokesCallback() {
        var invoked = false
        composeRule.setContent {
            AppTheme {
                SectionHeader(title = "Recent", actionLabel = "See all", onAction = { invoked = true })
            }
        }

        composeRule.onNodeWithText("See all").performClick()
        composeRule.runOnIdle { assertThat(invoked).isTrue() }
    }
}
