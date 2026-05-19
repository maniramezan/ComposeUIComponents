package io.github.maniramezan.compose.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import io.github.maniramezan.compose.theme.AppColors
import io.github.maniramezan.compose.theme.AppTypography

private val LightColors = AppColors.light()
private val DarkColors = AppColors.dark()
private val Typography = AppTypography.default()

@ShowkaseColor(name = "Primary", group = "Light Colors")
public val ShowkaseLightPrimary: Color = LightColors.primary

@ShowkaseColor(name = "On Primary", group = "Light Colors")
public val ShowkaseLightOnPrimary: Color = LightColors.onPrimary

@ShowkaseColor(name = "Surface", group = "Light Colors")
public val ShowkaseLightSurface: Color = LightColors.surface

@ShowkaseColor(name = "On Surface", group = "Light Colors")
public val ShowkaseLightOnSurface: Color = LightColors.onSurface

@ShowkaseColor(name = "Surface Variant", group = "Light Colors")
public val ShowkaseLightSurfaceVariant: Color = LightColors.surfaceVariant

@ShowkaseColor(name = "Primary", group = "Dark Colors")
public val ShowkaseDarkPrimary: Color = DarkColors.primary

@ShowkaseColor(name = "On Primary", group = "Dark Colors")
public val ShowkaseDarkOnPrimary: Color = DarkColors.onPrimary

@ShowkaseColor(name = "Surface", group = "Dark Colors")
public val ShowkaseDarkSurface: Color = DarkColors.surface

@ShowkaseColor(name = "On Surface", group = "Dark Colors")
public val ShowkaseDarkOnSurface: Color = DarkColors.onSurface

@ShowkaseColor(name = "Surface Variant", group = "Dark Colors")
public val ShowkaseDarkSurfaceVariant: Color = DarkColors.surfaceVariant

@ShowkaseTypography(name = "Display", group = "Typography")
public val ShowkaseDisplayTypography: TextStyle = Typography.display

@ShowkaseTypography(name = "Title", group = "Typography")
public val ShowkaseTitleTypography: TextStyle = Typography.title

@ShowkaseTypography(name = "Body", group = "Typography")
public val ShowkaseBodyTypography: TextStyle = Typography.body

@ShowkaseTypography(name = "Label", group = "Typography")
public val ShowkaseLabelTypography: TextStyle = Typography.label
