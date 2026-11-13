package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.Card
import io.github.maniramezan.compose.components.MenuPicker
import io.github.maniramezan.compose.components.QuestionnaireLabels
import io.github.maniramezan.compose.components.QuestionnaireView
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.Section
import io.github.maniramezan.compose.components.TextField
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import java.time.LocalDate

@Composable
internal fun QuestionnairePage(
    initialExample: QuestionnaireExample = QuestionnaireExample.SingleChoice,
    today: LocalDate = LocalDate.now(),
) {
    var example by remember { mutableStateOf(initialExample) }
    var configuration by remember { mutableStateOf(QuestionnaireExampleConfiguration()) }
    var skip by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(true) }
    var optionalCaption by remember { mutableStateOf(true) }
    var customRenderer by remember { mutableStateOf(true) }
    var submit by remember { mutableStateOf("") }
    var generation by remember { mutableIntStateOf(0) }

    key(example, generation) {
        var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }
        var event by remember { mutableStateOf("None yet") }
        SamplePage(
            preview = {
                Section("Questionnaire example") {
                    MenuPicker(
                        options = QuestionnaireExample.entries,
                        selectedOption = example,
                        onOptionSelected = { example = it },
                        optionLabel = { it.title },
                        label = "Example",
                    )
                    Card(contentPadding = PaddingValues(0.dp)) {
                        QuestionnaireView(
                            questions = configuration.questions(example, today),
                            answers = answers,
                            onAnswersChange = { answers = it },
                            labels =
                                QuestionnaireLabels(
                                    back = "Back",
                                    next = "Next",
                                    submit = submit.ifBlank { "Done" },
                                    increment = "Increase value",
                                    decrement = "Decrease value",
                                    skip = "Skip",
                                    optional = if (optionalCaption) "Optional" else null,
                                ),
                            onSubmit = { event = "Submitted ${it.size} answer(s)" },
                            onSkip = if (skip) ({ event = "Skipped" }) else null,
                            progressDescription = if (progress) ({ page, count -> "Question $page of $count" }) else null,
                            today = today,
                            modifier = Modifier.height(580.dp),
                            customInput =
                                if (customRenderer) {
                                    { question, answer, onChange ->
                                        QuestionnaireSampleCalendar(question.prompt, configuration.dateRange(today), answer, onChange)
                                    }
                                } else {
                                    null
                                },
                        )
                    }
                    Text(text = "Last event: $event", modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                }
            },
            controls = {
                Section("Question and presentation") {
                    TextField(
                        configuration.prompt,
                        { configuration = configuration.copy(prompt = it) },
                        "Prompt (empty uses example prompt)",
                    )
                    ControlSwitch("Required answers", configuration.required) { configuration = configuration.copy(required = it) }
                    ControlSwitch("Detail text", configuration.showsDetail) { configuration = configuration.copy(showsDetail = it) }
                    MenuPicker(
                        options = QuestionnaireChoiceStyle.entries,
                        selectedOption = configuration.choiceStyle,
                        onOptionSelected = { configuration = configuration.copy(choiceStyle = it) },
                        optionLabel = {
                            when (it) {
                                QuestionnaireChoiceStyle.Automatic -> "Automatic rows"
                                QuestionnaireChoiceStyle.Bullets -> "Bullet rows"
                                QuestionnaireChoiceStyle.Checkmarks -> "Checkmark rows"
                                QuestionnaireChoiceStyle.Chips -> "Wrapping chips"
                            }
                        },
                        label = "Choice presentation",
                    )
                    Text("Presentation changes the indicators. The question kind controls single or multiple selection.")
                    OutlinedTextField(
                        value = configuration.optionTitles,
                        onValueChange = { configuration = configuration.copy(optionTitles = it) },
                        label = { Text("Options (one per line)") },
                        minLines = 3,
                        maxLines = 6,
                    )
                    TextField(configuration.placeholder, { configuration = configuration.copy(placeholder = it) }, "Free-text placeholder")
                    TextField(configuration.trueTitle, { configuration = configuration.copy(trueTitle = it) }, "True choice title")
                    TextField(configuration.falseTitle, { configuration = configuration.copy(falseTitle = it) }, "False choice title")
                }
                Section("Number and date") {
                    ControlSlider(
                        "Lower bound: ${configuration.lowerBound.toInt()}",
                        configuration.lowerBound.toFloat(),
                        { configuration = configuration.copy(lowerBound = it.toInt().toDouble()) },
                        -100f..configuration.upperBound.toFloat(),
                    )
                    ControlSlider(
                        "Upper bound: ${configuration.upperBound.toInt()}",
                        configuration.upperBound.toFloat(),
                        { configuration = configuration.copy(upperBound = it.toInt().toDouble()) },
                        configuration.lowerBound.toFloat()..1000f,
                    )
                    ControlSlider(
                        "Number step: ${configuration.step.toInt()}",
                        configuration.step.toFloat(),
                        { configuration = configuration.copy(step = it.toInt().toDouble()) },
                        0f..100f,
                    )
                    TextField(configuration.unit, { configuration = configuration.copy(unit = it) }, "Number unit")
                    ControlSlider(
                        "Date range: ${configuration.dateRangeMonths} month(s)",
                        configuration.dateRangeMonths.toFloat(),
                        { configuration = configuration.copy(dateRangeMonths = it.toInt()) },
                        1f..24f,
                    )
                }
                Section("Navigation and answers") {
                    ControlSwitch("Skip button", skip) { skip = it }
                    ControlSwitch("Progress caption", progress) { progress = it }
                    ControlSwitch("Optional caption", optionalCaption) { optionalCaption = it }
                    ControlSwitch("Custom renderer", customRenderer) { customRenderer = it }
                    TextField(submit, { submit = it }, "Submit title (empty uses Done)")
                    SecondaryButton(text = "Restart", onClick = { generation++ })
                    Text(
                        text =
                            if (answers.isEmpty()) {
                                "No answers"
                            } else {
                                answers.toSortedMap().entries.joinToString("\n") { (id, answer) -> "$id: $answer" }
                            },
                    )
                }
            },
        )
    }
}
