package io.github.maniramezan.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.theme.IconToken
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

/**
 * How long a toast stays visible before it dismisses itself.
 *
 * [Short] and [Long] auto-dismiss after a fixed timeout; [Indefinite] stays
 * until the user or the caller dismisses it (via the action button, a
 * tap-to-dismiss affordance, or [ToastData.dismiss]).
 */
public enum class ToastDuration {
    Short,
    Long,
    Indefinite,
}

/** Screen edge a [ToastHost] anchors its toast to. */
public enum class ToastPosition {
    /** Top edge; the toast slides in from the top. */
    Top,

    /** Bottom edge; the toast slides in from the bottom. */
    Bottom,
}

/** Outcome of a [ToastHostState.showToast] call. */
public enum class ToastResult {
    /** The user tapped the action button. */
    ActionPerformed,

    /** The toast was dismissed by timeout, user, or caller without acting. */
    Dismissed,
}

/** Visible-timeout in milliseconds, or `null` for [ToastDuration.Indefinite]. */
internal val ToastDuration.timeoutMillis: Long?
    get() =
        when (this) {
            ToastDuration.Short -> 4_000L
            ToastDuration.Long -> 10_000L
            ToastDuration.Indefinite -> null
        }

/**
 * The currently shown toast. Exposed to [ToastHost] (and custom `toast` slots)
 * so they can render the message/action and drive its lifecycle.
 */
@Stable
public interface ToastData {
    public val message: String
    public val icon: IconToken?
    public val iconContentDescription: String?
    public val iconTint: Color?
    public val actionLabel: String?
    public val duration: ToastDuration

    /** Resolve the originating [ToastHostState.showToast] with [ToastResult.ActionPerformed]. */
    public fun performAction()

    /** Resolve the originating [ToastHostState.showToast] with [ToastResult.Dismissed]. */
    public fun dismiss()
}

/**
 * State holder that shows one toast at a time. Mirrors the
 * `SnackbarHostState` model: call [showToast] from a coroutine and it suspends
 * until the toast is dismissed (by timeout, the user, or the caller), returning
 * how it ended. Concurrent calls queue and run one after another.
 *
 * Pair with a [ToastHost] placed over your screen content. Obtain an instance
 * with [rememberToastHostState].
 */
@Stable
public class ToastHostState {
    public var currentToastData: ToastData? by mutableStateOf(null)
        private set

    private val mutex = Mutex()

    /**
     * Show [message] and suspend until the toast is dismissed.
     *
     * @param message visible toast text (caller-supplied, no default).
     * @param icon optional leading icon shown before the message.
     * @param iconContentDescription accessibility label for [icon]; leave null
     *   when the message already conveys the icon's meaning (decorative icon).
     * @param iconTint tint for [icon]; null uses the default surface tint. Pass
     *   [Color.Unspecified] to keep a colorful icon's own colors.
     * @param actionLabel optional action button label; when set, an action
     *   button is shown and tapping it returns [ToastResult.ActionPerformed].
     * @param duration auto-dismiss timeout, or [ToastDuration.Indefinite] to
     *   keep the toast until the user or caller dismisses it. Defaults to
     *   [ToastDuration.Short] without an action and [ToastDuration.Long] with one.
     */
    public suspend fun showToast(
        message: String,
        icon: IconToken? = null,
        iconContentDescription: String? = null,
        iconTint: Color? = null,
        actionLabel: String? = null,
        duration: ToastDuration = if (actionLabel == null) ToastDuration.Short else ToastDuration.Long,
    ): ToastResult =
        mutex.withLock {
            try {
                suspendCancellableCoroutine { continuation ->
                    currentToastData =
                        ToastDataImpl(
                            message = message,
                            icon = icon,
                            iconContentDescription = iconContentDescription,
                            iconTint = iconTint,
                            actionLabel = actionLabel,
                            duration = duration,
                            continuation = continuation,
                        )
                }
            } finally {
                currentToastData = null
            }
        }
}

