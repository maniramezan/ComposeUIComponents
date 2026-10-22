package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class FeedbackComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    private val indeterminateProgress =
        SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo.Indeterminate)

    @Test
    public fun skeletonPlaceholdersAreHiddenFromAccessibility() {
        composeRule.setContent {
            AppTheme {
                Box {
                    Skeleton(modifier = Modifier.testTag(SKELETON))
                    SkeletonBlock(height = 24.dp, modifier = Modifier.testTag(BLOCK))
                }
            }
        }

        // clearAndSetSemantics stops TalkBack from landing on an empty placeholder node.
        listOf(SKELETON, BLOCK).forEach { tag ->
            val node = composeRule.onNodeWithTag(tag).fetchSemanticsNode()
            assertTrue("$tag should clear its semantics", node.config.isClearingSemantics)
            assertTrue("$tag should expose no children", node.children.isEmpty())
        }
    }

    @Test
    public fun skeletonBlockHonorsExplicitSize() {
        composeRule.setContent {
            AppTheme {
                SkeletonBlock(height = 24.dp, width = 120.dp, modifier = Modifier.testTag(BLOCK))
            }
        }

        composeRule
            .onNodeWithTag(BLOCK)
            .assertWidthIsEqualTo(120.dp)
            .assertHeightIsEqualTo(24.dp)
    }

    @Test
    public fun loadMoreFooterRequestsOnceAndShowsProgressOnlyWhileLoading() {
        var loadRequests = 0
        var loading by mutableStateOf(false)
        composeRule.setContent {
            AppTheme {
                LoadMoreFooter(isLoadingMore = loading, onLoadMore = { loadRequests++ })
            }
        }

        composeRule.runOnIdle { assertEquals(1, loadRequests) }
        composeRule.onAllNodes(indeterminateProgress).assertCountEquals(0)

        composeRule.runOnIdle { loading = true }
        composeRule.onAllNodes(indeterminateProgress).assertCountEquals(1)

        composeRule.runOnIdle { loading = false }
        composeRule.onAllNodes(indeterminateProgress).assertCountEquals(0)
        assertEquals("Toggling loading must not re-request", 1, loadRequests)
    }

    @Test
    public fun loadMoreFooterRequestsAgainWhenScrolledBackIntoView() {
        var loadRequests = 0
        lateinit var scrollToTop: () -> Unit
        lateinit var scrollToBottom: () -> Unit
        composeRule.setContent {
            AppTheme {
                val state = rememberLazyListState()
                val scope = rememberCoroutineScope()
                scrollToTop = { scope.launch { state.scrollToItem(0) } }
                scrollToBottom = { scope.launch { state.scrollToItem(ROW_COUNT) } }
                LazyColumn(state = state, modifier = Modifier.testTag(LIST)) {
                    items(ROW_COUNT) { index -> Text("Row $index", modifier = Modifier.testTag(ROW)) }
                    item { LoadMoreFooter(isLoadingMore = false, onLoadMore = { loadRequests++ }) }
                }
            }
        }

        composeRule.onAllNodesWithTag(ROW).fetchSemanticsNodes().also { check(it.size < ROW_COUNT) }
        composeRule.runOnIdle { assertEquals(0, loadRequests) }

        composeRule.runOnIdle { scrollToBottom() }
        composeRule.runOnIdle { assertEquals(1, loadRequests) }

        composeRule.runOnIdle { scrollToTop() }
        composeRule.runOnIdle { scrollToBottom() }
        composeRule.runOnIdle { assertEquals(2, loadRequests) }
    }

    private companion object {
        const val SKELETON = "skeleton"
        const val BLOCK = "block"
        const val LIST = "list"
        const val ROW = "row"
        const val ROW_COUNT = 100
    }
}
