package io.github.maniramezan.compose.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import io.github.maniramezan.compose.components.FAB
import io.github.maniramezan.compose.components.IconButton
import io.github.maniramezan.compose.components.PrimaryButton
import io.github.maniramezan.compose.components.SecondaryButton
import io.github.maniramezan.compose.components.SegmentedControl
import io.github.maniramezan.compose.components.TextButton
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

public class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(icons = defaultAppIcons()) {
                MaterialTheme {
                    Scaffold { innerPadding ->
                        Surface(modifier = Modifier.padding(innerPadding)) {
                            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                                Text("Compose UI Catalog")
                                PrimaryButton(text = "Primary", onClick = {})
                                SecondaryButton(text = "Secondary", onClick = {})
                                TextButton(text = "Text", onClick = {})
                                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                                    IconButton(
                                        icon = AppTheme.icons.check,
                                        contentDescription = "Confirm",
                                        onClick = {},
                                    )
                                    FAB(
                                        icon = AppTheme.icons.check,
                                        contentDescription = "Create",
                                        onClick = {},
                                    )
                                }
                                SegmentedControl(
                                    options = listOf("Day", "Week", "Month"),
                                    selectedIndex = 1,
                                    onOptionSelected = {},
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
