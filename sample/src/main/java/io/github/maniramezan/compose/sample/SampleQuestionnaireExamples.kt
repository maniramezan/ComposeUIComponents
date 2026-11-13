package io.github.maniramezan.compose.sample

import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import java.time.LocalDate

/** Independent examples also define the order of the complete questionnaire. */
internal enum class QuestionnaireExample(
    val title: String,
    val prompt: String,
    val detail: String,
) {
    SingleChoice("Single choice", "Which option do you prefer?", "Choose one option. Tap it again to clear your answer."),
    MultipleChoice("Multiple choice", "Which options apply?", "Select every option that applies."),
    FreeText("Free-form text", "What would you like to add?", "Answer in your own words. Multiple lines are supported."),
    Boolean("Boolean choices", "Would you like to include this option?", "Either choice answers the question."),
    Number("Number", "How many items?", "Enter a number or use the stepper."),
    Date("Date", "Which date do you prefer?", "The date starts at today. Use the date field or calendar to choose another day."),
    CustomComponent(
        "Custom component example",
        "Which calendar date should be recorded?",
        "An inline calendar. Choose a date, then continue.",
    ),
    RatingScale("Rating scale", "How would you rate this item?", "A labeled scale composed from a single-choice question."),
    All("Complete questionnaire", "", ""),
}

/** Sample-only configuration; the public question model stays independent of these examples. */
internal data class QuestionnaireExampleConfiguration(
    val prompt: String = "",
    val optionTitles: String = "Option A\nOption B\nOption C",
    val choiceStyle: QuestionnaireChoiceStyle = QuestionnaireChoiceStyle.Automatic,
    val required: Boolean = true,
    val showsDetail: Boolean = true,
    val placeholder: String = "Write your answer",
    val trueTitle: String = "Yes",
    val falseTitle: String = "No",
    val lowerBound: Double = 0.0,
    val upperBound: Double = 100.0,
    val step: Double = 5.0,
    val unit: String = "",
    val dateRangeMonths: Int = 6,
) {
    fun dateRange(today: LocalDate): ClosedRange<LocalDate> = today..today.plusMonths(dateRangeMonths.toLong())

    fun questions(
        example: QuestionnaireExample,
        today: LocalDate,
    ): List<QuestionnaireQuestion> {
        val options =
            optionTitles
                .lines()
                .map(String::trim)
                .filter(String::isNotBlank)
                .map(::QuestionnaireOption)
        val examples = if (example == QuestionnaireExample.All) QuestionnaireExample.entries.dropLast(1) else listOf(example)
        return examples.map { item ->
            val kind =
                when (item) {
                    QuestionnaireExample.SingleChoice, QuestionnaireExample.All -> QuestionnaireQuestionKind.SingleChoice(options)
                    QuestionnaireExample.MultipleChoice -> QuestionnaireQuestionKind.MultipleChoice(options)
                    QuestionnaireExample.FreeText -> QuestionnaireQuestionKind.Text(placeholder.takeIf(String::isNotBlank))
                    QuestionnaireExample.Boolean -> QuestionnaireQuestionKind.Boolean(trueTitle, falseTitle)
                    QuestionnaireExample.Number ->
                        QuestionnaireQuestionKind.Number(
                            lowerBound..upperBound,
                            step,
                            unit.takeIf(String::isNotBlank),
                        )
                    QuestionnaireExample.Date -> QuestionnaireQuestionKind.Date(dateRange(today))
                    QuestionnaireExample.CustomComponent -> QuestionnaireQuestionKind.Custom
                    QuestionnaireExample.RatingScale ->
                        QuestionnaireQuestionKind.SingleChoice(
                            listOf("Very low", "Low", "Moderate", "High", "Very high").mapIndexed { index, label ->
                                QuestionnaireOption("${index + 1}", "${index + 1} — $label")
                            },
                        )
                }
            QuestionnaireQuestion(
                id = item.name,
                prompt = prompt.ifBlank { item.prompt },
                kind = kind,
                detail = item.detail.takeIf { showsDetail },
                isRequired = required,
                choiceStyle = choiceStyle,
            )
        }
    }
}
