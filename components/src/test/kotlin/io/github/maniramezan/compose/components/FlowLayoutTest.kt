package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class FlowLayoutTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    @Test
    public fun displaysDynamicItems() {
        composeRule.setContent {
            FlowLayout {
                Text("First")
                Text("Second")
            }
        }

        composeRule.onNodeWithText("First").assertIsDisplayed()
        composeRule.onNodeWithText("Second").assertIsDisplayed()
    }
}
