package io.github.maniramezan.compose.foundation.questionnaire

import java.time.LocalDate

/** A selectable answer with a stable [id] and a caller-localized [title]. */
public data class QuestionnaireOption(
    public val id: String,
    public val title: String,
) {
    /** Uses a unique title as the identifier. Prefer explicit IDs when localizing. */
    public constructor(title: String) : this(id = title, title = title)
}

/** Option appearance; selection cardinality always comes from the question kind. */
public enum class QuestionnaireChoiceStyle {
    /** Bullets for single/boolean choices, checkmarks for multiple choices. */
    Automatic,

    /** Full-width rows with circular indicators. */
    Bullets,

    /** Full-width rows with square indicators. */
    Checkmarks,

    /** Compact options that wrap onto additional lines. */
    Chips,
}

/** The input collected by a question. Every displayed string is caller-localized. */
public sealed interface QuestionnaireQuestionKind {
    /** Select zero or one option. Tapping the current selection clears it. */
    public data class SingleChoice(
        public val options: List<QuestionnaireOption>,
    ) : QuestionnaireQuestionKind

    /** Select any number of options, recorded in option order. */
    public data class MultipleChoice(
        public val options: List<QuestionnaireOption>,
    ) : QuestionnaireQuestionKind

    /** Explicit choices distinguish an unanswered question from a valid false answer. */
    public data class Boolean(
        public val trueTitle: String,
        public val falseTitle: String,
    ) : QuestionnaireQuestionKind

    /** Finite bounds; submission clamps and snaps to [step] measured from the lower bound. */
    public data class Number(
        public val range: ClosedFloatingPointRange<Double>,
        public val step: Double = 1.0,
        public val unitLabel: String? = null,
    ) : QuestionnaireQuestionKind

    /** A calendar day; the initial value is today clamped into [range]. */
    public data class Date(
        public val range: ClosedRange<LocalDate>,
    ) : QuestionnaireQuestionKind

    /** Multiline text, trimmed on submission. Blank text is unanswered. */
    public data class Text(
        public val placeholder: String? = null,
    ) : QuestionnaireQuestionKind

    /** Caller-rendered content may record any answer type or null while incomplete. */
    public data object Custom : QuestionnaireQuestionKind
}

/** One page, identified by [id]. IDs must be unique within a questionnaire. */
public data class QuestionnaireQuestion(
    public val id: String,
    public val prompt: String,
    public val kind: QuestionnaireQuestionKind,
    public val detail: String? = null,
    public val isRequired: Boolean = false,
    public val choiceStyle: QuestionnaireChoiceStyle = QuestionnaireChoiceStyle.Automatic,
)

/** A recorded answer. An absent entry in the answer map means unanswered. */
public sealed interface QuestionnaireAnswer {
    /** Option identifiers; custom inputs preserve their supplied order, including rankings. */
    public data class Choices(
        public val ids: List<String>,
    ) : QuestionnaireAnswer

    /** Both true and false count as answered. */
    public data class Boolean(
        public val value: kotlin.Boolean,
    ) : QuestionnaireAnswer

    /** A finite numeric value. */
    public data class Number(
        public val value: Double,
    ) : QuestionnaireAnswer

    /** A calendar day without time or time-zone ambiguity. */
    public data class Date(
        public val value: LocalDate,
    ) : QuestionnaireAnswer

    /** Free-form text. */
    public data class Text(
        public val value: String,
    ) : QuestionnaireAnswer
}
