package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class NavigationComponentsTest {
    @Test
    fun navigationComponentNamesAreStable() {
        assertThat(
            listOf(
                "TopAppBar",
                "MediumTopAppBar",
                "LargeTopAppBar",
                "TabBar",
                "TabRow",
                "NavRail",
                "AdaptiveNavScaffold",
            ),
        ).containsExactly(
            "TopAppBar",
            "MediumTopAppBar",
            "LargeTopAppBar",
            "TabBar",
            "TabRow",
            "NavRail",
            "AdaptiveNavScaffold",
        ).inOrder()
    }
}
