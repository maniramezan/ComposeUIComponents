package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
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

    private val longTitle = "A section title long enough to wrap across several lines on a phone-width screen"

    @Test
    public fun sectionTitleIsAHeadingAndLongTitlesDoNotPushActionsOffScreen() {
        composeRule.setContent {
            AppTheme {
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
            AppTheme {
                SectionHeader(title = longTitle, actionLabel = "See all", onAction = {})
            }
        }

        composeRule
            .onNodeWithText(longTitle)
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
        composeRule.onNodeWithText("See all").assertIsDisplayed()
    }
}
