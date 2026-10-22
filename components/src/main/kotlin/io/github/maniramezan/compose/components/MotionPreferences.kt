package io.github.maniramezan.compose.components

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * Whether decorative, looping animations (shimmer sweeps, typing dots) should run.
 *
 * Returns `false` under `@Preview` inspection and when the system animator duration scale is
 * `0` — the setting Android exposes for "Remove animations" / reduce motion. Components fall
 * back to a static frame in that case so the UI still conveys its state without movement.
 */
@Composable
internal fun rememberDecorativeAnimationsEnabled(): Boolean {
    val context = LocalContext.current
    val inInspection = LocalInspectionMode.current
    val reduceMotion =
        remember(context) {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            ) == 0f
        }
    return !inInspection && !reduceMotion
}
