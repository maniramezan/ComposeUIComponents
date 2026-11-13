package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.MenuPicker
import io.github.maniramezan.compose.components.QuestionnaireLabels
import io.github.maniramezan.compose.components.QuestionnaireView
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.Slider
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import java.time.LocalDate

private val QUESTIONNAIRE_EXAMPLES = listOf("Single", "Multiple", "Boolean", "Number", "Date", "Text", "Custom", "All")

@Composable
internal fun QuestionnairePage() {
    var example by remember { mutableIntStateOf(0) }
    var style by remember { mutableIntStateOf(0) }
    var required by remember { mutableStateOf(true) }
    var detail by remember { mutableStateOf(true) }
    var skip by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(true) }
    var optionalCaption by remember { mutableStateOf(true) }
    var customRenderer by remember { mutableStateOf(true) }
    var prompt by remember { mutableStateOf("Choose an answer") }
    var optionTitles by remember { mutableStateOf("Option A, Option B, Option C") }
    var trueTitle by remember { mutableStateOf("Yes") }
    var falseTitle by remember { mutableStateOf("No") }
    var placeholder by remember { mutableStateOf("Write your answer") }
    var unit by remember { mutableStateOf("items") }
    var submit by remember { mutableStateOf("Done") }
    var lower by remember { mutableStateOf(0f) }
    var upper by remember { mutableStateOf(100f) }
    var step by remember { mutableStateOf(5f) }
    var days by remember { mutableStateOf(90f) }
    var generation by remember { mutableIntStateOf(0) }
    var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }
    var event by remember { mutableStateOf("No event yet") }
    val today = remember { LocalDate.now() }
    val options =
        optionTitles
            .split(',')
            .map(String::trim)
            .filter(String::isNotBlank)
            .map(::QuestionnaireOption)
    val kinds =
        listOf(
            QuestionnaireQuestionKind.SingleChoice(options),
            QuestionnaireQuestionKind.MultipleChoice(options),
            QuestionnaireQuestionKind.Boolean(trueTitle, falseTitle),
            QuestionnaireQuestionKind.Number(lower.toDouble()..upper.toDouble(), step.toDouble(), unit),
            QuestionnaireQuestionKind.Date(today..today.plusDays(days.toLong())),
            QuestionnaireQuestionKind.Text(placeholder),
            QuestionnaireQuestionKind.Custom,
        )
    val questions =
        kinds
            .mapIndexed { index, kind ->
                QuestionnaireQuestion(
                    id = "question-$index",
                    prompt = if (example == 7) "${QUESTIONNAIRE_EXAMPLES[index]}: $prompt" else prompt,
                    kind = kind,
                    detail = if (detail) "Provide the value that fits best." else null,
                    isRequired = required,
                    choiceStyle = QuestionnaireChoiceStyle.entries[style],
                )
            }.filterIndexed { index, _ -> example == 7 || index == example }

    SamplePage(
        preview = {
            key(example, generation) {
                QuestionnaireView(
                    questions = questions,
                    answers = answers,
                    onAnswersChange = { answers = it },
                    labels =
                        QuestionnaireLabels(
                            back = "Back",
                            next = "Next",
                            submit = submit,
                            increment = "Increase value",
                            decrement = "Decrease value",
                            skip = "Skip",
                            optional = if (optionalCaption) "Optional" else null,
                        ),
                    onSubmit = { event = "Submitted ${it.size} answers: $it" },
                    onSkip = if (skip) ({ event = "Skipped" }) else null,
                    progressDescription = if (progress) ({ page, count -> "Question $page of $count" }) else null,
                    today = today,
                    modifier = Modifier.height(580.dp),
                    customInput =
                        if (customRenderer) {
                            { question, answer, onChange ->
                                val rating = (answer as? QuestionnaireAnswer.Number)?.value
                                Text(text = rating?.let { "Rating: ${it.toInt()}" } ?: "Move the slider to answer")
                                Slider(
                                    value = rating?.toFloat() ?: 3f,
                                    onValueChange = { onChange(QuestionnaireAnswer.Number(it.toInt().toDouble())) },
                                    valueRange = 1f..5f,
                                    label = question.prompt,
                                    valueDescription = rating?.let { "${it.toInt()} of 5" },
                                )
                                SecondaryButton(text = "Clear rating", onClick = { onChange(null) })
                            }
                        } else {
                            null
                        },
                )
            }
            Text(text = event)
            Text(text = "Answers: $answers")
        },
        controls = {
            MenuPicker(
                options = QUESTIONNAIRE_EXAMPLES.indices.toList(),
                selectedOption = example,
                onOptionSelected = { example = it },
                optionLabel = { QUESTIONNAIRE_EXAMPLES[it] },
                label = "Example",
            )
            ControlSegmented("Choice appearance", QuestionnaireChoiceStyle.entries.map { it.name }, style) { style = it }
            ControlSwitch("Required answers", required) { required = it }
            ControlSwitch("Supporting text", detail) { detail = it }
            ControlSwitch("Allow skip", skip) { skip = it }
            ControlSwitch("Progress caption", progress) { progress = it }
            ControlSwitch("Optional caption", optionalCaption) { optionalCaption = it }
            ControlSwitch("Custom renderer", customRenderer) { customRenderer = it }
            TextField(prompt, { prompt = it }, "Prompt")
            TextField(optionTitles, { optionTitles = it }, "Options separated by commas")
            TextField(trueTitle, { trueTitle = it }, "True label")
            TextField(falseTitle, { falseTitle = it }, "False label")
            TextField(placeholder, { placeholder = it }, "Text placeholder")
            TextField(unit, { unit = it }, "Number unit")
            TextField(submit, { submit = it }, "Submit label")
            ControlSlider("Lower bound: ${lower.toInt()}", lower, { lower = it }, -100f..0f)
            ControlSlider("Upper bound: ${upper.toInt()}", upper, { upper = it }, 0f..200f)
            ControlSlider("Step: ${step.toInt()}", step, { step = it.toInt().toFloat() }, 0f..20f)
            ControlSlider("Date range in days: ${days.toInt()}", days, { days = it }, 0f..365f)
            SecondaryButton(
                text = "Reset answers and page",
                onClick = {
                    answers = emptyMap()
                    event = "Reset"
                    generation++
                },
            )
        },
    )
}
