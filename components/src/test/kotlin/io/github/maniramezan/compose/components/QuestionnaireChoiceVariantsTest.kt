package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireAnswer
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireChoiceStyle
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireOption
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestion
import io.github.maniramezan.compose.foundation.questionnaire.QuestionnaireQuestionKind
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class QuestionnaireChoiceVariantsTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private val options = listOf(QuestionnaireOption("a", "Option A"), QuestionnaireOption("b", "Option B"))
    private var submitted: Map<String, QuestionnaireAnswer>? = null

    @Test
    public fun everySingleChoiceStyleReplacesAndClearsTheSelection() {
        exerciseStyles(QuestionnaireQuestionKind.SingleChoice(options)) {
            composeRule.onNodeWithText("Done").assertIsNotEnabled()
            composeRule.onNodeWithText("Option A").assertHeightIsAtLeast(48.dp).performClick()
            composeRule.onNodeWithText("Option B").performClick()
            composeRule.onNodeWithText("Option A").assertIsNotSelected()
            composeRule.onNodeWithText("Option B").assertIsSelected().performClick()
            composeRule.onNodeWithText("Done").assertIsNotEnabled()
            composeRule.onNodeWithText("Option A").performClick()
            composeRule.onNodeWithText("Done").performClick()
            composeRule.runOnIdle { assertThat(submitted).containsExactly("question", QuestionnaireAnswer.Choices(listOf("a"))) }
        }
    }

    @Test
    public fun everyMultipleChoiceStyleTogglesIndependentlyAndUsesOptionOrder() {
        exerciseStyles(QuestionnaireQuestionKind.MultipleChoice(options)) {
            composeRule.onNodeWithText("Done").assertIsNotEnabled()
            composeRule.onNodeWithText("Option B").assertHeightIsAtLeast(48.dp).performClick()
            composeRule.onNodeWithText("Option A").performClick()
            composeRule.onNodeWithText("Option B").assertIsSelected().performClick()
            composeRule.onNodeWithText("Option A").assertIsSelected()
            composeRule.onNodeWithText("Option B").assertIsNotSelected().performClick()
            composeRule.onNodeWithText("Done").assertIsEnabled().performClick()
            composeRule.runOnIdle { assertThat(submitted).containsExactly("question", QuestionnaireAnswer.Choices(listOf("a", "b"))) }
        }
    }

    @Test
    public fun everyBooleanStyleDistinguishesFalseFromUnanswered() {
        exerciseStyles(QuestionnaireQuestionKind.Boolean("Yes", "No")) {
            composeRule.onNodeWithText("Done").assertIsNotEnabled()
            composeRule.onNodeWithText("Yes").performClick()
            composeRule.onNodeWithText("No").assertHeightIsAtLeast(48.dp).performClick()
            composeRule.onNodeWithText("Yes").assertIsNotSelected()
            composeRule.onNodeWithText("No").assertIsSelected().performClick()
            composeRule.onNodeWithText("Done").assertIsNotEnabled()
            composeRule.onNodeWithText("No").performClick()
            composeRule.onNodeWithText("Done").performClick()
            composeRule.runOnIdle { assertThat(submitted).containsExactly("question", QuestionnaireAnswer.Boolean(false)) }
        }
    }

    private fun exerciseStyles(
        kind: QuestionnaireQuestionKind,
        assertions: () -> Unit,
    ) {
        var style by mutableStateOf(QuestionnaireChoiceStyle.Automatic)
        composeRule.setContent {
            key(style) { VariantHost(kind, style) }
        }
        QuestionnaireChoiceStyle.entries.forEach { variant ->
            composeRule.runOnIdle {
                style = variant
                submitted = null
            }
            assertions()
        }
    }

    @Composable
    private fun VariantHost(
        kind: QuestionnaireQuestionKind,
        style: QuestionnaireChoiceStyle,
    ) {
        var answers by remember { mutableStateOf<Map<String, QuestionnaireAnswer>>(emptyMap()) }
        QuestionnaireView(
            questions = listOf(QuestionnaireQuestion("question", "Choose", kind, isRequired = true, choiceStyle = style)),
            answers = answers,
            onAnswersChange = { answers = it },
            labels = QuestionnaireLabels("Back", "Next", "Done", "Increase", "Decrease"),
            onSubmit = { submitted = it },
            modifier = Modifier.height(620.dp),
        )
    }
}
