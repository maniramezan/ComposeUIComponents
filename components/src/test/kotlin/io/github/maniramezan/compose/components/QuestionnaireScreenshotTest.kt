package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w360dp-h800dp-xhdpi")
public class QuestionnaireScreenshotTest {
    @Test
    public fun everyChoiceKindAndPresentation() {
        val options = listOf(QuestionnaireOption("a", "First option"), QuestionnaireOption("b", "Second option"))
        val kinds =
            listOf(
                QuestionnaireQuestionKind.SingleChoice(options) to QuestionnaireAnswer.Choices(listOf("a")),
                QuestionnaireQuestionKind.MultipleChoice(options) to QuestionnaireAnswer.Choices(listOf("a", "b")),
                QuestionnaireQuestionKind.Boolean("Yes", "No") to QuestionnaireAnswer.Boolean(false),
            )
        kinds.forEachIndexed { index, (kind, answer) ->
            QuestionnaireChoiceStyle.entries.forEach { style ->
                captureRoboImage("build/outputs/roborazzi/questionnaire-choice-$index-${style.name}.png") {
                    AppTheme(darkTheme = false, dynamicColor = false) {
                        QuestionnaireView(
                            questions =
                                listOf(
                                    QuestionnaireQuestion(
                                        id = "choice",
                                        prompt = "Which options apply?",
                                        kind = kind,
                                        isRequired = true,
                                        choiceStyle = style,
                                    ),
                                ),
                            answers = mapOf("choice" to answer),
                            onAnswersChange = {},
                            labels = QuestionnaireLabels("Back", "Next", "Done", "Increase", "Decrease"),
                            onSubmit = {},
                            modifier = Modifier.width(360.dp).height(700.dp).background(AppTheme.colors.surface),
                        )
                    }
                }
            }
        }
    }

    @Test
    public fun questionnaireInputsInLightAndDarkThemes() {
        val today = LocalDate.of(2026, 6, 15)
        val options = listOf(QuestionnaireOption("a", "Option A"), QuestionnaireOption("b", "Option B"))
        val kinds =
            listOf(
                QuestionnaireQuestionKind.SingleChoice(options),
                QuestionnaireQuestionKind.MultipleChoice(options),
                QuestionnaireQuestionKind.Boolean("Yes", "No"),
                QuestionnaireQuestionKind.Number(-10.0..100.0, 5.0, "items"),
                QuestionnaireQuestionKind.Date(today..today.plusDays(90)),
                QuestionnaireQuestionKind.Text("Write your answer"),
                QuestionnaireQuestionKind.Custom,
            )
        kinds.forEachIndexed { index, kind ->
            listOf(false, true).forEach { dark ->
                captureRoboImage("build/outputs/roborazzi/questionnaire-input-$index-${if (dark) "dark" else "light"}.png") {
                    AppTheme(darkTheme = dark, dynamicColor = false) {
                        QuestionnaireView(
                            questions =
                                listOf(
                                    QuestionnaireQuestion(
                                        id = "question",
                                        prompt = "Provide an answer",
                                        kind = kind,
                                        detail = "Choose the value that fits best.",
                                    ),
                                ),
                            answers = emptyMap(),
                            onAnswersChange = {},
                            labels = QuestionnaireLabels("Back", "Next", "Submit", "Increase", "Decrease", "Skip", "Optional"),
                            onSubmit = {},
                            today = today,
                            modifier = Modifier.width(360.dp).height(700.dp).background(AppTheme.colors.surface),
                            customInput = { _, _, _ -> Text("Caller-provided component") },
                        )
                    }
                }
            }
        }
    }

    @Test
    public fun wrappingChoicesAtDoubleFontScaleInRtl() {
        captureRoboImage("build/outputs/roborazzi/questionnaire-font200-rtl.png") {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(LocalDensity.current.density, 2f),
            ) {
                AppTheme(darkTheme = true, dynamicColor = false) {
                    QuestionnaireView(
                        questions =
                            listOf(
                                QuestionnaireQuestion(
                                    "choice",
                                    "Which option do you prefer?",
                                    QuestionnaireQuestionKind.MultipleChoice(
                                        listOf(
                                            QuestionnaireOption("a", "First option with a longer title"),
                                            QuestionnaireOption("b", "Second option"),
                                        ),
                                    ),
                                    choiceStyle = QuestionnaireChoiceStyle.Chips,
                                ),
                            ),
                        answers = mapOf("choice" to QuestionnaireAnswer.Choices(listOf("a"))),
                        onAnswersChange = {},
                        labels = QuestionnaireLabels("Back", "Next", "Submit answers", "Increase", "Decrease"),
                        onSubmit = {},
                        modifier = Modifier.width(360.dp).height(700.dp).background(AppTheme.colors.surface),
                    )
                }
            }
        }
    }
}
