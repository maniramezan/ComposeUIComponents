package io.github.maniramezan.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import io.github.maniramezan.compose.utils.PreviewFontScale
import io.github.maniramezan.compose.utils.PreviewLightDark

@PreviewLightDark
@PreviewFontScale
@Preview(name = "Disclosure Card", group = "Containers")
@Composable
public fun DisclosureCardPreview(): Unit =
    AppTheme(icons = defaultAppIcons()) {
        var expanded by remember { mutableStateOf(false) }
        DisclosureCard(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            expandedStateDescription = "Expanded",
            collapsedStateDescription = "Collapsed",
            summary = { Text(text = "Word of the day") },
            detail = { Text(text = "A word selected for today.") },
        )
    }

@ShowkaseComposable(name = "Disclosure Card", group = "Containers")
@Composable
public fun DisclosureCardShowkase(): Unit = DisclosureCardPreview()
