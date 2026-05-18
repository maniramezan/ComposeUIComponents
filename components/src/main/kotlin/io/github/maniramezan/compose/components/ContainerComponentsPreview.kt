package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme

@Preview(name = "Container Components", group = "Containers")
@Composable
public fun ContainerComponentsPreview(): Unit =
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
            Section(title = "Account") {
                Card {
                    Text(text = "Plan")
                    Text(text = "Compose Pro")
                }
            }
            Surface {
                Text(text = "Surface content")
            }
            Snackbar(
                message = "Saved",
                actionLabel = "Undo",
                onAction = {},
            )
        }
    }

@Preview(name = "Dialog", group = "Containers")
@Composable
public fun DialogPreview(): Unit =
    AppTheme {
        Dialog(
            title = "Discard changes?",
            text = "Your unsaved edits will be lost.",
            confirmText = "Discard",
            dismissText = "Cancel",
            onConfirm = {},
            onDismissRequest = {},
        )
    }
