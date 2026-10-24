package io.github.maniramezan.compose.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class MenuPickerTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private val options = listOf("Small", "Medium", "Large")

    private val fieldMatcher = SemanticsMatcher.keyIsDefined(SemanticsProperties.EditableText)

    @Test
    public fun opensMenuAndSelectsOption() {
        var latest: String? = null
        composeRule.setContent {
            var selected by remember { mutableStateOf<String?>("Small") }
            MenuPicker(
                options = options,
                selectedOption = selected,
                onOptionSelected = {
                    latest = it
                    selected = it
                },
                optionLabel = { it },
                label = "Size",
            )
        }

        composeRule.onNode(fieldMatcher).assert(hasText("Small"))
        composeRule.onNodeWithText("Large").assertDoesNotExist()
        composeRule.onNode(fieldMatcher).performClick()
        composeRule.onNodeWithText("Large").assertExists()
        composeRule.onNodeWithText("Medium").assertExists()
        composeRule.onNodeWithText("Large").performClick()

        composeRule.waitForIdle()
        assert(latest == "Large")
        composeRule.onNode(fieldMatcher).assert(hasText("Large"))
        composeRule.onNodeWithText("Medium").assertDoesNotExist()
    }

    @Test
    public fun doesNotChangeSelectionWhenCallerIgnoresTheChoice() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Small",
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
            )
        }

        composeRule.onNode(fieldMatcher).performClick()
        composeRule.onNodeWithText("Large").performClick()
        composeRule.waitForIdle()

        composeRule.onNode(fieldMatcher).assert(hasText("Small"))
        composeRule.onNodeWithText("Medium").assertDoesNotExist()
    }

    @Test
    public fun supportsCallerSuppliedLabelsForNonStringOptions() {
        composeRule.setContent {
            var selected by remember { mutableStateOf<Int?>(2) }
            MenuPicker(
                options = listOf(1, 2, 3),
                selectedOption = selected,
                onOptionSelected = { selected = it },
                optionLabel = { "Level $it" },
            )
        }

        composeRule.onNode(fieldMatcher).assert(hasText("Level 2"))
        composeRule.onNode(fieldMatcher).performClick()
        composeRule.onNodeWithText("Level 3").performClick()
        composeRule.waitForIdle()
        composeRule.onNode(fieldMatcher).assert(hasText("Level 3"))
    }

    @Test
    public fun fieldExposesDropdownListRole() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Medium",
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
            )
        }

        composeRule
            .onNode(fieldMatcher)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.DropdownList))
    }

    @Test
    public fun fieldAnnouncesCallerSuppliedStateDescription() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Medium",
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
                stateDescription = "Medium, 2 of 3",
            )
        }

        composeRule
            .onNode(fieldMatcher)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Medium, 2 of 3"))
    }

    @Test
    public fun blankStateDescriptionIsNotAnnounced() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Medium",
                onOptionSelected = {},
                optionLabel = { it },
                stateDescription = "",
            )
        }

        composeRule
            .onNode(fieldMatcher)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
    }

    @Test
    public fun selectedMenuItemExposesSelectedSemantics() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Medium",
                onOptionSelected = {},
                optionLabel = { it },
            )
        }

        composeRule.onNode(fieldMatcher).performClick()
        composeRule.onNode(hasText("Medium") and SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)).assertIsSelected()
        composeRule.onNode(hasText("Large") and SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)).assertIsNotSelected()
    }

    @Test
    public fun disabledPickerCannotOpen() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Medium",
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
                enabled = false,
            )
        }

        composeRule.onNode(fieldMatcher).assertIsNotEnabled()
        composeRule.onNode(fieldMatcher).performClick()
        composeRule.onNodeWithText("Large").assertDoesNotExist()
    }

    @Test
    public fun errorStateExposesSupportingTextAsErrorMessage() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = null,
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
                supportingText = "Choose a size",
                isError = true,
            )
        }

        composeRule.onNodeWithText("Choose a size").assertExists()
        composeRule
            .onNode(fieldMatcher)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Error, "Choose a size"))
    }

    @Test
    public fun noErrorSemanticsWithoutErrorState() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = null,
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
                supportingText = "Choose a size",
            )
        }

        composeRule
            .onNode(fieldMatcher)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Error))
    }

    @Test
    public fun showsPlaceholderWhenNothingIsSelectedAndNoLabel() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = null,
                onOptionSelected = {},
                optionLabel = { it },
                placeholder = "Choose a size",
            )
        }

        composeRule.onNodeWithText("Choose a size").assertExists()
    }

    @Test
    public fun blankTextParametersRenderNoNodes() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = null,
                onOptionSelected = {},
                optionLabel = { it },
                label = " ",
                placeholder = "",
                supportingText = " ",
            )
        }

        composeRule
            .onAllNodes(hasText(" "), useUnmergedTree = true)
            .assertCountEquals(0)
    }

    @Test
    public fun fieldAndMenuItemsMeetTheMinimumTouchTarget() {
        composeRule.setContent {
            MenuPicker(
                options = options,
                selectedOption = "Medium",
                onOptionSelected = {},
                optionLabel = { it },
                label = "Size",
            )
        }

        composeRule.onNode(fieldMatcher).assertHeightIsAtLeast(48.dp)
        composeRule.onNode(fieldMatcher).performClick()
        composeRule.onNode(hasText("Large") and SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)).assertHeightIsAtLeast(48.dp)
    }
}
