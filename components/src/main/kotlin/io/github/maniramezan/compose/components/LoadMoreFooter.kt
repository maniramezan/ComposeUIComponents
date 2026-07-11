package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A footer for infinite-scrolling lazy lists: entering composition triggers
 * [onLoadMore] once, and a themed spinner shows while the next page loads.
 *
 * Place this as the last item in a `LazyColumn`/`LazyRow` (typically guarded
 * by a `hasMore` flag so it disappears once there is nothing left to load).
 * Compose disposes lazy items once they scroll outside the active window and
 * recomposes them fresh when they scroll back in, so as appended content
 * pushes this footer off-screen and the user scrolls back down to it, this
 * fires again — no manual "have I already triggered this page" bookkeeping
 * needed beyond what the caller already tracks for [isLoadingMore].
 *
 * ```kotlin
 * LazyColumn {
 *     items(items = list, key = { it.id }) { ItemRow(it) }
 *     if (hasMore) {
 *         item(key = "load-more") {
 *             LoadMoreFooter(isLoadingMore = isLoadingMore, onLoadMore = viewModel::loadMore)
 *         }
 *     }
 * }
 * ```
 *
 * @param isLoadingMore Whether a page request is in flight. Shows a spinner
 *   while `true`.
 * @param onLoadMore Invoked once when this footer enters composition. Callers
 *   are responsible for guarding against duplicate in-flight requests.
 */
@Composable
public fun LoadMoreFooter(
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
) {
    LaunchedEffect(Unit) { onLoadMore() }

    if (isLoadingMore) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.spacing.x2)
                    // Announce the transition into the loading state.
                    .semantics { liveRegion = LiveRegionMode.Polite },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}
