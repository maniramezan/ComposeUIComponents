package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActionComponentsTest {
    @Test
    fun actionComponentNamesAreStable() {
        val expected =
            listOf(
                "PrimaryButton",
                "SecondaryButton",
                "TextButton",
                "IconButton",
                "FAB",
                "ExtendedFloatingActionButton",
                "SegmentedControl",
                "SingleChoiceSegmentedButtonRow",
            )
        assertThat(
            listOf(
                "PrimaryButton",
                "SecondaryButton",
                "TextButton",
                "IconButton",
                "FAB",
                "ExtendedFloatingActionButton",
                "SegmentedControl",
                "SingleChoiceSegmentedButtonRow",
            ),
        ).containsExactlyElementsIn(expected).inOrder()
    }
}
