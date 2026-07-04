package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Containers", group = "Containers")
@Composable
public fun ContainerComponentsShowkase(): Unit = ContainerComponentsPreview()

@ShowkaseComposable(name = "Card", group = "Containers")
@Composable
public fun CardShowkase(): Unit =
    AppTheme {
        Card {
            Text(text = "Plan")
            Text(text = "Compose Pro")
        }
    }

@ShowkaseComposable(name = "Clickable Card", group = "Containers")
@Composable
public fun ClickableCardShowkase(): Unit =
    AppTheme {
        Card(onClick = {}) {
            Text(text = "Tap me")
        }
    }

@ShowkaseComposable(name = "Surface", group = "Containers")
@Composable
public fun SurfaceShowkase(): Unit =
    AppTheme {
        Surface {
            Text(text = "Surface content")
        }
    }

@ShowkaseComposable(name = "Section", group = "Containers")
@Composable
public fun SectionShowkase(): Unit =
    AppTheme {
        Section(title = "Account") {
            Text(text = "Profile")
        }
    }

@ShowkaseComposable(name = "Dialog", group = "Containers")
@Composable
public fun DialogShowkase(): Unit = DialogPreview()

@ShowkaseComposable(name = "Bottom Sheet", group = "Containers")
@Composable
public fun BottomSheetShowkase(): Unit = BottomSheetPreview()

@ShowkaseComposable(name = "Snackbar", group = "Containers")
@Composable
public fun SnackbarShowkase(): Unit =
    AppTheme {
        Snackbar(
            message = "Saved",
            actionLabel = "Undo",
            onAction = {},
        )
    }
