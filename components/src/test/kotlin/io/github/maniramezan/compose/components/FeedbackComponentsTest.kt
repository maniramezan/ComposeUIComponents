package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class FeedbackComponentsTest {
    @Test
    fun feedbackComponentNamesAreStable() {
        val expected =
            listOf(
                "ProgressIndicator",
                "Skeleton",
                "SkeletonBlock",
                "Toast",
                "ToastHost",
                "ChatLog",
                "AssistantContextCard",
                "AssistantQuickActionChips",
                "AssistantStatusBanner",
                "AssistantLimitPromptCard",
                "AssistantDisclaimerFooter",
            )
        assertThat(
            listOf(
                "ProgressIndicator",
                "Skeleton",
                "SkeletonBlock",
                "Toast",
                "ToastHost",
                "ChatLog",
                "AssistantContextCard",
                "AssistantQuickActionChips",
                "AssistantStatusBanner",
                "AssistantLimitPromptCard",
                "AssistantDisclaimerFooter",
            ),
        ).containsExactlyElementsIn(expected).inOrder()
    }
}
