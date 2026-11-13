package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class QuestionnaireViewTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private val labels = QuestionnaireLabels("Back", "Next", "Submit", "Increase", "Decrease", "Skip", "Optional")
    private val today = LocalDate.of(2026, 6, 15)
    private val options = listOf(QuestionnaireOption("a", "Option A"), QuestionnaireOption("b", "Option B"))
    private var submitted: Map<String, QuestionnaireAnswer>? = null

    @Test
    public fun requiredChoiceGatesNavigationAndBackPreservesSelection() {
        composeRule.setContent {
            QuestionnaireHost(listOf(choice(), textQuestion()))
        }
        composeRule.onNodeWithText("Next").assertIsNotEnabled()
        composeRule.onNodeWithText("Option A").assertHeightIsAtLeast(48.dp).performClick()
        composeRule.onNodeWithText("Option A").assertIsSelected()
        composeRule.onNodeWithText("Next").performClick()
        composeRule.onNode(hasSetTextAction()).assertExists()
        composeRule.onNodeWithText("Back").performClick()
        composeRule.onNodeWithText("Option A").assertIsSelected().performClick()
        composeRule.onNodeWithText("Next").assertIsNotEnabled()
    }

    @Test
    public fun booleanFalseSubmitsAsAnExplicitAnswer() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(QuestionnaireQuestion("boolean", "Continue?", QuestionnaireQuestionKind.Boolean("Yes", "No"), isRequired = true)),
            )
        }
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNodeWithText("No").performClick()
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("boolean", QuestionnaireAnswer.Boolean(false)) }
    }

    @Test
    public fun multipleChoiceChipsSubmitInOptionOrder() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(
                    choice().copy(kind = QuestionnaireQuestionKind.MultipleChoice(options), choiceStyle = QuestionnaireChoiceStyle.Chips),
                ),
            )
        }
        composeRule.onNodeWithText("Option B").assertHeightIsAtLeast(48.dp).performClick()
        composeRule.onNodeWithText("Option A").performClick()
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("choice", QuestionnaireAnswer.Choices(listOf("a", "b"))) }
    }

    @Test
    public fun textRequiresContentAndTrimsSubmission() {
        composeRule.setContent { QuestionnaireHost(listOf(textQuestion().copy(isRequired = true))) }
        composeRule.onNode(hasSetTextAction()).performTextInput("   ")
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performTextInput("value  ")
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("text", QuestionnaireAnswer.Text("value")) }
    }

    @Test
    public fun stepperStartsAtLowerBoundAndSubmissionSnapsTypedNumber() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(QuestionnaireQuestion("number", "Amount", QuestionnaireQuestionKind.Number(-5.0..10.0, 3.0), isRequired = true)),
            )
        }
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("Increase").assertHeightIsAtLeast(48.dp).performClick()
        composeRule.onNodeWithText("Submit").assertIsEnabled()
        composeRule.onNodeWithContentDescription("Decrease").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("Increase").performClick()
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("number", QuestionnaireAnswer.Number(-2.0)) }
    }

    @Test
    public fun pastedNumericInputIsSanitizedAndSnappedOnSubmission() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(QuestionnaireQuestion("number", "Amount", QuestionnaireQuestionKind.Number(-5.0..10.0, 3.0))),
            )
        }
        composeRule.onNode(hasSetTextAction()).performTextInput("8.2 units")
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("number", QuestionnaireAnswer.Number(7.0)) }
    }

    @Test
    public fun compactDateInputBlocksInvalidRequiredEditsAndAcceptsAValidDay() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(QuestionnaireQuestion("date", "Date", QuestionnaireQuestionKind.Date(today..today.plusDays(10)), isRequired = true)),
            )
        }
        composeRule.onNode(hasSetTextAction()).performTextReplacement("99999999")
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performTextReplacement("06182026")
        composeRule.onNodeWithText("Submit").assertIsEnabled().performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("date", QuestionnaireAnswer.Date(today.plusDays(3))) }
    }

    @Test
    public fun editingARecordedDateDoesNotReplaceIncompleteInputWithToday() {
        var answers by mutableStateOf<Map<String, QuestionnaireAnswer>>(
            mapOf("date" to QuestionnaireAnswer.Date(today.plusDays(3))),
        )
        composeRule.setContent {
            QuestionnaireView(
                questions =
                    listOf(
                        QuestionnaireQuestion("date", "Date", QuestionnaireQuestionKind.Date(today..today.plusDays(10)), isRequired = true),
                    ),
                answers = answers,
                onAnswersChange = { answers = it },
                labels = labels,
                onSubmit = { submitted = it },
                today = today,
                modifier = Modifier.height(620.dp),
            )
        }
        composeRule.onNode(hasSetTextAction()).performTextReplacement("99999999")
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.runOnIdle { assertThat(answers).isEmpty() }
        composeRule.onNode(hasSetTextAction()).performTextReplacement("06192026")
        composeRule.onNodeWithText("Submit").assertIsEnabled().performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("date", QuestionnaireAnswer.Date(today.plusDays(4))) }
    }

    @Test
    @Config(qualifiers = "w480dp-h800dp")
    public fun selectingADateRecordsTheSelectedCalendarDay() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(QuestionnaireQuestion("date", "Date", QuestionnaireQuestionKind.Date(today..today.plusDays(10)))),
            )
        }
        composeRule.onNodeWithContentDescription("Switch to calendar input mode").performClick()
        composeRule.onNodeWithText("Thursday, June 18, 2026").performClick()
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("date", QuestionnaireAnswer.Date(today.plusDays(3))) }
    }

    @Test
    public fun untouchedDateSubmitsTheDisplayedClampedDefault() {
        composeRule.setContent {
            QuestionnaireHost(
                listOf(
                    QuestionnaireQuestion(
                        "date",
                        "Date",
                        QuestionnaireQuestionKind.Date(
                            today.plusDays(2)..today.plusDays(10),
                        ),
                        isRequired = true,
                    ),
                ),
            )
        }
        composeRule.onNodeWithText("Submit").assertIsEnabled().performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("date", QuestionnaireAnswer.Date(today.plusDays(2))) }
    }

    @Test
    public fun customRendererCanProvideOrClearAnyAnswer() {
        composeRule.setContent {
            var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }
            QuestionnaireView(
                questions = listOf(QuestionnaireQuestion("custom", "Custom", QuestionnaireQuestionKind.Custom, isRequired = true)),
                answers = answers,
                onAnswersChange = { answers = it },
                labels = labels,
                onSubmit = { submitted = it },
                customInput = { _, answer, change ->
                    Text(text = answer.toString())
                    SecondaryButton("Set answer", { change(QuestionnaireAnswer.Boolean(false)) })
                    SecondaryButton("Clear answer", { change(null) })
                },
            )
        }
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNodeWithText("Set answer").performClick()
        composeRule.onNodeWithText("Submit").assertIsEnabled()
        composeRule.onNodeWithText("Clear answer").performClick()
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
        composeRule.onNodeWithText("Set answer").performClick()
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.runOnIdle { assertThat(submitted).containsExactly("custom", QuestionnaireAnswer.Boolean(false)) }
    }

    @Test
    public fun skipDoesNotSubmitOrClearAnswers() {
        var skipped = 0
        val answers = mapOf("choice" to QuestionnaireAnswer.Choices(listOf("a")))
        composeRule.setContent {
            QuestionnaireView(
                listOf(choice()),
                answers,
                { error("Skip changed answers") },
                labels,
                { submitted = it },
                onSkip = { skipped++ },
            )
        }
        composeRule.onNodeWithText("Skip").performClick()
        composeRule.runOnIdle {
            assertThat(skipped).isEqualTo(1)
            assertThat(submitted).isNull()
        }
    }

    @Test
    public fun submissionReturnsToAnEarlierRequiredPageIfItsAnswerWasRemoved() {
        var answers by mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap())
        composeRule.setContent {
            QuestionnaireView(listOf(choice(), textQuestion()), answers, { answers = it }, labels, { submitted = it })
        }
        composeRule.onNodeWithText("Option A").performClick()
        composeRule.onNodeWithText("Next").performClick()
        composeRule.runOnIdle { answers = emptyMap() }
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.onNodeWithText("Option A").assertExists()
        composeRule.onNodeWithText("Next").assertIsNotEnabled()
        composeRule.runOnIdle { assertThat(submitted).isNull() }
    }

    @Test
    public fun emptyAndUnsupportedCustomQuestionsShowNoSubmitControl() {
        composeRule.setContent {
            QuestionnaireHost(listOf(QuestionnaireQuestion("custom", "Custom", QuestionnaireQuestionKind.Custom)))
        }
        composeRule.onNodeWithText("Submit").assertDoesNotExist()
        composeRule.onNodeWithText("Custom").assertDoesNotExist()
    }

    @Test
    public fun removingPagesClampsTheCurrentIndex() {
        var questions by mutableStateOf(listOf(choice().copy(isRequired = false), textQuestion()))
        composeRule.setContent { QuestionnaireHost(questions) }
        composeRule.onNodeWithText("Next").performClick()
        composeRule.runOnIdle { questions = listOf(choice()) }
        composeRule.onNodeWithText("Option A").assertExists()
        composeRule.onNodeWithText("Submit").assertIsNotEnabled()
    }

    @Composable
    private fun QuestionnaireHost(questions: List<QuestionnaireQuestion>) {
        var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }
        QuestionnaireView(
            questions = questions,
            answers = answers,
            onAnswersChange = { answers = it },
            labels = labels,
            onSubmit = { submitted = it },
            today = today,
            modifier = Modifier.height(620.dp),
            progressDescription = { page, count -> "Question $page of $count" },
        )
    }

    private fun choice(): QuestionnaireQuestion =
        QuestionnaireQuestion("choice", "Choose", QuestionnaireQuestionKind.SingleChoice(options), isRequired = true)

    private fun textQuestion(): QuestionnaireQuestion =
        QuestionnaireQuestion("text", "Notes", QuestionnaireQuestionKind.Text("Enter notes"))
}
