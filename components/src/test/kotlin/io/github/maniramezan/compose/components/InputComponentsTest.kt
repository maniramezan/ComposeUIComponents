package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class InputComponentsTest {
    @Test
    fun inputComponentNamesAreStable() {
        assertThat(
            listOf(
                "TextField",
                "PasswordField",
                "SearchField",
                "Checkbox",
                "RadioGroup",
                "Switch",
                "Slider",
            ),
        ).containsExactly(
            "TextField",
            "PasswordField",
            "SearchField",
            "Checkbox",
            "RadioGroup",
            "Switch",
            "Slider",
        ).inOrder()
    }
}
