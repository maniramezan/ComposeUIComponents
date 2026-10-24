package io.github.maniramezan.compose.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ThumbsFeedbackRowTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private val reported = mutableListOf<ThumbsRating>()

    private fun setRow(
        initial: ThumbsRating = ThumbsRating.None,
        enabled: Boolean = true,
        allowClearing: Boolean = true,
        selectedStateDescription: String? = null,
        positive: String = UP,
    ) {
        composeRule.setContent {
            var rating by remember { mutableStateOf(initial) }
            ThumbsFeedbackRow(
                rating = rating,
                onRatingChange = {
                    reported += it
                    rating = it
                },
                positiveContentDescription = positive,
                negativeContentDescription = DOWN,
                enabled = enabled,
                allowClearing = allowClearing,
                selectedStateDescription = selectedStateDescription,
            )
        }
    }

    @Test
    public fun noRatingShowsBothUnselected() {
        setRow()

        composeRule.onNodeWithContentDescription(UP).assertIsNotSelected().assertIsEnabled()
        composeRule.onNodeWithContentDescription(DOWN).assertIsNotSelected().assertIsEnabled()
    }

    @Test
    public fun tappingPositiveSelectsItAndReportsPositive() {
        setRow()

        composeRule.onNodeWithContentDescription(UP).performClick()

        assertEquals(listOf(ThumbsRating.Positive), reported)
        composeRule.onNodeWithContentDescription(UP).assertIsSelected()
        composeRule.onNodeWithContentDescription(DOWN).assertIsNotSelected()
    }

    @Test
    public fun tappingNegativeSelectsItAndReportsNegative() {
        setRow()

        composeRule.onNodeWithContentDescription(DOWN).performClick()

        assertEquals(listOf(ThumbsRating.Negative), reported)
        composeRule.onNodeWithContentDescription(DOWN).assertIsSelected()
        composeRule.onNodeWithContentDescription(UP).assertIsNotSelected()
    }

    @Test
    public fun tappingTheOtherChoiceSwitchesSelection() {
        setRow(initial = ThumbsRating.Positive)

        composeRule.onNodeWithContentDescription(DOWN).performClick()

        assertEquals(listOf(ThumbsRating.Negative), reported)
        composeRule.onNodeWithContentDescription(UP).assertIsNotSelected()
        composeRule.onNodeWithContentDescription(DOWN).assertIsSelected()
    }

    @Test
    public fun tappingSelectedChoiceClearsRatingByDefault() {
        setRow(initial = ThumbsRating.Positive)

        composeRule.onNodeWithContentDescription(UP).performClick()

        assertEquals(listOf(ThumbsRating.None), reported)
        composeRule.onNodeWithContentDescription(UP).assertIsNotSelected()
    }

    @Test
    public fun tappingSelectedChoiceDoesNothingWhenClearingIsNotAllowed() {
        setRow(initial = ThumbsRating.Negative, allowClearing = false)

        composeRule.onNodeWithContentDescription(DOWN).performClick()

        assertEquals(emptyList<ThumbsRating>(), reported)
        composeRule.onNodeWithContentDescription(DOWN).assertIsSelected()
    }

    @Test
    public fun disabledRowIgnoresTapsAndKeepsShowingRating() {
        setRow(initial = ThumbsRating.Positive, enabled = false)

        composeRule
            .onNodeWithContentDescription(UP)
            .assertIsNotEnabled()
            .assertIsSelected()
            .performClick()
        composeRule
            .onNodeWithContentDescription(DOWN)
            .assertIsNotEnabled()
            .assertIsNotSelected()
            .performClick()

        assertEquals(emptyList<ThumbsRating>(), reported)
    }

    @Test
    public fun buttonsExposeButtonRole() {
        setRow()

        val isButton = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)
        composeRule.onNodeWithContentDescription(UP).assert(isButton)
        composeRule.onNodeWithContentDescription(DOWN).assert(isButton)
    }

    @Test
    public fun selectedButtonExposesCallerStateDescription() {
        setRow(initial = ThumbsRating.Positive, selectedStateDescription = SELECTED)

        composeRule
            .onNodeWithContentDescription(UP)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, SELECTED))
        composeRule
            .onNodeWithContentDescription(DOWN)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
    }

    @Test
    public fun withoutStateDescriptionNoneIsExposed() {
        setRow(initial = ThumbsRating.Positive)

        composeRule
            .onNodeWithContentDescription(UP)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
    }

    @Test
    public fun buttonsMeetMinimumTouchTarget() {
        setRow()

        composeRule.onNodeWithContentDescription(UP).assertWidthIsAtLeast(MIN_TARGET).assertHeightIsAtLeast(MIN_TARGET)
        composeRule.onNodeWithContentDescription(DOWN).assertWidthIsAtLeast(MIN_TARGET).assertHeightIsAtLeast(MIN_TARGET)
    }

    @Test
    public fun blankContentDescriptionLeavesButtonUnlabeled() {
        setRow(positive = "")

        composeRule.onNodeWithContentDescription(UP).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(DOWN).assertExists()
    }

    private companion object {
        const val UP = "Helpful"
        const val DOWN = "Not helpful"
        const val SELECTED = "Chosen"
        val MIN_TARGET = 48.dp
    }
}