private class ToastDataImpl(
    override val message: String,
    override val icon: IconToken?,
    override val iconContentDescription: String?,
    override val iconTint: Color?,
    override val actionLabel: String?,
    override val duration: ToastDuration,
    private val continuation: CancellableContinuation<ToastResult>,
) : ToastData {
    override fun performAction() {
        if (continuation.isActive) continuation.resume(ToastResult.ActionPerformed)
    }

    override fun dismiss() {
        if (continuation.isActive) continuation.resume(ToastResult.Dismissed)
    }
}

/** Remember a [ToastHostState] across recompositions. */
@Composable
public fun rememberToastHostState(): ToastHostState = remember { ToastHostState() }

/**
 * Overlay that displays the toast currently held by [hostState], handling
 * enter/exit animation, auto-dismiss timing, and edge-safe placement. Place it
 * as the last child of your screen so it floats above the content.
 *
 * @param position screen edge the toast anchors to; defaults to
 *   [ToastPosition.Bottom]. [ToastPosition.Top] slides in from the top edge.
 * @param dismissContentDescription accessibility label for the tap-to-dismiss
 *   action applied to an [ToastDuration.Indefinite] toast that has no action
 *   button (so it is never undismissable). Caller-supplied; pass a localized
 *   string such as "Dismiss".
 * @param toast slot that renders a [ToastData]. Defaults to the [Toast] visual.
 */
@Composable
public fun ToastHost(
    hostState: ToastHostState,
    modifier: Modifier = Modifier,
    position: ToastPosition = ToastPosition.Bottom,
    dismissContentDescription: String? = null,
    toast: @Composable (ToastData) -> Unit = { data ->
        DefaultToast(data = data, dismissContentDescription = dismissContentDescription)
    },
) {
    val current = hostState.currentToastData

    // Run the auto-dismiss timer per shown toast; Indefinite toasts have none.
    LaunchedEffect(current) {
        val timeout = current?.duration?.timeoutMillis
        if (current != null && timeout != null) {
            delay(timeout)
            current.dismiss()
        }
    }

    // Retain the last toast so the exit animation can keep rendering it while
    // the host state has already cleared to null.
    var lastData by remember { mutableStateOf(current) }
    if (current != null) {
        lastData = current
    }

    val isTop = position == ToastPosition.Top
    val contentAlignment = if (isTop) Alignment.TopCenter else Alignment.BottomCenter

    val slideOffset: (Int) -> Int = { fullHeight -> if (isTop) -fullHeight else fullHeight }
    val slideSpec =
        tween<IntOffset>(
            durationMillis = AppTheme.motion.mediumMillis,
            easing = AppTheme.motion.emphasizedEasing,
        )
    val fadeSpec =
        tween<Float>(
            durationMillis = AppTheme.motion.mediumMillis,
            easing = AppTheme.motion.emphasizedEasing,
        )

    Box(modifier = modifier.fillMaxSize(), contentAlignment = contentAlignment) {
        AnimatedVisibility(
            visible = current != null,
            enter = slideInVertically(animationSpec = slideSpec, initialOffsetY = slideOffset) + fadeIn(fadeSpec),
            exit = slideOutVertically(animationSpec = slideSpec, targetOffsetY = slideOffset) + fadeOut(fadeSpec),
        ) {
            val data = lastData
            if (data != null) {
                Box(
                    modifier =
                        Modifier
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                            .padding(AppTheme.spacing.md),
                ) {
                    toast(data)
                }
            }
        }
    }
}

@Composable
private fun DefaultToast(
    data: ToastData,
    dismissContentDescription: String?,
) {
    // A persistent toast without an action would otherwise have no way to go
    // away; make its surface tap-to-dismiss so the user can clear it.
    val isPersistentWithoutAction =
        data.duration == ToastDuration.Indefinite && data.actionLabel == null
    val tapModifier =
        if (isPersistentWithoutAction) {
            Modifier.clickable(onClickLabel = dismissContentDescription) { data.dismiss() }
        } else {
            Modifier
        }

    Toast(
        message = data.message,
        modifier = tapModifier,
        icon = data.icon,
        iconContentDescription = data.iconContentDescription,
        iconTint = data.iconTint,
        actionLabel = data.actionLabel,
        onAction = if (data.actionLabel != null) data::performAction else null,
    )
}
