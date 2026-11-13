package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class SampleFloatingActionPagesTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun selectedItemModeShowsTitleOnlyWhenTriggerTitleIsEnabled() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    ExpandableFABPage()
                }
            }
        }
        composeRule
            .onNodeWithText(
                "Show selected item on FAB",
            ).performScrollTo()
            .performSemanticsAction(SemanticsActions.OnClick) { it() }
        composeRule.onNodeWithText("Show selected item on FAB").assertIsOn()
        composeRule.onNodeWithContentDescription("Actions").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        composeRule.onNodeWithText("Action 1").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        composeRule.onNodeWithContentDescription("Action 1").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Action 1", useUnmergedTree = true).assertDoesNotExist()
        composeRule.onNodeWithText("Show trigger title").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        composeRule.onNodeWithContentDescription("Action 1").performScrollTo()
        composeRule.onNodeWithText("Action 1", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Show trigger title").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        composeRule.onNodeWithContentDescription("Action 1").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Action 1", useUnmergedTree = true).assertDoesNotExist()
        composeRule.onNodeWithText("Last selection: Action 1").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Show selected item on FAB").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("Actions").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Action 1", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    public fun standaloneButtonCountsTapsAndRetainsCountAfterChangingPresentation() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    FabPage()
                }
            }
        }
        composeRule.onNodeWithContentDescription("Add item").performClick()
        composeRule.onNodeWithText("Button taps: 1").assertIsDisplayed()
        composeRule.onNodeWithText("Show title").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("Add item").performScrollTo().performClick()
        composeRule.onNodeWithText("Button taps: 2").performScrollTo().assertIsDisplayed()
    }
}
