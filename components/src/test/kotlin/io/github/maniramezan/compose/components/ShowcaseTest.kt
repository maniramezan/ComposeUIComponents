package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class ShowcaseTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun peekDefaultsToAppStoreFraction() {
        assertThat(ShowcaseItemWidth.Peek().visibleFraction).isEqualTo(0.85f)
    }

    @Test
    public fun itemWidthVariantsHaveValueEquality() {
        assertThat(ShowcaseItemWidth.Peek(0.5f)).isEqualTo(ShowcaseItemWidth.Peek(0.5f))
        assertThat(ShowcaseItemWidth.Fixed(120.dp)).isEqualTo(ShowcaseItemWidth.Fixed(120.dp))
        assertThat(ShowcaseItemWidth.Wrap).isSameInstanceAs(ShowcaseItemWidth.Wrap)
    }

    @Test
    public fun feedRendersEverySectionTitleAndItsItems() {
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    section(title = "Top Apps", items = listOf("Focus", "Loop")) { Text(it) }
                    section(title = "Games", items = listOf("Nova")) { Text(it) }
                }
            }
        }

        composeRule.onNodeWithText("Top Apps").assertExists()
        composeRule.onNodeWithText("Games").assertExists()
        composeRule.onNodeWithText("Focus").assertExists()
    }

    @Test
    public fun sectionActionInvokesCallback() {
        var clicked = false
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    section(
                        title = "Top Apps",
                        items = listOf("Focus"),
                        actionLabel = "See all",
                        onAction = { clicked = true },
                    ) { Text(it) }
                }
            }
        }

        composeRule.onNodeWithText("See all").performClick()

        composeRule.runOnIdle {
            assertThat(clicked).isTrue()
        }
    }

    @Test
    public fun customSectionRendersArbitraryContent() {
        composeRule.setContent {
            AppTheme {
                ShowcaseFeed {
                    customSection { Text("Hero Banner") }
                }
            }
        }

        composeRule.onNodeWithText("Hero Banner").assertExists()
    }
}
