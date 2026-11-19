package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireFlow
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import java.time.LocalDate

@Composable
internal fun QuestionnaireInput(
    question: QuestionnaireQuestion,
    answer: QuestionnaireAnswer?,
    onAnswerChange: (QuestionnaireAnswer?) -> Unit,
    labels: QuestionnaireLabels,
    today: LocalDate,
    customInput: (@Composable (QuestionnaireQuestion, QuestionnaireAnswer?, (QuestionnaireAnswer?) -> Unit) -> Unit)?,
) {
    when (val kind = question.kind) {
        is QuestionnaireQuestionKind.SingleChoice ->
            QuestionnaireChoices(kind.options, false, question.choiceStyle, answer, onAnswerChange)
        is QuestionnaireQuestionKind.MultipleChoice ->
            QuestionnaireChoices(kind.options, true, question.choiceStyle, answer, onAnswerChange)
        is QuestionnaireQuestionKind.Boolean -> {
            val selection =
                (answer as? QuestionnaireAnswer.Boolean)?.let {
                    QuestionnaireAnswer.Choices(listOf(it.value.toString()))
                }
            QuestionnaireChoices(
                listOf(QuestionnaireOption("true", kind.trueTitle), QuestionnaireOption("false", kind.falseTitle)),
                false,
                question.choiceStyle,
                selection,
            ) { selected ->
                val id = (selected as? QuestionnaireAnswer.Choices)?.ids?.firstOrNull()
                onAnswerChange(id?.let { QuestionnaireAnswer.Boolean(it == "true") })
            }
        }
        is QuestionnaireQuestionKind.Number -> QuestionnaireNumberInput(question, kind, answer, onAnswerChange, labels)
        is QuestionnaireQuestionKind.Date -> QuestionnaireDateInput(question, kind, answer, onAnswerChange, today)
        is QuestionnaireQuestionKind.Text -> QuestionnaireTextInput(question.prompt, kind.placeholder, answer, onAnswerChange)
        QuestionnaireQuestionKind.Custom ->
            CompositionLocalProvider(LocalContentColor provides AppTheme.colors.onSurface) {
                customInput?.invoke(question, answer, onAnswerChange)
            }
    }
}

@Composable
private fun QuestionnaireChoices(
    options: List<QuestionnaireOption>,
    allowsMultiple: Boolean,
    style: QuestionnaireChoiceStyle,
    answer: QuestionnaireAnswer?,
    onAnswerChange: (QuestionnaireAnswer?) -> Unit,
) {
    val selected = (answer as? QuestionnaireAnswer.Choices)?.ids.orEmpty()
    val resolvedStyle =
        if (style == QuestionnaireChoiceStyle.Automatic) {
            if (allowsMultiple) QuestionnaireChoiceStyle.Checkmarks else QuestionnaireChoiceStyle.Bullets
        } else {
            style
        }
    val groupModifier = if (allowsMultiple) Modifier else Modifier.selectableGroup()
    val optionContent: @Composable (QuestionnaireOption) -> Unit = { option ->
        val isSelected = option.id in selected
        val onClick = {
            onAnswerChange(QuestionnaireFlow.toggling(option.id, answer, options, allowsMultiple))
        }
        if (resolvedStyle == QuestionnaireChoiceStyle.Chips) {
            PillChip(label = option.title, isSelected = isSelected, onClick = onClick)
        } else {
            SelectableCard(
                selected = isSelected,
                onClick = onClick,
                role = if (allowsMultiple) Role.Checkbox else Role.RadioButton,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(AppTheme.spacing.x1),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1),
                ) {
                    if (resolvedStyle == QuestionnaireChoiceStyle.Checkmarks) {
                        Checkbox(checked = isSelected, onCheckedChange = null, modifier = Modifier.clearAndSetSemantics {})
                    } else {
                        RadioButton(selected = isSelected, onClick = null, modifier = Modifier.clearAndSetSemantics {})
                    }
                    androidx.compose.material3.Text(text = option.title, style = AppTheme.typography.bodyLarge)
                }
            }
        }
    }
    if (resolvedStyle == QuestionnaireChoiceStyle.Chips) {
        FlowLayout(modifier = groupModifier) { options.forEach { optionContent(it) } }
    } else {
        Column(modifier = groupModifier, verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
            options.forEach { optionContent(it) }
        }
    }
}
