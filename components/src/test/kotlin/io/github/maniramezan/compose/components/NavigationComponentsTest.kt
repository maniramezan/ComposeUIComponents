package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class NavigationComponentsTest {
    @Test
    fun navigationComponentNamesAreStable() {
        val expected =
            listOf(
                "TopAppBar",
                "MediumTopAppBar",
                "LargeTopAppBar",
                "TabBar",
                "TabRow",
                "NavRail",
                "AdaptiveNavScaffold",
            )
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
        ).containsExactlyElementsIn(expected).inOrder()
    }
}
