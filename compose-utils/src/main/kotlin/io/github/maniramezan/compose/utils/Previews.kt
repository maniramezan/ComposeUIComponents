package io.github.maniramezan.compose.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/** Renders the annotated preview in both light and dark UI modes. */
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
public annotation class PreviewLightDark

/** Renders the annotated preview at 100% and 200% font scale, the accessibility bar for components. */
@Preview(name = "Font 100%", fontScale = 1.0f)
@Preview(name = "Font 200%", fontScale = 2.0f)
public annotation class PreviewFontScale
