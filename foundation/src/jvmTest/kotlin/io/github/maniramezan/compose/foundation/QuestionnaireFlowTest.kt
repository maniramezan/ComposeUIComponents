package io.github.maniramezan.compose.foundation

import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireFlow
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireNumbers
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Locale

class QuestionnaireFlowTest {
    private val today = LocalDate.of(2026, 6, 15)
    private val options = listOf(QuestionnaireOption("a", "First"), QuestionnaireOption("b", "Second"))
    private val single = question(QuestionnaireQuestionKind.SingleChoice(options))

    @Test
    fun repairsDuplicatesAndDropsUnrenderableQuestionsWithoutMutatingInputs() {
        val duplicated = single.copy(kind = QuestionnaireQuestionKind.SingleChoice(options + options.first()))
        val questions =
            listOf(
                duplicated,
                single.copy(prompt = "Duplicate"),
                question(QuestionnaireQuestionKind.SingleChoice(emptyList()), "empty"),
                question(QuestionnaireQuestionKind.Number(Double.NaN..10.0), "nan"),
                question(QuestionnaireQuestionKind.Number(10.0..0.0), "reversed"),
                question(QuestionnaireQuestionKind.Number(0.0..Double.POSITIVE_INFINITY), "infinity"),
                question(QuestionnaireQuestionKind.Date(today..today.minusDays(1)), "date"),
                question(QuestionnaireQuestionKind.Custom, "custom"),
            )
        val validated = QuestionnaireFlow.validQuestions(questions)
        assertThat(validated).containsExactly(single)
        assertThat((duplicated.kind as QuestionnaireQuestionKind.SingleChoice).options).hasSize(3)
        assertThat(QuestionnaireFlow.validQuestions(questions, supportsCustomInput = true).map { it.id })
            .containsExactly("question", "custom")
            .inOrder()
    }

