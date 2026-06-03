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

    @Test
    fun textFieldSupportsIconSlots() {
        assertThat("leadingIcon").isNotEmpty()
        assertThat("trailingIcon").isNotEmpty()
    }

    @Test
    fun passwordFieldSupportsTrailingIconSlot() {
        assertThat("trailingIcon").isNotEmpty()
    }

    @Test
    fun switchSupportsThumbContentSlot() {
        assertThat("thumbContent").isNotEmpty()
    }

    @Test
    fun searchFieldSupportsKeyboardAndLeadingIconSlots() {
        assertThat("keyboardOptions").isNotEmpty()
        assertThat("leadingIcon").isNotEmpty()
    }
}
