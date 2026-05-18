package io.github.maniramezan.compose.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.theme.AppTheme

@ShowkaseComposable(name = "Actions", group = "Actions")
@Composable
public fun ActionComponentsShowkase(): Unit = ActionComponentsPreview()

@ShowkaseComposable(name = "Secondary Button", group = "Actions")
@Composable
public fun SecondaryButtonShowkase(): Unit = AppTheme {
    SecondaryButton(text = "Back", onClick = {})
}

@ShowkaseComposable(name = "Text Button", group = "Actions")
@Composable
public fun TextButtonShowkase(): Unit = AppTheme {
    TextButton(text = "Skip", onClick = {})
}

@ShowkaseComposable(name = "Icon Button", group = "Actions")
@Composable
public fun IconButtonShowkase(): Unit = AppTheme {
    IconButton(
        icon = AppTheme.icons.check,
        contentDescription = "Confirm",
        onClick = {},
    )
}

@ShowkaseComposable(name = "FAB", group = "Actions")
@Composable
public fun FABShowkase(): Unit = AppTheme {
    FAB(
        icon = AppTheme.icons.check,
        contentDescription = "Create",
        onClick = {},
    )
}

@ShowkaseComposable(name = "Segmented Control", group = "Actions")
@Composable
public fun SegmentedControlShowkase(): Unit = AppTheme {
    SegmentedControl(
        options = listOf("Day", "Week", "Month"),
        selectedIndex = 1,
        onOptionSelected = {},
    )
}