    @Test
    fun selectionStylesNeverChangeCardinalityOrRequiredState() {
        QuestionnaireChoiceStyle.entries.forEach { style ->
            val question = single.copy(choiceStyle = style)
            assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Choices(listOf("b", "a")), question))
                .isEqualTo(QuestionnaireAnswer.Choices(listOf("b")))
            assertThat(QuestionnaireFlow.canAdvance(question, null, today)).isFalse()
        }
    }

    @Test
    fun multipleChoicesFilterUnknownIdsAndUseOptionOrder() {
        val question = question(QuestionnaireQuestionKind.MultipleChoice(options))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Choices(listOf("unknown", "b", "b", "a")), question))
            .isEqualTo(QuestionnaireAnswer.Choices(listOf("a", "b")))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Choices(listOf("unknown")), question)).isNull()
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Text("a"), question)).isNull()
    }

    @Test
    fun selectedSingleOptionCanBeClearedAndMultipleTogglesKeepOrder() {
        val selected = QuestionnaireFlow.toggling("a", null, options, false)
        assertThat(QuestionnaireFlow.toggling("a", selected, options, false)).isNull()
        val multiple = QuestionnaireFlow.toggling("a", QuestionnaireAnswer.Choices(listOf("b")), options, true)
        assertThat(multiple).isEqualTo(QuestionnaireAnswer.Choices(listOf("a", "b")))
        assertThat(QuestionnaireFlow.toggling("a", multiple, options, true)).isEqualTo(QuestionnaireAnswer.Choices(listOf("b")))
        assertThat(QuestionnaireFlow.toggling("missing", multiple, options, true)).isEqualTo(multiple)
    }

    @Test
    fun falseBooleanIsAnsweredButMissingAndWrongTypesAreNot() {
        val question = question(QuestionnaireQuestionKind.Boolean("Yes", "No"))
        assertThat(QuestionnaireFlow.canAdvance(question, QuestionnaireAnswer.Boolean(false), today)).isTrue()
        assertThat(QuestionnaireFlow.canAdvance(question, null, today)).isFalse()
        assertThat(QuestionnaireFlow.canAdvance(question, QuestionnaireAnswer.Choices(listOf("false")), today)).isFalse()
    }

    @Test
    fun numbersClampSnapFromLowerBoundAndRejectNonFiniteAnswers() {
        val question = question(QuestionnaireQuestionKind.Number(-5.0..10.0, step = 3.0))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Number(0.0), question)).isEqualTo(QuestionnaireAnswer.Number(1.0))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Number(-100.0), question)).isEqualTo(QuestionnaireAnswer.Number(-5.0))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Number(100.0), question)).isEqualTo(QuestionnaireAnswer.Number(10.0))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Number(Double.NaN), question)).isNull()
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Number(Double.POSITIVE_INFINITY), question)).isNull()
        listOf(0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY).forEach { step ->
            assertThat(QuestionnaireFlow.snapped(2.4, 0.0..10.0, step)).isEqualTo(2.4)
            assertThat(QuestionnaireFlow.stepped(2.4, true, 0.0..10.0, step)).isWithin(0.0001).of(3.4)
        }
        assertThat(QuestionnaireFlow.stepped(null, true, -5.0..10.0, 3.0)).isEqualTo(-5.0)
        assertThat(QuestionnaireFlow.stepped(10.0, true, -5.0..10.0, 3.0)).isEqualTo(10.0)
        assertThat(QuestionnaireFlow.snapped(1.0, 0.0..10.0, 2.0)).isEqualTo(2.0)
    }

    @Test
    fun datesUseTheDisplayedDefaultAndClampStoredValues() {
        val question = question(QuestionnaireQuestionKind.Date(today.plusDays(1)..today.plusDays(10)))
        assertThat(QuestionnaireFlow.effectiveAnswer(null, question, today)).isEqualTo(QuestionnaireAnswer.Date(today.plusDays(1)))
        assertThat(QuestionnaireFlow.canAdvance(question, null, today)).isTrue()
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Date(today.plusDays(20)), question))
            .isEqualTo(QuestionnaireAnswer.Date(today.plusDays(10)))
    }

    @Test
    fun textMustContainNonWhitespaceAndSubmissionsAreTrimmed() {
        val question = question(QuestionnaireQuestionKind.Text())
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Text(" \n\t"), question)).isNull()
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Text("  first\nsecond  "), question))
            .isEqualTo(QuestionnaireAnswer.Text("first\nsecond"))
    }

    @Test
    fun customAnswersKeepRankOrderAndValidateAllAnswerCases() {
        val question = question(QuestionnaireQuestionKind.Custom)
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Choices(listOf("b", "a", "b")), question))
            .isEqualTo(QuestionnaireAnswer.Choices(listOf("b", "a")))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Boolean(false), question)).isEqualTo(QuestionnaireAnswer.Boolean(false))
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Number(Double.NaN), question)).isNull()
        assertThat(QuestionnaireFlow.normalized(QuestionnaireAnswer.Choices(emptyList()), question)).isNull()
        assertThat(
            QuestionnaireFlow.normalized(QuestionnaireAnswer.Text("  value  "), question),
        ).isEqualTo(QuestionnaireAnswer.Text("value"))
    }

    @Test
    fun finalValidationFindsEarlierMissingRequiredAnswersAndRemovesStaleKeys() {
        val questions = listOf(single, question(QuestionnaireQuestionKind.Text(), "optional").copy(isRequired = false))
        assertThat(QuestionnaireFlow.firstUnansweredRequiredIndex(questions, emptyMap(), today)).isEqualTo(0)
        val answers = mapOf("question" to QuestionnaireAnswer.Choices(listOf("b")), "stale" to QuestionnaireAnswer.Text("value"))
        assertThat(QuestionnaireFlow.firstUnansweredRequiredIndex(questions, answers, today)).isNull()
        assertThat(
            QuestionnaireFlow.submission(questions, answers, today),
        ).containsExactly("question", QuestionnaireAnswer.Choices(listOf("b")))
        assertThat(QuestionnaireFlow.submission(emptyList(), answers, today)).isEmpty()
    }

    @Test
    fun numericEditingHandlesDecimalCommaPastedTextAndLocalizedDigits() {
        assertThat(QuestionnaireNumbers.sanitize("-1,234.50 units", Locale.US, true)).isEqualTo("-1234.50")
        assertThat(QuestionnaireNumbers.sanitize("--1,2,3", Locale.GERMANY, false)).isEqualTo("1,23")
        assertThat(QuestionnaireNumbers.parse("-1,25", Locale.GERMANY)).isEqualTo(-1.25)
        assertThat(QuestionnaireNumbers.parse("١٢٫٥", Locale.forLanguageTag("ar"))).isEqualTo(12.5)
        assertThat(QuestionnaireNumbers.parse("-", Locale.US)).isNull()
        assertThat(QuestionnaireNumbers.parse("NaN", Locale.US)).isNull()
        assertThat(QuestionnaireNumbers.parse("1.2.3", Locale.US)).isNull()
        listOf(Locale.US, Locale.GERMANY, Locale.forLanguageTag("ar")).forEach { locale ->
            assertThat(QuestionnaireNumbers.parse(QuestionnaireNumbers.format(-12.5, locale), locale)).isEqualTo(-12.5)
        }
    }

    private fun question(
        kind: QuestionnaireQuestionKind,
        id: String = "question",
    ) = QuestionnaireQuestion(id, "Prompt", kind, isRequired = true)
}
