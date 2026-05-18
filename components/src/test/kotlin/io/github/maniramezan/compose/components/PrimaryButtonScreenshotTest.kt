package io.github.maniramezan.compose.components

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w360dp-h640dp-xhdpi")
public class PrimaryButtonScreenshotTest {
    @Test
    public fun primaryButtonDefaultMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/primary-button-default.png") {
            PrimaryButtonPreview()
        }
    }
}
