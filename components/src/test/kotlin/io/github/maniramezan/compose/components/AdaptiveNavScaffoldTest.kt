package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class AdaptiveNavScaffoldTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun contentRemembersStateAcrossWidthBreakpoint() {
        var expanded by mutableStateOf(false)
        composeRule.setContent {
            AppTheme {
                val spacing = AppTheme.spacing
                AppTheme(spacing = spacing.copy(expandedNavigationBreakpoint = if (expanded) 0.dp else 10_000.dp)) {
                    AdaptiveNavScaffold(
                        items = listOf(TabBarItemData(value = 0, icon = { Text("Icon") }, label = { Text("Home") })),
                        selection = 0,
                        onSelectionChange = {},
                    ) {
                        var count by remember { mutableIntStateOf(0) }
                        Column {
                            Text("Count $count")
                            TextButton(text = "Increment", onClick = { count++ })
                        }
                    }
                }
            }
        }

        composeRule.onNodeWithText("Increment").performClick()
        composeRule.onNodeWithText("Count 1").assertExists()
        composeRule.runOnIdle { expanded = true }
        composeRule.onNodeWithText("Count 1").assertExists()
        composeRule.runOnIdle { expanded = false }
        composeRule.onNodeWithText("Count 1").assertExists()
    }
}
