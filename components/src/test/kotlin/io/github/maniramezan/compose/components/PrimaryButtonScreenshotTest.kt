package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.Test

public class PrimaryButtonScreenshotTest {
    @Test
    public fun primaryButtonDefaultGoldenPathIsStable() {
        assertThat("build/outputs/roborazzi/primary-button-default.png")
            .endsWith("primary-button-default.png")
    }
}
