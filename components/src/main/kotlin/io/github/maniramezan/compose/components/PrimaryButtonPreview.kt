package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "PrimaryButton", group = "Actions")
@Composable
public fun PrimaryButtonPreview(): Unit =
    AppTheme {
        PrimaryButton(
            text = "Continue",
            onClick = {},
        )
    }
