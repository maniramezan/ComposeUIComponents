package io.github.maniramezan.compose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.github.maniramezan.compose.components.TabBar
import io.github.maniramezan.compose.components.TabBarItemData
import io.github.maniramezan.compose.components.TopAppBar
import io.github.maniramezan.compose.components.rememberTabBarScrollBehavior
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme

/**
 * A genuinely full-screen [TabBar] demo — its own [ComponentActivity], not embedded inside the
 * component browser's padded detail pane. Launch this to judge the bar's real on-device height,
 * inset handling, and scroll-to-hide behavior the way an actual app would present it, rather
 * than a scaled-down inline preview.
 */
public class TabBarFullScreenDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                icons = defaultAppIcons(),
                dynamicColor = true,
            ) {
                TabBarFullScreenDemo(onClose = { finish() })
            }
        }
    }
}

private enum class FullScreenDemoDestination(
    val label: String,
) {
    HOME("Home"),
    FAVORITES("Favorites"),
    PROFILE("Profile"),
}

@Composable
private fun TabBarFullScreenDemo(onClose: () -> Unit) {
    var selection by rememberSaveable { mutableStateOf(FullScreenDemoDestination.HOME) }
    val scrollBehavior = rememberTabBarScrollBehavior()

    val items =
        remember {
            listOf(
                TabBarItemData(
                    value = FullScreenDemoDestination.HOME,
                    icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Home") },
                ),
                TabBarItemData(
                    value = FullScreenDemoDestination.FAVORITES,
                    icon = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = null) },
                    label = { Text("Favorites") },
                ),
                TabBarItemData(
                    value = FullScreenDemoDestination.PROFILE,
                    icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) },
                    label = { Text("Profile") },
                ),
            )
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = selection.label,
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close demo")
                    }
                },
            )
        },
        bottomBar = {
            TabBar(
                items = items,
                selection = selection,
                onSelectionChange = { selection = it },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            items(30) { index ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clip(AppTheme.shapes.large)
                            .background(AppTheme.colors.surfaceContainer),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "${selection.label} item #$index",
                        modifier = Modifier.padding(horizontal = AppTheme.spacing.lg),
                    )
                }
            }
        }
    }
}
