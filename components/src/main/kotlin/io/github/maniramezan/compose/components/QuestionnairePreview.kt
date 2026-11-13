package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@Composable
internal fun QuestionnairePreviewContent() {
    var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }
    QuestionnaireView(
        questions =
            listOf(
                QuestionnaireQuestion(
                    id = "choice",
                    prompt = "Which option do you prefer?",
                    detail = "Choose the closest match.",
                    isRequired = true,
                    kind =
                        QuestionnaireQuestionKind.SingleChoice(
                            listOf(QuestionnaireOption("Option A"), QuestionnaireOption("Option B"), QuestionnaireOption("Option C")),
                        ),
                ),
            ),
        answers = answers,
        onAnswersChange = { answers = it },
        labels = QuestionnaireLabels("Back", "Next", "Done", "Increase value", "Decrease value", "Skip", "Optional"),
        onSubmit = {},
        onSkip = {},
        progressDescription = { page, count -> "Question $page of $count" },
        modifier = Modifier.height(AppTheme.spacing.x9 * 8),
    )
}

@PreviewLightDark
@PreviewFontScale
@ShowkaseComposable(name = "Questionnaire", group = "Inputs")
@Composable
public fun QuestionnairePreview(): Unit = AppTheme { QuestionnairePreviewContent() }
