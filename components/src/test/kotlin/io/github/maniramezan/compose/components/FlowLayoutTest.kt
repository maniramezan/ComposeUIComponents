package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class FlowLayoutTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun displaysDynamicItems() {
        composeRule.setContent {
            AppTheme {
                FlowLayout {
                    Text("First")
                    Text("Second")
                }
            }
        }

        composeRule.onNodeWithText("First").assertIsDisplayed()
        composeRule.onNodeWithText("Second").assertIsDisplayed()
    }
}
