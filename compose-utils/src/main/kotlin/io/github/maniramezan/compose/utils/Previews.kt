package io.github.maniramezan.compose.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
public annotation class PreviewLightDark

@Preview(name = "Font 100%", fontScale = 1.0f)
@Preview(name = "Font 200%", fontScale = 2.0f)
public annotation class PreviewFontScale
