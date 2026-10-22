package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Loading Overlay", group = "Feedback")
@Composable
public fun LoadingOverlayPreview(): Unit =
    AppTheme {
        LoadingOverlay(
            loading = true,
            contentDescription = "Saving",
            message = "Saving changes",
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.x2),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.x2),
            ) {
                TextField(value = "Draft title", onValueChange = {}, label = "Title")
                TextField(value = "Draft notes", onValueChange = {}, label = "Notes")
                PrimaryButton(text = "Save", onClick = {})
            }
        }
    }
