package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SelectionSheetTest {
    private fun leaf(
        id: String,
        title: String,
        subtitle: String? = null,
    ) = SelectionSheetNode(id = id, title = title, subtitle = subtitle)

    private val nodes: List<SelectionSheetNode<String>> =
        listOf(
            leaf("water", "Water", subtitle = "still"),
            leaf("juice", "Juice"),
            SelectionSheetNode(
                id = "fruit",
                title = "Fruit",
                children =
                    listOf(
                        leaf("apple", "Apple", subtitle = "green"),
                        leaf("banana", "Banana", subtitle = "yellow"),
                        leaf("mango", "Mango", subtitle = "ripe"),
                    ),
            ),
        )

    @Test
    fun blankQueryReturnsAllNodes() {
        assertThat(filterSelectionSheetNodes(nodes, "").map { it.id })
            .containsExactly("water", "juice", "fruit")
            .inOrder()
        assertThat(filterSelectionSheetNodes(nodes, "   ").size).isEqualTo(nodes.size)
    }

    @Test
    fun matchesLeafByTitle() {
        assertThat(filterSelectionSheetNodes(nodes, "Juice").map { it.id }).containsExactly("juice")
    }

    @Test
    fun matchesLeafBySubtitleCaseInsensitively() {
        assertThat(filterSelectionSheetNodes(nodes, "STILL").map { it.id }).containsExactly("water")
    }

    @Test
    fun parentTitleMatchKeepsAllChildren() {
        val result = filterSelectionSheetNodes(nodes, "Fruit")
        assertThat(result.map { it.id }).containsExactly("fruit")
        assertThat(result.single().children.map { it.id })
            .containsExactly("apple", "banana", "mango")
            .inOrder()
    }

    @Test
    fun childMatchPrunesParentToMatchingChildren() {
        val result = filterSelectionSheetNodes(nodes, "Banana")
        assertThat(result.map { it.id }).containsExactly("fruit")
        assertThat(result.single().children.map { it.id }).containsExactly("banana")
    }

    @Test
    fun queryIsTrimmed() {
        val result = filterSelectionSheetNodes(nodes, "  Mango  ")
        assertThat(result.single().children.map { it.id }).containsExactly("mango")
    }

    @Test
    fun noMatchReturnsEmpty() {
        assertThat(filterSelectionSheetNodes(nodes, "zzz")).isEmpty()
    }

    @Test
    fun matchesPreserveOriginalOrder() {
        val flat = listOf(leaf("1", "Alpha test"), leaf("2", "Beta"), leaf("3", "Gamma test"))
        assertThat(filterSelectionSheetNodes(flat, "test").map { it.id })
            .containsExactly("1", "3")
            .inOrder()
    }

    @Test
    fun isLeafReflectsChildren() {
        assertThat(leaf("a", "A").isLeaf).isTrue()
        assertThat(SelectionSheetNode(id = "p", title = "P", children = listOf(leaf("c", "C"))).isLeaf).isFalse()
    }
}
