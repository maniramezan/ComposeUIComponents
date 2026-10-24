package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ContainerComponentsTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private val longTitle = "A section title long enough to wrap across several lines on a phone-width screen"

    @Test
    public fun sectionHeaderActionInvokesCallback() {
        var invoked = false
        composeRule.setContent {
            SectionHeader(title = "Recent", actionLabel = "See all", onAction = { invoked = true })
        }

        composeRule.onNodeWithText("See all").performClick()
        composeRule.runOnIdle { assertThat(invoked).isTrue() }
    }

    @Test
    public fun sectionTitleIsAHeadingAndLongTitlesDoNotPushActionsOffScreen() {
        composeRule.setContent {
            LargeFontContent {
                Section(
                    title = longTitle,
                    actions = { TextButton(text = "Edit", onClick = {}) },
                ) {
                    Text(text = "Body")
                }
            }
        }

        composeRule
            .onNodeWithText(longTitle)
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
        composeRule.onNodeWithText("Edit").assertIsDisplayed()
        composeRule.onNodeWithText("Body").assertIsDisplayed()
    }

    @Test
    public fun sectionHeaderKeepsItsActionVisibleWithALongTitle() {
        composeRule.setContent {
            LargeFontContent {
                SectionHeader(title = longTitle, actionLabel = "See all", onAction = {})
            }
        }

        composeRule
            .onNodeWithText(longTitle)
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
        composeRule.onNodeWithText("See all").assertIsDisplayed()
    }

    @Composable
    private fun LargeFontContent(content: @Composable () -> Unit) {
        val density = LocalDensity.current.density
        CompositionLocalProvider(LocalDensity provides Density(density, fontScale = 2f)) {
            AppTheme {
                Box(Modifier.width(240.dp)) { content() }
            }
        }
    }
}
