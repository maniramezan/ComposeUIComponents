package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ListComponentsTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    @Test
    public fun emptyStateRendersCallerSuppliedTextAndAction() {
        composeRule.setContent {
            EmptyState(
                title = "Nothing here",
                message = "Items appear here",
                action = { TextButton(text = "Add", onClick = {}) },
            )
        }

        composeRule.onNodeWithText("Nothing here").assertIsDisplayed()
        composeRule.onNodeWithText("Items appear here").assertIsDisplayed()
        composeRule.onNodeWithText("Add").assertIsDisplayed()
    }

    @Test
    public fun listItemMergesHeadlineAndSupportingTextIntoOneFocusStop() {
        composeRule.setContent {
            ListItem(
                headline = "Headline",
                supportingText = "Supporting",
                trailingContent = { Text("Trailing") },
            )
        }

        composeRule
            .onNode(hasText("Headline") and hasText("Supporting"), useUnmergedTree = false)
            .assertIsDisplayed()
        composeRule.onNodeWithText("Trailing").assertIsDisplayed()
    }

    @Test
    public fun listItemOmitsSupportingTextWhenNull() {
        composeRule.setContent {
            ListItem(headline = "Headline only")
        }

        composeRule.onAllNodes(hasText("Headline only")).assertCountEquals(1)
        composeRule.onNode(hasText("Headline only")).assert(hasTextExactly("Headline only"))
    }

    @Test
    public fun lazyListRendersItemsLazily() {
        composeRule.setContent {
            LazyList(modifier = Modifier.height(200.dp)) {
                items(100) { index -> Text("Row $index") }
            }
        }

        composeRule.onNodeWithText("Row 0").assertIsDisplayed()
        composeRule.onNodeWithText("Row 99").assertDoesNotExist()
    }
}
