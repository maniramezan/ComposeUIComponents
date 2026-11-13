package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireFlow
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import java.time.LocalDate

/** Caller-localized navigation, optional caption, and numeric stepper accessibility labels. */
public data class QuestionnaireLabels(
    public val back: String,
    public val next: String,
    public val submit: String,
    public val increment: String,
    public val decrement: String,
    public val skip: String? = null,
    public val optional: String? = null,
)

/**
 * A paged questionnaire with caller-owned [answers]. Each change emits a new map through
 * [onAnswersChange]; submission emits normalized answers after validating every required page.
 * Empty questionnaires show no controls. Skip invokes [onSkip] without submitting or clearing.
 *
 * [customInput] receives the question, its current answer, and an answer setter accepting null
 * while incomplete. Without a renderer, custom questions are omitted. Invalid questions and
 * duplicate IDs are filtered by [QuestionnaireFlow.validQuestions].
 *
 * Supply a bounded height to scroll the input while keeping navigation visible. In an
 * unbounded scrolling parent this view expands to its content. The host owns system/IME insets.
 * [progressDescription] receives a one-based page number and total; return localized text.
 * [today] controls the displayed date default and can be fixed for deterministic previews.
 */
@Composable
public fun QuestionnaireView(
    questions: List<QuestionnaireQuestion>,
    answers: Map<String, QuestionnaireAnswer>,
    onAnswersChange: (Map<String, QuestionnaireAnswer>) -> Unit,
    labels: QuestionnaireLabels,
    onSubmit: (Map<String, QuestionnaireAnswer>) -> Unit,
    modifier: Modifier = Modifier,
    onSkip: (() -> Unit)? = null,
    progressDescription: ((Int, Int) -> String)? = null,
    today: LocalDate = LocalDate.now(),
    customInput: (@Composable (QuestionnaireQuestion, QuestionnaireAnswer?, (QuestionnaireAnswer?) -> Unit) -> Unit)? = null,
) {
    val validQuestions = QuestionnaireFlow.validQuestions(questions, supportsCustomInput = customInput != null)
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val index = currentIndex.coerceIn(0, (validQuestions.size - 1).coerceAtLeast(0))
    val question = validQuestions.getOrNull(index) ?: return
    val focusManager = LocalFocusManager.current
    var inputIsValid by rememberSaveable(question) { mutableStateOf(true) }
    val canAdvance = QuestionnaireFlow.canAdvance(question, answers[question.id], today) && (!question.isRequired || inputIsValid)
    val setAnswer: (QuestionnaireAnswer?) -> Unit = { answer ->
        inputIsValid = question.kind !is QuestionnaireQuestionKind.Date || answer != null
        onAnswersChange(if (answer == null) answers - question.id else answers + (question.id to answer))
    }
    val advance = {
        focusManager.clearFocus()
        if (canAdvance) {
            val effective = QuestionnaireFlow.effectiveAnswer(answers[question.id], question, today)
            val updated = if (effective == null) answers - question.id else answers + (question.id to effective)
            if (index < validQuestions.lastIndex) {
                onAnswersChange(updated)
                currentIndex = index + 1
            } else {
                val unanswered = QuestionnaireFlow.firstUnansweredRequiredIndex(validQuestions, updated, today)
                if (unanswered != null) {
                    currentIndex = unanswered
                } else {
                    val submitted = QuestionnaireFlow.submission(validQuestions, updated, today)
                    onAnswersChange(submitted)
                    onSubmit(submitted)
                }
            }
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val boundedHeight = constraints.hasBoundedHeight
        Column(
            modifier = Modifier.padding(AppTheme.spacing.x2),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
        ) {
            val progressText = progressDescription?.invoke(index + 1, validQuestions.size)?.takeIf { it.isNotBlank() }
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.half)) {
                if (progressText != null) {
                    Text(
                        text = progressText,
                        style = AppTheme.typography.labelMedium,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    )
                }
                LinearProgressIndicator(
                    progress = { (index + 1).toFloat() / validQuestions.size },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            key(question) {
                val pageModifier =
                    if (boundedHeight) {
                        Modifier.weight(1f).verticalScroll(rememberScrollState())
                    } else {
                        Modifier
                    }
                Column(
                    modifier = pageModifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
                ) {
                    QuestionnaireHeading(question, labels.optional)
                    QuestionnaireInput(question, answers[question.id], setAnswer, labels, today, customInput)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x1)) {
                if (index > 0) {
                    SecondaryButton(
                        text = labels.back,
                        onClick = {
                            focusManager.clearFocus()
                            currentIndex = index - 1
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                PrimaryButton(
                    text = if (index == validQuestions.lastIndex) labels.submit else labels.next,
                    onClick = advance,
                    enabled = canAdvance,
                    modifier = Modifier.fillMaxWidth(),
                )
                val skipLabel = labels.skip?.takeIf { it.isNotBlank() }
                if (onSkip != null && skipLabel != null) {
                    TextButton(text = skipLabel, onClick = onSkip, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun QuestionnaireHeading(
    question: QuestionnaireQuestion,
    optionalLabel: String?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.half)) {
        Text(
            text = question.prompt,
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colors.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        question.detail?.takeIf { it.isNotBlank() }?.let {
            Text(text = it, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.onSurfaceVariant)
        }
        if (!question.isRequired) {
            optionalLabel?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = AppTheme.typography.labelSmall, color = AppTheme.colors.onSurfaceVariant)
            }
        }
    }
}
