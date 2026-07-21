package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class TabBarInteractionTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private fun items(disableIndex: Int? = null) =
        listOf(0, 1, 2).mapIndexed { index, value ->
            TabBarItemData(
                value = value,
                icon = { Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null) },
                label = { Text(text = "Item $value") },
                enabled = index != disableIndex,
            )
        }

    @Test
    public fun tappingItemInvokesCallbackWithItsValue() {
        var lastSelection = -1
        composeRule.setContent {
            AppTheme {
                TabBar(items = items(), selection = 0, onSelectionChange = { lastSelection = it })
            }
        }

        composeRule.onNodeWithText("Item 2").performClick()

        composeRule.runOnIdle {
            assert(lastSelection == 2) { "Expected value 2, got $lastSelection" }
        }
    }

    @Test
    public fun selectionIsDrivenByValueNotPosition() {
        composeRule.setContent {
            AppTheme {
                var selection by remember { mutableIntStateOf(1) }
                TabBar(
                    // Reversed order — selection must still track by value, not list position.
                    items = items().reversed(),
                    selection = selection,
                    onSelectionChange = { selection = it },
                )
            }
        }

        composeRule.onNodeWithText("Item 1").assertIsSelected()
        composeRule.onNodeWithText("Item 0").assertIsNotSelected()
        composeRule.onNodeWithText("Item 2").assertIsNotSelected()
    }

    @Test
    public fun disabledItemDoesNotInvokeCallbackAndIsNotEnabled() {
        var lastSelection = -1
        composeRule.setContent {
            AppTheme {
                TabBar(
                    items = items(disableIndex = 2),
                    selection = 0,
                    onSelectionChange = { lastSelection = it },
                )
            }
        }

        composeRule.onNodeWithText("Item 2").assertIsNotEnabled()
        composeRule.onNodeWithText("Item 2").performClick()

        composeRule.runOnIdle {
            assert(lastSelection == -1) { "Disabled item must not invoke the selection callback" }
        }
        composeRule.onNodeWithText("Item 0").assertIsEnabled()
    }

    @Test
    public fun itemsMeetMinimumTouchTargetHeight() {
        composeRule.setContent {
            AppTheme {
                TabBar(items = items(), selection = 0, onSelectionChange = {})
            }
        }

        composeRule.onNodeWithText("Item 0").assertHeightIsAtLeast(48.dp)
        composeRule.onNodeWithText("Item 1").assertHeightIsAtLeast(48.dp)
    }

    @Test
    public fun iconOnlyItemExposesContentDescriptionForAccessibility() {
        composeRule.setContent {
            AppTheme {
                TabBar(
                    items =
                        listOf(
                            TabBarItemData(
                                value = 0,
                                icon = {
                                    Icon(imageVector = AppTheme.icons.check.imageVector, contentDescription = null)
                                },
                                label = null,
                                contentDescription = "Home",
                            ),
                        ),
                    selection = 0,
                    onSelectionChange = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Home").assertIsSelected()
    }

    @Test
    public fun centeredArrangementRendersAllItems() {
        composeRule.setContent {
            AppTheme {
                TabBar(
                    items = items(),
                    selection = 0,
                    onSelectionChange = {},
                    arrangement = TabBarArrangement.Centered,
                )
            }
        }

        composeRule.onNodeWithText("Item 0").assertIsSelected()
        composeRule.onNodeWithText("Item 1").assertIsNotSelected()
        composeRule.onNodeWithText("Item 2").assertIsNotSelected()
    }

    @Test
    public fun scrollBehaviorCollapsesTheBarsMeasuredHeightNotJustItsDrawPosition() {
        lateinit var scrollBehavior: TabBarScrollBehavior
        composeRule.setContent {
            AppTheme {
                scrollBehavior = rememberTabBarScrollBehavior()
                TabBar(
                    items = items(),
                    selection = 0,
                    onSelectionChange = {},
                    scrollBehavior = scrollBehavior,
                )
            }
        }
        composeRule.waitForIdle()

        val limit = scrollBehavior.heightOffsetLimit
        assert(limit < 0f) { "Expected TabBar to report a real collapse limit once measured, got $limit" }

        // Simulate a full scroll-to-hide.
        composeRule.runOnIdle { scrollBehavior.heightOffset = limit }
        composeRule.waitForIdle()

        // The bar's own layout size — not just its draw offset — must shrink to zero, so a
        // parent (e.g. Scaffold) reclaims the space for content above it.
        composeRule.onRoot().assertHeightIsEqualTo(0.dp)
    }

    @Test
    public fun scrollingRealContentUpHidesTheBarViaTheNestedScrollConnection() {
        lateinit var scrollBehavior: TabBarScrollBehavior
        composeRule.setContent {
            AppTheme {
                scrollBehavior = rememberTabBarScrollBehavior()
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
                                .testTag("list"),
                    ) {
                        items(100) { index -> Text(text = "Row $index", modifier = Modifier.fillMaxWidth().height(48.dp)) }
                    }
                    TabBar(
                        items = items(),
                        selection = 0,
                        onSelectionChange = {},
                        scrollBehavior = scrollBehavior,
                    )
                }
            }
        }
        composeRule.waitForIdle()
        assert(scrollBehavior.heightOffsetLimit < 0f) {
            "Expected TabBar to report a real collapse limit once measured, got ${scrollBehavior.heightOffsetLimit}"
        }
        assert(scrollBehavior.heightOffset == 0f) { "Bar should start fully shown" }

        // A real upward scroll gesture on the actual scrollable content — not a manual
        // heightOffset assignment — is what should drive the bar into hiding.
        composeRule.onNodeWithTag("list").performTouchInput { swipeUp() }
        composeRule.waitForIdle()

        assert(scrollBehavior.heightOffset < 0f) {
            "Expected a real scroll gesture to move the bar toward hidden via its " +
                "nestedScrollConnection, but heightOffset is still ${scrollBehavior.heightOffset}"
        }
    }

    @Test
    public fun contentAboveExpandsIntoTheSpaceInARealScaffoldAsTheBarHides() {
        lateinit var scrollBehavior: TabBarScrollBehavior
        var lastBottomPaddingDp = androidx.compose.ui.unit.Dp.Unspecified
        composeRule.setContent {
            AppTheme {
                scrollBehavior = rememberTabBarScrollBehavior()
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        TabBar(
                            items = items(),
                            selection = 0,
                            onSelectionChange = {},
                            scrollBehavior = scrollBehavior,
                        )
                    },
                ) { innerPadding ->
                    lastBottomPaddingDp = innerPadding.calculateBottomPadding()
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
                                .testTag("scaffoldList"),
                    ) {
                        items(100) { index ->
                            Text(text = "Row $index", modifier = Modifier.fillMaxWidth().height(48.dp))
                        }
                    }
                }
            }
        }
        composeRule.waitForIdle()
        val fullyShownBottomPadding = lastBottomPaddingDp
        assert(scrollBehavior.heightOffsetLimit < 0f) { "Expected a real collapse limit once measured" }

        composeRule.onNodeWithTag("scaffoldList").performTouchInput { swipeUp() }
        composeRule.waitForIdle()

        // As the bar hides, Scaffold must recompute innerPadding to reflect the bar's smaller
        // reported height, so the content area actually grows into the reclaimed space instead
        // of always reserving the bar's full height regardless of its scroll state.
        assert(lastBottomPaddingDp < fullyShownBottomPadding) {
            "Expected Scaffold's bottom content padding to shrink as the bar hides: " +
                "was $fullyShownBottomPadding, now $lastBottomPaddingDp"
        }
    }
}
