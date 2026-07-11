package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ShowcaseVerticalSectionTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun verticalSectionRendersHeaderAndItems() {
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    verticalSection(title = "New Videos", items = listOf("First", "Second")) {
                        Text(it)
                    }
                }
            }
        }

        composeRule.onNodeWithText("New Videos").assertExists()
        composeRule.onNodeWithText("First").assertExists()
        composeRule.onNodeWithText("Second").assertExists()
    }

    @Test
    public fun verticalSectionInvokesLoadMoreWhenHasMoreIsTrue() {
        var loadMoreCount = 0
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    verticalSection(
                        title = "New Videos",
                        items = listOf("First"),
                        hasMore = true,
                        onLoadMore = { loadMoreCount++ },
                    ) { Text(it) }
                }
            }
        }

        composeRule.runOnIdle {
            assertThat(loadMoreCount).isEqualTo(1)
        }
    }

    @Test
    public fun verticalSectionDoesNotInvokeLoadMoreWhenHasMoreIsFalse() {
        var loadMoreCount = 0
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    verticalSection(
                        title = "New Videos",
                        items = listOf("First"),
                        hasMore = false,
                        onLoadMore = { loadMoreCount++ },
                    ) { Text(it) }
                }
            }
        }

        composeRule.runOnIdle {
            assertThat(loadMoreCount).isEqualTo(0)
        }
    }

    @Test
    public fun verticalSectionCoexistsWithHorizontalSections() {
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    section(title = "Trending", items = listOf("Hit")) { Text(it) }
                    verticalSection(title = "New Videos", items = listOf("Fresh")) { Text(it) }
                }
            }
        }

        composeRule.onNodeWithText("Trending").assertExists()
        composeRule.onNodeWithText("Hit").assertExists()
        composeRule.onNodeWithText("New Videos").assertExists()
        composeRule.onNodeWithText("Fresh").assertExists()
    }
}
