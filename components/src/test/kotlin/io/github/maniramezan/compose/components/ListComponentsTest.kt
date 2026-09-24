package io.github.maniramezan.compose.components

import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
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
public class ListComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private val politeLiveRegion = SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite)

    @Test
    public fun emptyStateRendersTitleMessageAndAction() {
        composeRule.setContent {
            AppTheme {
                EmptyState(
                    title = "Nothing here",
                    message = "Items you add appear here.",
                    action = { TextButton(text = "Add item", onClick = {}) },
                )
            }
        }

        composeRule.onNodeWithText("Nothing here").assertIsDisplayed()
        composeRule.onNodeWithText("Items you add appear here.").assertIsDisplayed()
        composeRule.onNodeWithText("Add item").assertIsDisplayed()
    }

    @Test
    public fun loadingAndErrorStatesAreAnnouncedAsLiveRegions() {
        composeRule.setContent {
            AppTheme {
                LoadingState(label = "Loading items")
                ErrorState(title = "Something went wrong", message = "Try again later.")
            }
        }

        composeRule.onNode(hasTextInLiveRegion("Loading items")).assertExists()
        composeRule.onNode(hasTextInLiveRegion("Something went wrong")).assertExists()
    }

    private fun hasTextInLiveRegion(text: String): SemanticsMatcher = politeLiveRegion and hasAnyDescendant(hasText(text))
}
