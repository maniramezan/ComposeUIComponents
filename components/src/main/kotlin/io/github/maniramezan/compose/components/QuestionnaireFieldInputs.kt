package io.github.maniramezan.compose.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireFlow
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireNumbers
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.flow.drop
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
internal fun QuestionnaireTextInput(
    prompt: String,
    placeholder: String?,
    answer: QuestionnaireAnswer?,
    onAnswerChange: (QuestionnaireAnswer?) -> Unit,
) {
    OutlinedTextField(
        value = (answer as? QuestionnaireAnswer.Text)?.value.orEmpty(),
        onValueChange = { onAnswerChange(it.takeIf(String::isNotEmpty)?.let(QuestionnaireAnswer::Text)) },
        label = { Text(prompt) },
        placeholder = placeholder?.takeIf { it.isNotBlank() }?.let { { Text(it) } },
        minLines = 3,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun QuestionnaireNumberInput(
    question: QuestionnaireQuestion,
    kind: QuestionnaireQuestionKind.Number,
    answer: QuestionnaireAnswer?,
    onAnswerChange: (QuestionnaireAnswer?) -> Unit,
    labels: QuestionnaireLabels,
) {
    val locale = LocalConfiguration.current.locales[0]
    val value = (answer as? QuestionnaireAnswer.Number)?.value?.takeIf { it.isFinite() }
    var text by rememberSaveable(question.id) { mutableStateOf(value?.let { QuestionnaireNumbers.format(it, locale) }.orEmpty()) }
    LaunchedEffect(answer, locale) {
        if (QuestionnaireNumbers.parse(text, locale) != value) {
            text = value?.let { QuestionnaireNumbers.format(it, locale) }.orEmpty()
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = QuestionnaireNumbers.sanitize(it, locale, allowsNegative = kind.range.start < 0)
                onAnswerChange(QuestionnaireNumbers.parse(text, locale)?.let(QuestionnaireAnswer::Number))
            },
            label = { Text(question.prompt) },
            suffix = kind.unitLabel?.takeIf { it.isNotBlank() }?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
            SecondaryButton(
                text = "−",
                onClick = {
                    val updated = QuestionnaireFlow.stepped(value, false, kind.range, kind.step)
                    text = QuestionnaireNumbers.format(updated, locale)
                    onAnswerChange(QuestionnaireAnswer.Number(updated))
                },
                modifier = Modifier.weight(1f).semantics { contentDescription = labels.decrement },
                enabled = value == null || value > kind.range.start,
            )
            SecondaryButton(
                text = "+",
                onClick = {
                    val updated = QuestionnaireFlow.stepped(value, true, kind.range, kind.step)
                    text = QuestionnaireNumbers.format(updated, locale)
                    onAnswerChange(QuestionnaireAnswer.Number(updated))
                },
                modifier = Modifier.weight(1f).semantics { contentDescription = labels.increment },
                enabled = value == null || value < kind.range.endInclusive,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuestionnaireDateInput(
    question: QuestionnaireQuestion,
    kind: QuestionnaireQuestionKind.Date,
    answer: QuestionnaireAnswer?,
    onAnswerChange: (QuestionnaireAnswer?) -> Unit,
    today: LocalDate,
) {
    val displayed = (QuestionnaireFlow.effectiveAnswer(answer, question, today) as QuestionnaireAnswer.Date).value
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val calendarWidth = minimumTouchTargetSize() * 7 + AppTheme.spacing.x3
        val viewportWidth = maxWidth
        key(kind.range) {
            val state =
                rememberDatePickerState(
                    initialSelectedDateMillis = displayed.utcMillis(),
                    initialDisplayMode = DisplayMode.Input,
                    yearRange = kind.range.start.year..kind.range.endInclusive.year,
                    selectableDates =
                        object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                                Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC).toLocalDate() in kind.range

                            override fun isSelectableYear(year: Int): Boolean = year in kind.range.start.year..kind.range.endInclusive.year
                        },
                )
            val onChange by rememberUpdatedState(onAnswerChange)
            val currentToday by rememberUpdatedState(today)
            var synchronizedInput by remember(state) { mutableStateOf(answer to today) }
            LaunchedEffect(state) {
                snapshotFlow { state.selectedDateMillis }.drop(1).collect { millis ->
                    val selected = millis?.let { QuestionnaireAnswer.Date(Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()) }
                    synchronizedInput = selected to currentToday
                    onChange(selected)
                }
            }
            LaunchedEffect(answer, today) {
                if (synchronizedInput != answer to today) {
                    synchronizedInput = answer to today
                    state.selectedDateMillis = displayed.utcMillis()
                    state.displayedMonthMillis = displayed.utcMillis()
                }
            }
            val pickerWidth = if (state.displayMode == DisplayMode.Picker) maxOf(calendarWidth, viewportWidth) else viewportWidth
            Box(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                DatePicker(
                    state = state,
                    title = null,
                    showModeToggle = true,
                    modifier = Modifier.width(pickerWidth),
                )
            }
        }
    }
}

private fun LocalDate.utcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
