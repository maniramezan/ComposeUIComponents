package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class QuestionnairePageTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private val today = LocalDate.of(2026, 6, 15)

    @Test
    public fun everyExampleIsReachableWithoutCompletingOtherInputs() {
        var example by mutableStateOf(QuestionnaireExample.SingleChoice)
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    key(example) { QuestionnairePage(initialExample = example, today = today) }
                }
            }
        }
        QuestionnaireExample.entries.forEach { item ->
            composeRule.runOnIdle { example = item }
            val first = if (item == QuestionnaireExample.All) QuestionnaireExample.SingleChoice else item
            composeRule.onNode(hasText(first.prompt) and !hasSetTextAction()).assertExists()
            if (item == QuestionnaireExample.RatingScale) {
                composeRule.onNodeWithText("1 — Very low").assertExists()
                composeRule.onNodeWithText("5 — Very high").assertExists()
            }
        }
    }

    @Test
    public fun completeQuestionnaireSubmitsEveryExampleIncludingCalendarAndRating() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    QuestionnairePage(initialExample = QuestionnaireExample.All, today = today)
                }
            }
        }
        composeRule.onNodeWithText("Question 1 of 8").assertExists()
        composeRule.onNodeWithText("Option A").performClick()
        next()
        composeRule.onNodeWithText("Option B").performScrollTo().performClick()
        next()
        composeRule
            .onNode(hasSetTextAction() and hasText(QuestionnaireExample.FreeText.prompt))
            .performScrollTo()
            .performTextInput("An answer")
        next()
        composeRule.onNode(hasText("No") and !hasSetTextAction()).performScrollTo().performClick()
        next()
        composeRule.onNodeWithText("+").performScrollTo().performClick()
        next()
        next()
        next()
        composeRule.onNodeWithText("Question 8 of 8").assertExists()
        composeRule.onNodeWithText("Done").assertIsNotEnabled()
        composeRule.onNodeWithText("3 — Moderate").performScrollTo().performClick()
        composeRule.onNodeWithText("Done").performScrollTo().performClick()
        composeRule.onNodeWithText("Last event: Submitted 8 answer(s)").assertExists()
    }

    @Test
    public fun switchingExamplesAndRestartClearAnswersAndEvents() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    QuestionnairePage(today = today)
                }
            }
        }
        composeRule.onNodeWithText("Option A").performClick()
        composeRule.onNodeWithText("Done").performScrollTo().performClick()
        composeRule.onNodeWithText("Last event: Submitted 1 answer(s)").assertExists()
        composeRule.onNodeWithText("Example").performScrollTo().performClick()
        composeRule.onNodeWithText("Multiple choice").performClick()
        composeRule.onNodeWithText("Last event: None yet").assertExists()
        composeRule.onNodeWithText("No answers").assertExists()
        composeRule.onNodeWithText("Done").assertIsNotEnabled()
        composeRule.onNodeWithText("Option B").performScrollTo().performClick()
        composeRule.onNodeWithText("Restart").performScrollTo().performClick()
        composeRule.onNodeWithText("No answers").assertExists()
        composeRule.onNodeWithText("Done").assertIsNotEnabled()
    }

    @Test
    @Config(qualifiers = "w480dp-h800dp")
    public fun customCalendarRecordsDefaultSelectionExternalChangesAndRangeChanges() {
        var answer by mutableStateOf<QuestionnaireAnswer?>(null)
        var range by mutableStateOf<ClosedRange<LocalDate>>(today..today.plusDays(10))
        composeRule.setContent {
            AppTheme {
                QuestionnaireSampleCalendar("Choose date", range, answer) { answer = it }
            }
        }
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today), answer) }
        composeRule.onNodeWithText("Thursday, June 18, 2026").performClick()
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today.plusDays(3)), answer) }
        composeRule.runOnIdle { answer = QuestionnaireAnswer.Date(today.plusDays(4)) }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today.plusDays(4)), answer) }
        composeRule.runOnIdle { range = today.plusDays(8)..today.plusDays(10) }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today.plusDays(8)), answer) }
        composeRule.runOnIdle { answer = null }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today.plusDays(8)), answer) }
    }

    @Test
    @Config(qualifiers = "w320dp-h800dp")
    public fun narrowCalendarProvidesAButtonToReachTheLastWeekday() {
        var answer by mutableStateOf<QuestionnaireAnswer?>(null)
        composeRule.setContent {
            AppTheme {
                QuestionnaireSampleCalendar("Choose date", today..today.plusDays(10), answer) { answer = it }
            }
        }
        composeRule.onNodeWithText("Show later weekdays").performClick()
        composeRule.onNodeWithText("Saturday, June 20, 2026").performClick()
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today.plusDays(5)), answer) }
        composeRule.onNodeWithText("Show earlier weekdays").performClick()
        composeRule.onNodeWithText("Monday, June 15, 2026").performClick()
        composeRule.runOnIdle { assertEquals(QuestionnaireAnswer.Date(today), answer) }
    }

    private fun next() {
        composeRule
            .onNodeWithText("Next")
            .performScrollTo()
            .assertIsEnabled()
            .performClick()
    }
}
