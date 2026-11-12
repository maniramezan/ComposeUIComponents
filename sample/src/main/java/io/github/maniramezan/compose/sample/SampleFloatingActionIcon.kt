package io.github.maniramezan.compose.sample

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.maniramezan.compose.components.MenuPicker
import io.github.maniramezan.compose.theme.IconToken

internal enum class FloatingActionIcon(
    val title: String,
    val imageVector: ImageVector,
) {
    Plus("Plus", Icons.Default.Add),
    Close("Close", Icons.Default.Close),
    Edit("Edit", Icons.Default.Edit),
    Star("Star", Icons.Default.Star),
    More("More", Icons.Default.MoreVert),
    ;

    val token: IconToken get() = IconToken(imageVector)
}

@Composable
internal fun FloatingActionIconPicker(
    label: String,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
) {
    MenuPicker(
        options = FloatingActionIcon.entries,
        selectedOption = FloatingActionIcon.entries[selectedIndex],
        onOptionSelected = { onSelectedIndexChange(it.ordinal) },
        optionLabel = { it.title },
        label = label,
    )
}
