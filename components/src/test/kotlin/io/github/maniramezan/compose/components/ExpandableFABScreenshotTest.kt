package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w360dp-h640dp-xhdpi")
public class ExpandableFABScreenshotTest {
    @Test
    public fun collapsed() {
        captureRoboImage("build/outputs/roborazzi/expandable-fab-collapsed.png") {
            AppTheme(icons = defaultAppIcons(), dynamicColor = false) {
                ExpandableFABPreviewContent(expanded = false)
            }
        }
    }

    @Test
    public fun vertical() {
        captureRoboImage("build/outputs/roborazzi/expandable-fab-up.png") {
            AppTheme(icons = defaultAppIcons(), dynamicColor = false) {
                ExpandableFABPreviewContent()
            }
        }
    }

    @Test
    public fun horizontal() {
        captureRoboImage("build/outputs/roborazzi/expandable-fab-start.png") {
            AppTheme(icons = defaultAppIcons(), dynamicColor = false) {
                ExpandableFABPreviewContent(direction = FABExpansionDirection.Start)
            }
        }
    }

    @Test
    public fun darkRtlWithDoubleFontScale() {
        captureRoboImage("build/outputs/roborazzi/expandable-fab-dark-rtl-font200.png") {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
            ) {
                AppTheme(icons = defaultAppIcons(), darkTheme = true, dynamicColor = false) {
                    Box(Modifier.width(240.dp).background(AppTheme.colors.background)) {
                        ExpandableFABPreviewContent(longLabels = true)
                    }
                }
            }
        }
    }

    @Test
    public fun horizontalRtlWithDoubleFontScale() {
        captureRoboImage("build/outputs/roborazzi/expandable-fab-horizontal-rtl-font200.png") {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
            ) {
                AppTheme(icons = defaultAppIcons(), dynamicColor = false) {
                    Box(Modifier.width(320.dp).background(AppTheme.colors.background)) {
                        ExpandableFABPreviewContent(direction = FABExpansionDirection.Start, longLabels = true)
                    }
                }
            }
        }
    }
}
