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
                "Toast",
            ),
        ).containsExactly(
            "ProgressIndicator",
            "Skeleton",
            "Toast",
        ).inOrder()
    }
}
