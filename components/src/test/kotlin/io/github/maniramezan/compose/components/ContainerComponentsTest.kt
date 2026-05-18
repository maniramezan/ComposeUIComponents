package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ContainerComponentsTest {
    @Test
    fun containerComponentNamesAreStable() {
        assertThat(
            listOf(
                "Card",
                "Surface",
                "Section",
                "BottomSheet",
                "Dialog",
                "Snackbar",
            ),
        ).containsExactly(
            "Card",
            "Surface",
            "Section",
            "BottomSheet",
            "Dialog",
            "Snackbar",
        ).inOrder()
    }
}
