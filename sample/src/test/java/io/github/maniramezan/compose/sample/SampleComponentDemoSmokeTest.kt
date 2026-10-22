package io.github.maniramezan.compose.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class SampleComponentDemoSmokeTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun demoIdsAreUnique() {
        val ids = sampleDemos().map { it.id }
        assertEquals("Duplicate demo ids: ${ids.groupBy { it }.filterValues { it.size > 1 }.keys}", ids.size, ids.toSet().size)
    }

    @Test
    public fun everyDemoPageComposesWithoutCrashing() {
        val demos = sampleDemos()
        assertTrue(demos.isNotEmpty())
        var current by mutableStateOf(demos.first())
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                // Mirrors the browser's detail pane, which scrolls vertically.
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    current.content()
                }
            }
        }

        demos.forEach { demo ->
            composeRule.runOnIdle { current = demo }
            composeRule.waitForIdle()
        }
    }
}
