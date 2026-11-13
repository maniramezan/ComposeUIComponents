package io.github.maniramezan.compose.sample

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/** Caller-owned calendar that immediately records its displayed date, including its default. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuestionnaireSampleCalendar(
    label: String,
    range: ClosedRange<LocalDate>,
    answer: QuestionnaireAnswer?,
    onAnswerChange: (QuestionnaireAnswer?) -> Unit,
) {
    val displayed = ((answer as? QuestionnaireAnswer.Date)?.value ?: range.start).coerceIn(range)
    val onChange by rememberUpdatedState(onAnswerChange)
    key(range) {
        val state =
            rememberDatePickerState(
                initialSelectedDateMillis = displayed.utcMillis(),
                yearRange = range.start.year..range.endInclusive.year,
                selectableDates =
                    object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis.calendarDay() in range

                        override fun isSelectableYear(year: Int): Boolean = year in range.start.year..range.endInclusive.year
                    },
            )
        LaunchedEffect(state) {
            snapshotFlow { state.selectedDateMillis }.drop(1).collect { millis ->
                onChange(millis?.let { QuestionnaireAnswer.Date(it.calendarDay()) })
            }
        }
        LaunchedEffect(answer) {
            val normalized = QuestionnaireAnswer.Date(displayed)
            if (answer != normalized) onChange(normalized)
            if (state.selectedDateMillis != displayed.utcMillis()) {
                state.selectedDateMillis = displayed.utcMillis()
                state.displayedMonthMillis = displayed.utcMillis()
            }
        }
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val calendarWidth = maxOf(maxWidth, 360.dp)
            val isNarrow = maxWidth < calendarWidth
            val scrollState = rememberScrollState()
            val scope = rememberCoroutineScope()
            Column {
                Box(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
                    DatePicker(
                        state = state,
                        title = null,
                        showModeToggle = false,
                        modifier = Modifier.width(calendarWidth).semantics { contentDescription = label },
                    )
                }
                if (isNarrow) {
                    val showsFirstWeekdays by remember { derivedStateOf { scrollState.value == 0 } }
                    SecondaryButton(
                        text = if (showsFirstWeekdays) "Show later weekdays" else "Show earlier weekdays",
                        onClick = {
                            scope.launch { scrollState.animateScrollTo(if (showsFirstWeekdays) scrollState.maxValue else 0) }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

private fun LocalDate.utcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.calendarDay(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
