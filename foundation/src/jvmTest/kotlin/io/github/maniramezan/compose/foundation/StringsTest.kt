package io.github.maniramezan.compose.foundation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class StringsTest {
    @Test
    fun normalizedWhitespaceTrimsAndCollapsesWhitespace() {
        assertThat("  Primary\n Button\t Default  ".normalizedWhitespace()).isEqualTo("Primary Button Default")
    }

    @Test
    fun stableIdentifierKeepsLettersAndDigitsOnly() {
        assertThat("Primary Button / Default!".toStableIdentifier()).isEqualTo("primary-button-default")
    }

    @Test
    fun stableIdentifierSupportsCustomSeparator() {
        assertThat("Navigation Rail".toStableIdentifier(separator = '_')).isEqualTo("navigation_rail")
    }

    @Test
    fun displayNameCreatesReadableTitle() {
        assertThat("navigation-rail_default".toDisplayName()).isEqualTo("Navigation Rail Default")
    }
}
