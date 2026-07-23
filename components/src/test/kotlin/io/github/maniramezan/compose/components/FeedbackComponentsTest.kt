package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class FeedbackComponentsTest {
    @Test
    fun feedbackComponentNamesAreStable() {
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
        ).containsExactly(
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
        ).inOrder()
    }
}
