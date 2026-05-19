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
public class ComponentCategoryScreenshotTest {
    @Test
    public fun actionComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/actions-components.png") {
            ActionComponentsPreview()
        }
    }

    @Test
    public fun inputComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/input-components.png") {
            InputComponentsPreview()
        }
    }

    @Test
    public fun containerComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/container-components.png") {
            ContainerComponentsPreview()
        }
    }

    @Test
    public fun dialogMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/dialog.png") {
            DialogPreview()
        }
    }

    @Test
    public fun listComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/list-components.png") {
            ListComponentsPreview()
        }
    }

    @Test
    public fun navigationComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/navigation-components.png") {
            NavigationComponentsPreview()
        }
    }

    @Test
    public fun navigationRailMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/navigation-rail.png") {
            NavRailPreview()
        }
    }

    @Test
    public fun feedbackComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/feedback-components.png") {
            FeedbackComponentsPreview()
        }
    }

    @Test
    public fun typographyComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/typography-components.png") {
            TypographyComponentsPreview()
        }
    }
}
