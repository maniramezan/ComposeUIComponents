package io.github.maniramezan.compose.sample

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.maniramezan.compose.components.NoticeBanner
import io.github.maniramezan.compose.components.NoticeSeverity
import io.github.maniramezan.compose.theme.AppTheme

private val SEVERITY_OPTIONS = NoticeSeverity.entries.map { it.name }

@Composable
internal fun NoticeBannerPage() {
    var severityIndex by remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    var showIcon by remember { mutableStateOf(true) }
    var describeIcon by remember { mutableStateOf(true) }
    var showAction by remember { mutableStateOf(true) }
    var showDismiss by remember { mutableStateOf(true) }
    var longMessage by remember { mutableStateOf(false) }
    var actionCount by remember { mutableIntStateOf(0) }

    val severity = NoticeSeverity.entries[severityIndex]

    SamplePage(
        preview = {
            if (visible) {
                NoticeBanner(
                    message =
                        if (longMessage) {
                            "Some items could not be updated. They stay as they were and will be retried " +
                                "automatically the next time a connection is available."
                        } else {
                            "Notice message"
                        },
                    severity = severity,
                    icon = if (showIcon) AppTheme.icons.check else null,
                    iconContentDescription = if (describeIcon) severity.name else null,
                    actionLabel = if (showAction) "Action" else null,
                    onAction = if (showAction) ({ actionCount++ }) else null,
                    dismissContentDescription = if (showDismiss) "Dismiss" else null,
                    onDismiss = if (showDismiss) ({ visible = false }) else null,
                )
            }
            Text("Action taps: $actionCount")
        },
        controls = {
            ControlSegmented(
                label = "Severity",
                options = SEVERITY_OPTIONS,
                selectedIndex = severityIndex,
                onOptionSelected = { severityIndex = it },
            )
            ControlSwitch(label = "Visible", checked = visible, onCheckedChange = { visible = it })
            ControlSwitch(label = "Icon", checked = showIcon, onCheckedChange = { showIcon = it })
            ControlSwitch(label = "Icon description", checked = describeIcon, onCheckedChange = { describeIcon = it })
            ControlSwitch(label = "Action", checked = showAction, onCheckedChange = { showAction = it })
            ControlSwitch(label = "Dismiss", checked = showDismiss, onCheckedChange = { showDismiss = it })
            ControlSwitch(label = "Long message", checked = longMessage, onCheckedChange = { longMessage = it })
        },
    )
}
