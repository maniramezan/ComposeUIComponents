package io.github.maniramezan.compose.foundation.questionnaire

import java.time.LocalDate
import kotlin.math.floor

/** Pure validation and normalization shared by questionnaire UIs and consumers. */
public object QuestionnaireFlow {
    /**
     * Keeps the first question/option with each ID. Drops empty choices, invalid ranges,
     * and custom questions without a renderer. Input lists are never mutated.
     */
    public fun validQuestions(
        questions: List<QuestionnaireQuestion>,
        supportsCustomInput: Boolean = false,
    ): List<QuestionnaireQuestion> =
        questions.distinctBy { it.id }.mapNotNull { question ->
            val kind = question.kind
            when (kind) {
                is QuestionnaireQuestionKind.SingleChoice ->
                    kind.options.distinctBy { it.id }.takeIf { it.isNotEmpty() }?.let {
                        question.copy(kind = kind.copy(options = it))
                    }
                is QuestionnaireQuestionKind.MultipleChoice ->
                    kind.options.distinctBy { it.id }.takeIf { it.isNotEmpty() }?.let {
                        question.copy(kind = kind.copy(options = it))
                    }
                is QuestionnaireQuestionKind.Number ->
                    question.takeIf { kind.range.start.isFinite() && kind.range.endInclusive.isFinite() && !kind.range.isEmpty() }
                is QuestionnaireQuestionKind.Date ->
                    question.takeIf { !kind.range.isEmpty() && kind.range.start.year >= 1 && kind.range.endInclusive.year <= 9999 }
                QuestionnaireQuestionKind.Custom -> question.takeIf { supportsCustomInput }
                else -> question
            }
        }

    /** Cleans an answer, returning null for empty, non-finite, or mismatched values. */
    public fun normalized(
        answer: QuestionnaireAnswer?,
        question: QuestionnaireQuestion,
    ): QuestionnaireAnswer? =
        when (val kind = question.kind) {
            is QuestionnaireQuestionKind.SingleChoice ->
                (answer as? QuestionnaireAnswer.Choices)
                    ?.ids
                    ?.firstOrNull { id -> kind.options.any { it.id == id } }
                    ?.let { QuestionnaireAnswer.Choices(listOf(it)) }
            is QuestionnaireQuestionKind.MultipleChoice ->
                (answer as? QuestionnaireAnswer.Choices)?.let { selection ->
                    kind.options
                        .map { it.id }
                        .distinct()
                        .filter { it in selection.ids }
                        .takeIf { it.isNotEmpty() }
                        ?.let(QuestionnaireAnswer::Choices)
                }
            is QuestionnaireQuestionKind.Boolean -> answer as? QuestionnaireAnswer.Boolean
            is QuestionnaireQuestionKind.Number ->
                (answer as? QuestionnaireAnswer.Number)
                    ?.value
                    ?.takeIf { it.isFinite() }
                    ?.let { QuestionnaireAnswer.Number(snapped(it, kind.range, kind.step)) }
            is QuestionnaireQuestionKind.Date ->
                (answer as? QuestionnaireAnswer.Date)?.let { QuestionnaireAnswer.Date(it.value.coerceIn(kind.range)) }
            is QuestionnaireQuestionKind.Text -> normalizedText(answer)
            QuestionnaireQuestionKind.Custom ->
                when (answer) {
                    is QuestionnaireAnswer.Text -> normalizedText(answer)
                    is QuestionnaireAnswer.Number -> answer.takeIf { it.value.isFinite() }
                    is QuestionnaireAnswer.Choices ->
                        answer.ids
                            .distinct()
                            .takeIf { it.isNotEmpty() }
                            ?.let(QuestionnaireAnswer::Choices)
                    else -> answer
                }
        }

    /** Uses the displayed date default if no valid answer is stored. */
    public fun effectiveAnswer(
        answer: QuestionnaireAnswer?,
        question: QuestionnaireQuestion,
        today: LocalDate,
    ): QuestionnaireAnswer? =
        normalized(answer, question) ?: (question.kind as? QuestionnaireQuestionKind.Date)?.let {
            QuestionnaireAnswer.Date(today.coerceIn(it.range))
        }

    /** Optional questions can always advance. Required dates include their displayed default. */
    public fun canAdvance(
        question: QuestionnaireQuestion,
        answer: QuestionnaireAnswer?,
        today: LocalDate,
    ): Boolean = !question.isRequired || effectiveAnswer(answer, question, today) != null

    /** Returns the first required page needing attention, or null when submission is valid. */
    public fun firstUnansweredRequiredIndex(
        questions: List<QuestionnaireQuestion>,
        answers: Map<String, QuestionnaireAnswer>,
        today: LocalDate,
    ): Int? = questions.indexOfFirst { !canAdvance(it, answers[it.id], today) }.takeIf { it >= 0 }

    /** Removes unknown/invalid answers and includes displayed date defaults. */
    public fun submission(
        questions: List<QuestionnaireQuestion>,
        answers: Map<String, QuestionnaireAnswer>,
        today: LocalDate,
    ): Map<String, QuestionnaireAnswer> =
        questions
            .mapNotNull { question ->
                effectiveAnswer(answers[question.id], question, today)?.let { question.id to it }
            }.toMap()

    /** Toggles a choice and preserves option order for multiple selection. */
    public fun toggling(
        optionId: String,
        answer: QuestionnaireAnswer?,
        options: List<QuestionnaireOption>,
        allowsMultiple: Boolean,
    ): QuestionnaireAnswer? {
        if (options.none { it.id == optionId }) return answer
        val selected = (answer as? QuestionnaireAnswer.Choices)?.ids.orEmpty().toSet()
        val ids =
            if (allowsMultiple) {
                val updated = if (optionId in selected) selected - optionId else selected + optionId
                options.map { it.id }.distinct().filter { it in updated }
            } else {
                if (selected == setOf(optionId)) emptyList() else listOf(optionId)
            }
        return ids.takeIf { it.isNotEmpty() }?.let(QuestionnaireAnswer::Choices)
    }

    /** Clamps and snaps from the lower bound. Invalid steps only clamp. */
    public fun snapped(
        value: Double,
        range: ClosedFloatingPointRange<Double>,
        step: Double,
    ): Double {
        val clamped = value.coerceIn(range)
        if (!step.isFinite() || step <= 0) return clamped
        val steps = floor((clamped - range.start) / step + 0.5)
        val snapped = range.start + steps * step
        return if (snapped.isFinite()) snapped.coerceIn(range) else clamped
    }

    /** An empty field starts at the lower bound. Invalid steps advance by one. */
    public fun stepped(
        value: Double?,
        increment: Boolean,
        range: ClosedFloatingPointRange<Double>,
        step: Double,
    ): Double {
        if (value == null || !value.isFinite()) return range.start
        val delta = step.takeIf { it.isFinite() && it > 0 } ?: 1.0
        return (value + if (increment) delta else -delta).coerceIn(range)
    }

    private fun normalizedText(answer: QuestionnaireAnswer?): QuestionnaireAnswer.Text? =
        (answer as? QuestionnaireAnswer.Text)
            ?.value
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?.let(QuestionnaireAnswer::Text)
}
