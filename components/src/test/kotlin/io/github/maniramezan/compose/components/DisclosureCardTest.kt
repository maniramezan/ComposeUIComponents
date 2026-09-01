package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class DisclosureCardTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun revealsDetailAfterTap() {
        composeRule.setContent {
            AppTheme(icons = defaultAppIcons()) {
                var expanded by remember { mutableStateOf(false) }
                DisclosureCard(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    expandedStateDescription = "Expanded",
                    collapsedStateDescription = "Collapsed",
                    summary = { Text("Summary") },
                    detail = { Text("Detail") },
                )
            }
        }

        composeRule.onNodeWithText("Summary").performClick()
        composeRule.onNodeWithText("Detail").assertIsDisplayed()
    }
}
