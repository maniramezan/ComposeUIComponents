package io.github.maniramezan.compose.components

import android.provider.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.testcommons.compose.ThemedComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class AsyncContentTest {
    @get:Rule
    public val composeRule = ThemedComposeRule(theme = { content -> AppTheme(content = content) })

    private var state by mutableStateOf<AsyncContentState<String>>(AsyncContentState.Loading)
    private var retryCount = 0
    private var createCount = 0

    private fun setAsyncContent(
        initial: AsyncContentState<String>,
        emptyMessage: String? = EMPTY_MESSAGE,
        withActions: Boolean = true,
    ) {
        state = initial
        composeRule.setContent {
            AsyncContent(
                state = state,
                loadingLabel = LOADING,
                emptyTitle = EMPTY_TITLE,
                errorTitle = ERROR_TITLE,
                errorMessage = ERROR_MESSAGE,
                emptyMessage = emptyMessage,
                emptyAction =
                    if (withActions) {
                        { TextButton(text = CREATE, onClick = { createCount++ }) }
                    } else {
                        null
                    },
                errorAction =
                    if (withActions) {
                        { TextButton(text = RETRY, onClick = { retryCount++ }) }
                    } else {
                        null
                    },
            ) { data ->
                Text(data)
            }
        }
    }

    private fun setReducedMotion(reduced: Boolean) {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        Settings.Global.putFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            if (reduced) 0f else 1f,
        )
    }

    private fun livePolite(text: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite) and
            hasAnyDescendant(hasText(text))

    @Test
    public fun loadingShowsOnlyTheCallerSuppliedLabel() {
        setAsyncContent(AsyncContentState.Loading)

        composeRule.onNodeWithText(LOADING).assertIsDisplayed()
        composeRule.onNodeWithText(EMPTY_TITLE).assertDoesNotExist()
        composeRule.onNodeWithText(ERROR_TITLE).assertDoesNotExist()
        composeRule.onNodeWithText(CONTENT).assertDoesNotExist()
    }

    @Test
    public fun emptyShowsTitleMessageAndActionThatInvokesCallback() {
        setAsyncContent(AsyncContentState.Empty)

        composeRule.onNodeWithText(EMPTY_TITLE).assertIsDisplayed()
        composeRule.onNodeWithText(EMPTY_MESSAGE).assertIsDisplayed()
        composeRule.onNodeWithText(CREATE).performClick()

        assertEquals(1, createCount)
        composeRule.onNodeWithText(LOADING).assertDoesNotExist()
    }

    @Test
    public fun emptyOmitsNullMessageAndAction() {
        setAsyncContent(AsyncContentState.Empty, emptyMessage = null, withActions = false)

        composeRule.onNodeWithText(EMPTY_TITLE).assertIsDisplayed()
        composeRule.onNodeWithText(EMPTY_MESSAGE).assertDoesNotExist()
        composeRule.onNodeWithText(CREATE).assertDoesNotExist()
    }

    @Test
    public fun errorShowsDefaultMessageAndRetryAction() {
        setAsyncContent(AsyncContentState.Error())

        composeRule.onNodeWithText(ERROR_TITLE).assertIsDisplayed()
        composeRule.onNodeWithText(ERROR_MESSAGE).assertIsDisplayed()
        composeRule.onNodeWithText(RETRY).performClick()

        assertEquals(1, retryCount)
    }

    @Test
    public fun errorStateMessageOverridesTheDefaultMessage() {
        setAsyncContent(AsyncContentState.Error(message = CUSTOM_ERROR))

        composeRule.onNodeWithText(CUSTOM_ERROR).assertIsDisplayed()
        composeRule.onNodeWithText(ERROR_MESSAGE).assertDoesNotExist()
    }

    @Test
    public fun blankErrorStateMessageFallsBackToTheDefaultMessage() {
        setAsyncContent(AsyncContentState.Error(message = "  "))

        composeRule.onNodeWithText(ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    public fun contentReceivesTheStateData() {
        setAsyncContent(AsyncContentState.Content(CONTENT))

        composeRule.onNodeWithText(CONTENT).assertIsDisplayed()
        composeRule.onNodeWithText(LOADING).assertDoesNotExist()
    }

    @Test
    public fun everyPlaceholderIsAnnouncedAsAPoliteLiveRegion() {
        setAsyncContent(AsyncContentState.Loading)
        composeRule.onNode(livePolite(LOADING)).assertExists()

        state = AsyncContentState.Empty
        composeRule.waitForIdle()
        composeRule.onNode(livePolite(EMPTY_TITLE)).assertExists()

        state = AsyncContentState.Error()
        composeRule.waitForIdle()
        composeRule.onNode(livePolite(ERROR_TITLE)).assertExists()
    }

    @Test
    public fun switchingStateKindReplacesThePreviousPresentation() {
        setAsyncContent(AsyncContentState.Loading)

        state = AsyncContentState.Content(CONTENT)
        composeRule.waitForIdle()

        composeRule.onNodeWithText(CONTENT).assertIsDisplayed()
        composeRule.onNodeWithText(LOADING).assertDoesNotExist()
    }

    @Test
    public fun changingContentDataKeepsASingleContentNode() {
        setAsyncContent(AsyncContentState.Content(CONTENT))

        state = AsyncContentState.Content(OTHER_CONTENT)
        composeRule.mainClock.advanceTimeByFrame()
        composeRule.waitForIdle()

        composeRule.onNodeWithText(OTHER_CONTENT).assertIsDisplayed()
        composeRule.onNodeWithText(CONTENT).assertDoesNotExist()
    }

    @Test
    public fun crossFadeKeepsTheOutgoingStateUntilTheTransitionEnds() {
        setReducedMotion(false)
        setAsyncContent(AsyncContentState.Loading)
        composeRule.waitForIdle()

        composeRule.mainClock.autoAdvance = false
        state = AsyncContentState.Content(CONTENT)
        composeRule.waitForIdle()
        composeRule.mainClock.advanceTimeByFrame()

        composeRule.onNodeWithText(LOADING).assertExists()
        composeRule.onNodeWithText(CONTENT).assertExists()

        composeRule.mainClock.autoAdvance = true
        composeRule.waitForIdle()

        composeRule.onNodeWithText(LOADING).assertDoesNotExist()
        composeRule.onNodeWithText(CONTENT).assertIsDisplayed()
    }

    @Test
    public fun removedAnimationsSwitchStatesWithoutACrossFade() {
        setReducedMotion(true)
        setAsyncContent(AsyncContentState.Loading)
        composeRule.waitForIdle()

        composeRule.mainClock.autoAdvance = false
        state = AsyncContentState.Content(CONTENT)
        composeRule.waitForIdle()
        composeRule.mainClock.advanceTimeByFrame()

        composeRule.onNodeWithText(LOADING).assertDoesNotExist()
        composeRule.onNodeWithText(CONTENT).assertExists()
    }

    @Test
    public fun slotOverloadRendersCallerSlotsAndPassesTheErrorState() {
        state = AsyncContentState.Loading
        composeRule.setContent {
            AsyncContent(
                state = state,
                loading = { Text("slot-loading") },
                empty = { Text("slot-empty") },
                error = { error -> Text("slot-error:${error.message}") },
                content = { data -> Text("slot-content:$data") },
            )
        }
        composeRule.onNodeWithText("slot-loading").assertIsDisplayed()

        state = AsyncContentState.Empty
        composeRule.waitForIdle()
        composeRule.onNode(livePolite("slot-empty")).assertExists()

        state = AsyncContentState.Error(message = "boom")
        composeRule.waitForIdle()
        composeRule.onNodeWithText("slot-error:boom").assertIsDisplayed()

        state = AsyncContentState.Content(CONTENT)
        composeRule.waitForIdle()
        composeRule.onNodeWithText("slot-content:$CONTENT").assertIsDisplayed()
    }

    private companion object {
        const val LOADING = "Loading projects"
        const val EMPTY_TITLE = "No projects"
        const val EMPTY_MESSAGE = "Create your first project"
        const val CREATE = "Create project"
        const val ERROR_TITLE = "Could not load"
        const val ERROR_MESSAGE = "Check your connection"
        const val CUSTOM_ERROR = "The server is unavailable"
        const val RETRY = "Retry"
        const val CONTENT = "Project list"
        const val OTHER_CONTENT = "Updated project list"
    }
}
