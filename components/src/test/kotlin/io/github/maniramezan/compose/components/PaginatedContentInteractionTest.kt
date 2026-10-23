package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.maniramezan.compose.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class PaginatedContentInteractionTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun callbackReplacementDoesNotRepeatTheCurrentPageAndUsesTheLatestCallback() {
        var revision by mutableIntStateOf(0)
        val events = mutableListOf<String>()
        lateinit var pagerState: PagerState
        lateinit var scope: CoroutineScope
        composeRule.setContent {
            AppTheme {
                val currentRevision = revision
                pagerState = rememberPagerState(pageCount = { 2 })
                scope = rememberCoroutineScope()
                PaginatedContent(
                    pages = listOf(PaginationPage("First"), PaginationPage("Second")),
                    pagerState = pagerState,
                    modifier = Modifier.height(240.dp),
                    showScrollHint = false,
                    onPageChanged = { events += "$currentRevision:$it" },
                ) { _, page -> Text(page.title) }
            }
        }

        composeRule.runOnIdle { assertThat(events).containsExactly("0:0") }
        composeRule.runOnIdle { revision = 1 }
        composeRule.runOnIdle { assertThat(events).containsExactly("0:0") }
        composeRule.runOnIdle { scope.launch { pagerState.scrollToPage(1) } }
        composeRule.runOnIdle { assertThat(events).containsExactly("0:0", "1:1").inOrder() }
    }
}
